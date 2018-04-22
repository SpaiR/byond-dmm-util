package io.github.spair.byond.dmm;

import java.util.*;

final class TGMParser extends StandardParser implements MapParser {

    @Override
    protected Dmm createDmm(final List<MapDeclaration> mapDeclarations) {
        return super.createDmm(concatMapDeclarations(mapDeclarations));
    }

    @Override
    protected Map<String, String> collectTiles(final String dmmText) {
        return super.collectTiles(convertAndExtractTilesDefinition(dmmText));
    }

    private List<MapDeclaration> concatMapDeclarations(final List<MapDeclaration> mapDeclarations) {
        Map<Integer, MapDeclaration> tmpDeclarations = new HashMap<>();

        mapDeclarations.forEach(mapDeclaration -> {
            final int z = mapDeclaration.getZ();
            MapDeclaration levelDeclaration = tmpDeclarations.getOrDefault(z, new MapDeclaration(1, 1, z, ""));

            StringBuilder mapTextBuilder = new StringBuilder(levelDeclaration.getMapText());

            if (mapTextBuilder.length() > 0) {
                mapTextBuilder.append('\n');
            }

            mapTextBuilder.append(SPLIT_NEW_LINE.matcher(mapDeclaration.getMapText()).replaceAll(""));

            levelDeclaration.setMapText(mapTextBuilder.toString());
            tmpDeclarations.putIfAbsent(z, levelDeclaration);
        });

        return new ArrayList<>(tmpDeclarations.values());
    }

    private String convertAndExtractTilesDefinition(final String dmmText) {
        StringBuilder tileDefBuilder = new StringBuilder();
        Scanner scanner = new Scanner(dmmText);

        boolean isFirstLine = true;

        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();

            if (isFirstLine) {
                isFirstLine = false;
                continue;
            } else if (line.startsWith("(")) {
                break;
            } else if (line.startsWith("\"") && tileDefBuilder.length() > 0) {
                tileDefBuilder.append('\n');
            }

            tileDefBuilder.append(line);
        }

        scanner.close();

        return tileDefBuilder.toString();
    }
}
