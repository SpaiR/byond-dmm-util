package io.github.spair.byond.dmm.parser;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.ByondVars;
import io.github.spair.byond.dme.Dme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StandardParser implements MapParser {

    private static final Pattern TILE = Pattern.compile("\"(\\w+)\"\\s=\\s\\((.*)\\)\n");

    private static final Pattern SPLIT_ITEM = Pattern.compile("(,|^)(?=/)(?![^{]*[}])");
    private static final Pattern SPLIT_VARS = Pattern.compile(";\\s?(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    private static final Pattern SPLIT_VAR = Pattern.compile("\\s=\\s");

    static final Pattern SPLIT_NEW_LINE = Pattern.compile("\n");
    static final Pattern MAP =
            Pattern.compile("\\((\\d+?),(\\d+?),(\\d+?)\\)\\s=\\s\\{\"\\s*([\\na-zA-Z]+)\\s*\"}");

    private static final Pattern ITEM_WITH_VAR = Pattern.compile("^(/.+)\\{(.*)}");

    private Map<String, TileInstance> tileInstances;
    private Pattern tileInstanceSplit;

    @Override
    public final Dmm parse(final String dmmText, final Dme dme) {
        tileInstances = parseTiles(collectTiles(dmmText));

        if (tileInstances.isEmpty()) {
            throw new IllegalArgumentException("No tiles found");
        }

        final int tileInstanceSize = tileInstances.keySet().iterator().next().length();
        tileInstanceSplit = Pattern.compile(String.format("(?<=\\G.{%d})", tileInstanceSize));

        Dmm dmm = createDmm(collectMap(dmmText), dme);
        dmm.setTileInstances(tileInstances);

        return dmm;
    }

    private Dmm createDmm(final String mapText, final Dme dme) {
        if (mapText.isEmpty()) {
            throw new IllegalArgumentException("No maps found");
        }

        Dmm dmm = new Dmm();

        dmm.setTiles(parseMapText(mapText));
        dmm.setDmeRootPath(dme.getAbsoluteRootPath());
        dmm.setIconSize(dme.getItem(ByondTypes.WORLD).getVarAsInt(ByondVars.ICON_SIZE).orElse(Dmm.DEFAULT_ICON_SIZE));

        dmm.forEach(tile -> {
            List<TileItem> tileItems = new ArrayList<>();

            tile.getTileInstance().forEach(dmmItem ->
                    tileItems.add(
                            new TileItem(
                                    tile.getX(), tile.getY(), tile.getZ(),
                                    dme.getItem(dmmItem.getType()), dmmItem.getVars()
                            )
                    )
            );

            tile.setTileItems(tileItems);
        });

        return dmm;
    }

    protected String collectMap(final String dmmText) {
        Matcher mapMatcher = MAP.matcher(dmmText);

        if (mapMatcher.find()) {
            return mapMatcher.group(4);
        }

        throw new IllegalArgumentException("No maps found in dmm");
    }

    protected Map<String, String> collectTiles(final String dmmText) {
        Matcher tileMatcher = TILE.matcher(dmmText);
        Map<String, String> tiles = new HashMap<>();

        while (tileMatcher.find()) {
            tiles.put(tileMatcher.group(1), tileMatcher.group(2));
        }

        return tiles;
    }

    private Map<String, TileInstance> parseTiles(final Map<String, String> tiles) {
        Map<String, TileInstance> tileInstances = new HashMap<>();

        tiles.forEach((key, value) -> {
            TileInstance tileInstance = new TileInstance(key);
            String[] allItems = SPLIT_ITEM.split(value);

            for (String item : allItems) {
                DmmItem dmmItem = new DmmItem();
                Matcher itemWithVar = ITEM_WITH_VAR.matcher(item);

                if (itemWithVar.find()) {
                    dmmItem.setType(itemWithVar.group(1));
                    String[] vars = SPLIT_VARS.split(itemWithVar.group(2));

                    for (String varDefinition : vars) {
                        String[] splittedVarDefinition = SPLIT_VAR.split(varDefinition);
                        dmmItem.setVar(splittedVarDefinition[0], splittedVarDefinition[1]);
                    }
                } else {
                    dmmItem.setType(item);
                }

                tileInstance.addDmmItem(dmmItem);
            }

            tileInstances.put(key, tileInstance);
        });

        return tileInstances;
    }

    private Tile[][] parseMapText(final String mapText) {
        String[] mapLines = SPLIT_NEW_LINE.split(mapText);
        final int maxY = mapLines.length;
        final int maxX = tileInstanceSplit.split(mapLines[0]).length;
        Tile[][] tiles = new Tile[maxY][maxX];

        int y = maxY - 1;

        for (String mapLine : mapLines) {
            int x = 0;

            for (String instance : tileInstanceSplit.split(mapLine)) {
                tiles[y][x] = new Tile(x + 1, y + 1, 1, tileInstances.get(instance));
                x++;
            }

            y--;
        }

        return tiles;
    }
}
