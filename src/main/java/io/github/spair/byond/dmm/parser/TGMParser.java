package io.github.spair.byond.dmm.parser;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;

final class TGMParser extends StandardParser implements MapParser {

    @Override
    protected Map<String, String> collectTiles(final String dmmText) {
        return super.collectTiles(convertAndExtractTilesDefinition(dmmText));
    }

    @Override
    @SuppressWarnings("MagicNumber")
    protected String collectMap(final String dmmText) {
        Matcher mapMatcher = patternHolder.mapMatcher(dmmText);
        List<List<String>> columns = new ArrayList<>();

        while (mapMatcher.find()) {
            String[] rows = patternHolder.splitNewLine(mapMatcher.group(4));

            if (columns.isEmpty()) {
                Arrays.stream(rows).<List<String>>map(r -> new ArrayList<>()).forEach(columns::add);
            }

            for (int currentY = 0; currentY < rows.length; currentY++) {
                columns.get(currentY).add(rows[currentY]);
            }
        }

        if (!columns.isEmpty()) {
            StringBuilder mapTextBuilder = new StringBuilder();

            columns.forEach(row -> {
                row.forEach(mapTextBuilder::append);
                mapTextBuilder.append('\n');
            });

            return mapTextBuilder.toString();
        } else {
            throw new IllegalArgumentException("Map not found in file");
        }
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

        tileDefBuilder.append('\n');
        scanner.close();

        return tileDefBuilder.toString();
    }
}
