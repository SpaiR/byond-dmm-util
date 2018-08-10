package io.github.spair.byond.dmm.render;

@SuppressWarnings("checkstyle:MagicNumber")
abstract class AbstractPixelEffect implements PixelEffect {

    int resolvePixel(final int a, final int r, final int g, final int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
