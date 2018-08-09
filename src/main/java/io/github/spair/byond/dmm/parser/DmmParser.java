package io.github.spair.byond.dmm.parser;

import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dmm.Dmm;
import lombok.val;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;

@SuppressWarnings("WeakerAccess")
public final class DmmParser {

    private static final String DMM_SUFFIX = ".dmm";

    public static Dmm parse(final File dmmFile, final Dme dme) {
        try {
            if (!dmmFile.isFile() || !dmmFile.getName().endsWith(DMM_SUFFIX)) {
                throw new IllegalArgumentException("Parser only accept '.dmm' files");
            }

            val dmmText = readFile(dmmFile);

            if (dmmText.isEmpty()) {
                throw new IllegalArgumentException("File could not be empty");
            }

            return ParserFactory.createFromText(dmmText).parse(dmmText, dme);
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse dmm file. File path: " + dmmFile.getPath(), e);
        }
    }

    private static String readFile(final File dmmFile) {
        try (val reader = new BufferedReader(new FileReader(dmmFile))) {
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
