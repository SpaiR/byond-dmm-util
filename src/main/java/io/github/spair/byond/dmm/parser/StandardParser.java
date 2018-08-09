package io.github.spair.byond.dmm.parser;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.ByondVars;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dmm.Dmm;
import io.github.spair.byond.dmm.DmmItem;
import io.github.spair.byond.dmm.Tile;
import io.github.spair.byond.dmm.TileItem;
import io.github.spair.byond.dmm.TileInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StandardParser implements MapParser {

    @SuppressWarnings("checkstyle:VisibilityModifier")
    final PatternHolder patternHolder = new PatternHolder();

    private Map<String, TileInstance> tileInstances = new HashMap<>();
    private Pattern tileInstanceSplit;

    @Override
    public final Dmm parse(final String dmmText, final Dme dme) {
        parseTiles(collectTiles(dmmText));

        if (tileInstances.isEmpty()) {
            throw new IllegalArgumentException("No tiles found");
        }

        computeTileInstanceSize();

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

    @SuppressWarnings("MagicNumber")
    protected String collectMap(final String dmmText) {
        Matcher mapMatcher = patternHolder.mapMatcher(dmmText);

        if (mapMatcher.find()) {
            return mapMatcher.group(4);
        }

        throw new IllegalArgumentException("No maps found in dmm");
    }

    protected Map<String, String> collectTiles(final String dmmText) {
        Matcher tileMatcher = patternHolder.tileMatcher(dmmText);
        Map<String, String> tiles = new HashMap<>();

        while (tileMatcher.find()) {
            tiles.put(tileMatcher.group(1), tileMatcher.group(2));
        }

        return tiles;
    }

    private void parseTiles(final Map<String, String> tiles) {
        tiles.forEach((key, value) -> {
            TileInstance tileInstance = new TileInstance(key);
            String[] allItems = patternHolder.splitItem(value);

            for (String item : allItems) {
                DmmItem dmmItem = new DmmItem();
                Matcher itemWithVar = patternHolder.itemWithVarMatcher(item);

                if (itemWithVar.find()) {
                    dmmItem.setType(itemWithVar.group(1));
                    String[] vars = patternHolder.splitVars(itemWithVar.group(2));

                    for (String varDefinition : vars) {
                        String[] splittedVarDefinition = patternHolder.splitVar(varDefinition);
                        dmmItem.setVar(splittedVarDefinition[0], splittedVarDefinition[1]);
                    }
                } else {
                    dmmItem.setType(item);
                }

                tileInstance.addDmmItem(dmmItem);
            }

            tileInstances.put(key, tileInstance);
        });
    }

    private Tile[][] parseMapText(final String mapText) {
        String[] mapLines = patternHolder.splitNewLine(mapText);
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

    private void computeTileInstanceSize() {
        final int tileInstanceSize = tileInstances.keySet().iterator().next().length();
        tileInstanceSplit = Pattern.compile(String.format("(?<=\\G.{%d})", tileInstanceSize));
    }
}
