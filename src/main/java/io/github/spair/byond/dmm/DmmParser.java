package io.github.spair.byond.dmm;

import java.io.*;

public final class DmmParser {

    private static final String DMM_SUFFIX = ".dmm";

    public Dmm parse(final File dmmFile) {
        if (dmmFile.isFile() && dmmFile.getName().endsWith(DMM_SUFFIX)) {
            String dmmText = readFile(dmmFile);
            MapParser mapParser = ParserFactory.createFromText(dmmText);
            return mapParser.parse(dmmText);
        } else {
            throw new IllegalArgumentException("Parser only accept '.dmm' files");
        }
    }

    private String readFile(final File dmmFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dmmFile))) {
            StringBuilder builder = new StringBuilder();
            reader.lines().forEach(line -> builder.append(line.trim()).append('\n'));
            return builder.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
