package io.github.spair.byond.dmm.render;

import java.util.function.Function;

@SuppressWarnings("checkstyle:MagicNumber")
abstract class PixelEffect implements Function<Integer, Integer> {

    int resolvePixel(final int a, final int r, final int g, final int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
