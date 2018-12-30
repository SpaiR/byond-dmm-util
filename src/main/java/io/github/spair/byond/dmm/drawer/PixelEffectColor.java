package io.github.spair.byond.dmm.drawer;

import java.awt.Color;

@SuppressWarnings("checkstyle:MagicNumber")
final class PixelEffectColor extends PixelEffect {

    private final int newAlpha;
    private final int newRed;
    private final int newGreen;
    private final int newBlue;

    PixelEffectColor(final Color color) {
        this.newAlpha = color.getAlpha();
        this.newRed = color.getRed();
        this.newGreen = color.getGreen();
        this.newBlue = color.getBlue();
    }

    @Override
    public Integer apply(final Integer pixel) {
        int a = (pixel >> 24) & 0xff;
        int r = (pixel >> 16) & 0xff;
        int g = (pixel >> 8) & 0xff;
        int b = pixel & 0xff;
        a = (a * newAlpha) / 255;
        r = (r * newRed) / 255;
        g = (g * newGreen) / 255;
        b = (b * newBlue) / 255;
        return resolvePixel(a, r, g, b);
    }
}
