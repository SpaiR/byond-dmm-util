package io.github.spair.byond.dmm;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StandardParser implements MapParser {

    private static final Pattern TILE = Pattern.compile("\"(\\w+)\"\\s=\\s\\((.*)\\)\n");
    private static final Pattern MAP =
            Pattern.compile("\\((\\d+?),(\\d+?),(\\d+?)\\)\\s=\\s\\{\"\\s*([\\na-zA-Z]+)\\s*\"}");

    private static final Pattern SPLIT_ITEM = Pattern.compile("(,|^)(?=/)");
    private static final Pattern SPLIT_VARS = Pattern.compile(";\\s(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    private static final Pattern SPLIT_VAR = Pattern.compile("\\s=\\s");

    static final Pattern SPLIT_NEW_LINE = Pattern.compile("\n");

    private static final Pattern ITEM_WITH_VAR = Pattern.compile("^(/.+)\\{(.*)}");

    private Map<String, TileInstance> tileInstances;
    private Pattern tileInstanceSplit;

    @Override
    public Dmm parse(final String dmmText) {
        tileInstances = parseTiles(collectTiles(dmmText));

        if (tileInstances.isEmpty()) {
            throw new IllegalArgumentException("No tiles found");
        }

        final int tileInstanceSize = tileInstances.keySet().iterator().next().length();
        tileInstanceSplit = Pattern.compile("(?<=\\G.{" + tileInstanceSize + "})");

        return createDmm(collectMaps(dmmText));
    }

    protected Dmm createDmm(final List<MapDeclaration> mapDeclarations) {
        if (mapDeclarations.isEmpty()) {
            throw new IllegalArgumentException("No maps found");
        }

        Dmm dmm = new Dmm();

        mapDeclarations.forEach(mapDeclaration -> {
            final int zLevelNum = mapDeclaration.getZ();
            dmm.getZLevelOrCreate(zLevelNum).setTiles(parseMapText(mapDeclaration.getMapText(), zLevelNum));
        });

        return dmm;
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

    private List<MapDeclaration> collectMaps(final String dmmText) {
        Matcher mapMatcher = MAP.matcher(dmmText);
        List<MapDeclaration> maps = new ArrayList<>();

        while (mapMatcher.find()) {
            int x = Integer.parseInt(mapMatcher.group(1));
            int y = Integer.parseInt(mapMatcher.group(2));
            int z = Integer.parseInt(mapMatcher.group(3));
            maps.add(new MapDeclaration(x, y, z, mapMatcher.group(4)));
        }

        return maps;
    }

    private Tile[][] parseMapText(final String mapText, final int currentZLevel) {
        String[] mapLines = SPLIT_NEW_LINE.split(mapText);
        Tile[][] tiles = new Tile[mapLines.length][tileInstanceSplit.split(mapLines[0]).length];

        int y = 0;

        for (String mapLine : mapLines) {
            int x = 0;

            for (String instance : tileInstanceSplit.split(mapLine)) {
                tiles[y][x] = new Tile(x + 1, y + 1, currentZLevel, tileInstances.get(instance));
                x++;
            }

            y++;
        }

        return tiles;
    }
}
