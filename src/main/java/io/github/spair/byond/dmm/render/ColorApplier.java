package io.github.spair.byond.dmm.render;

import java.awt.Color;
import java.awt.image.BufferedImage;

@SuppressWarnings("MagicNumber")
final class ColorApplier {

    static void apply(final Color color, final BufferedImage img) {
        final int newAlpha = color.getAlpha();
        final int newRed = color.getRed();
        final int newGreen = color.getGreen();
        final int newBlue = color.getBlue();

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int pixel = img.getRGB(x, y);

                if (pixel == 0) {
                    continue;
                }

                int a = (pixel >> 24) & 0xff;
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                float factor = a / 255f;

                a = applyFactor(newAlpha, a, factor);
                r = applyFactor(newRed, r, factor);
                g = applyFactor(newGreen, g, factor);
                b = applyFactor(newBlue, b, factor);

                pixel = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, pixel);
            }
        }
    }

    private static int applyFactor(final int newColor, final int oldColor, final float factor) {
        return newColor == 0 ? 0 : (int) (newColor * (1 - factor) + oldColor * factor);
    }

    private ColorApplier() {
    }
}
