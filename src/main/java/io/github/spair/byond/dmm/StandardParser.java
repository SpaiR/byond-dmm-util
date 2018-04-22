package io.github.spair.byond.dmm;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StandardParser implements MapParser {

    private static final Pattern TILE = Pattern.compile("\"(\\w+)\"\\s=\\s\\((.*)\\)\n");
    private static final Pattern MAP =
            Pattern.compile("\\((\\d+?),(\\d+?),(\\d+?)\\)\\s=\\s\\{\"\\s*([\\na-zA-Z]+)\\s*\"}");

    private static final Pattern SPLIT_INSTANCE = Pattern.compile("(,|^)(?=/)");
    private static final Pattern SPLIT_VARS = Pattern.compile(";\\s(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    private static final Pattern SPLIT_VAR = Pattern.compile("\\s=\\s");

    static final Pattern SPLIT_NEW_LINE = Pattern.compile("\n");

    private static final Pattern INSTANCE_WITH_VAR = Pattern.compile("^(/.+)\\{(.*)}");

    private Map<String, TileInstance> tileInstances;
    private Pattern instanceSplitterator;

    @Override
    public Dmm parse(final String dmmText) {
        if (Objects.isNull(dmmText) || dmmText.isEmpty()) {
            throw new IllegalArgumentException("File could not be empty");
        }

        tileInstances = parseTiles(collectTiles(dmmText));

        if (tileInstances.isEmpty()) {
            throw new IllegalArgumentException("No tiles were found");
        }

        final int tileInstanceSize = tileInstances.keySet().iterator().next().length();
        instanceSplitterator = Pattern.compile("(?<=\\G.{" + tileInstanceSize + "})");

        return createDmm(collectMaps(dmmText));
    }

    protected Dmm createDmm(final List<MapDeclaration> mapDeclarations) {
        Dmm dmm = new Dmm();
        Map<Integer, ZLevel> zLevels = dmm.getZLevels();

        if (mapDeclarations.isEmpty()) {
            throw new IllegalArgumentException("No maps were found");
        }

        mapDeclarations.forEach(mapDeclaration -> {
            final int z = mapDeclaration.getZ();

            ZLevel zLevel = zLevels.getOrDefault(z, new ZLevel(z));
            zLevel.setTiles(parseMapText(mapDeclaration.getMapText(), z));
            zLevels.putIfAbsent(z, zLevel);
        });

        return dmm;
    }

    protected Map<String, String> collectTiles(final String dmmText) {
        Matcher tileMatcher = TILE.matcher(dmmText);
        Map<String, String> tiles = new TreeMap<>();

        while (tileMatcher.find()) {
            tiles.put(tileMatcher.group(1), tileMatcher.group(2));
        }

        return tiles;
    }

    private Map<String, TileInstance> parseTiles(final Map<String, String> tiles) {
        Map<String, TileInstance> tileInstances = new HashMap<>();

        tiles.forEach((key, value) -> {
            TileInstance tileInstance = new TileInstance(key);
            String[] allInstances = SPLIT_INSTANCE.split(value);

            for (String instance : allInstances) {
                DmmItem dmmItem = new DmmItem();
                Matcher instanceWithVar = INSTANCE_WITH_VAR.matcher(instance);

                if (instanceWithVar.find()) {
                    dmmItem.setType(instanceWithVar.group(1));
                    String[] vars = SPLIT_VARS.split(instanceWithVar.group(2));

                    for (String varDef : vars) {
                        String[] splittedVarDef = SPLIT_VAR.split(varDef);
                        dmmItem.getVars().put(splittedVarDef[0], splittedVarDef[1]);
                    }
                } else {
                    dmmItem.setType(instance);
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
        Tile[][] tiles = new Tile[mapLines.length][instanceSplitterator.split(mapLines[0]).length];

        int y = 0;

        for (String mapLine : mapLines) {
            int x = 0;

            for (String instance : instanceSplitterator.split(mapLine)) {
                tiles[y][x] = new Tile(x + 1, y + 1, currentZLevel, tileInstances.get(instance));
                x++;
            }

            y++;
        }

        return tiles;
    }
}
