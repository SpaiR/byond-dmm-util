package io.github.spair.byond.dmm.render;

import java.awt.Color;

@SuppressWarnings("checkstyle:MagicNumber")
final class ColorPixelEffect extends PixelEffect {

    private final int newAlpha;
    private final int newRed;
    private final int newGreen;
    private final int newBlue;

    ColorPixelEffect(final Color color) {
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

        float factor = a / 255f;

        a = applyFactor(newAlpha, a, factor);
        r = applyFactor(newRed, r, factor);
        g = applyFactor(newGreen, g, factor);
        b = applyFactor(newBlue, b, factor);

        return resolvePixel(a, r, g, b);
    }

    private static int applyFactor(final int newColor, final int oldColor, final float factor) {
        return newColor == 0 ? 0 : (int) (newColor * (1 - factor) + oldColor * factor);
    }
}
