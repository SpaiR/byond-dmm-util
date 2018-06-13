package io.github.spair.byond.dmm;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.Objects;

public final class ResourceUtil {

    public static File readResourceFile(final String path) {
        return new File(Objects.requireNonNull(ResourceUtil.class.getClassLoader().getResource(path)).getFile());
    }

    public static String readResourceText(final String path) {
        try {
            return new String(Files.readAllBytes(readResourceFile(path).toPath()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
