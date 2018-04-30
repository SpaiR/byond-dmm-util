package io.github.spair.byond.dmm;

import io.github.spair.byond.dme.Dme;

import java.io.*;

@SuppressWarnings("WeakerAccess")
public final class DmmParser {

    private static final String DMM_SUFFIX = ".dmm";

    public Dmm parse(final File dmmFile) {
        if (dmmFile.isFile() && dmmFile.getName().endsWith(DMM_SUFFIX)) {
            final String dmmText = readFile(dmmFile);

            if (dmmText.isEmpty()) {
                throw new IllegalArgumentException("File could not be empty");
            }

            return ParserFactory.createFromText(dmmText).parse(dmmText);
        } else {
            throw new IllegalArgumentException("Parser only accept '.dmm' files");
        }
    }

    public Dmm parseAndInjectDme(final File dmmFile, final Dme dme) {
        Dmm dmm = parse(dmmFile);
        dmm.injectDme(dme);
        return dmm;
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
