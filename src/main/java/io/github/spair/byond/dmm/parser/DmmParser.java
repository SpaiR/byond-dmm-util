package io.github.spair.byond.dmm.parser;

import io.github.spair.byond.dme.Dme;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;

@SuppressWarnings("WeakerAccess")
public final class DmmParser {

    private static final String DMM_SUFFIX = ".dmm";

    public static Dmm parse(final File dmmFile, final Dme dme) {
        if (dmmFile.isFile() && dmmFile.getName().endsWith(DMM_SUFFIX)) {
            final String dmmText = readFile(dmmFile);

            if (dmmText.isEmpty()) {
                throw new IllegalArgumentException("File could not be empty");
            }

            return ParserFactory.createFromText(dmmText).parse(dmmText, dme);
        } else {
            throw new IllegalArgumentException("Parser only accept '.dmm' files");
        }
    }

    private static String readFile(final File dmmFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dmmFile))) {
            StringBuilder builder = new StringBuilder();
            reader.lines().forEach(line -> builder.append(line.trim()).append('\n'));
            return builder.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private DmmParser() {
    }
}
