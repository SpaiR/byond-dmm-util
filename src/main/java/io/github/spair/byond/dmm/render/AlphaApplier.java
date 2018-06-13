package io.github.spair.byond.dmm.render;

import java.awt.image.BufferedImage;

@SuppressWarnings("MagicNumber")
final class AlphaApplier {

    static void apply(final Integer alpha, final BufferedImage img) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int pixel = img.getRGB(x, y);

                if (pixel == 0) {
                    continue;
                }

                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                pixel = (alpha << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, pixel);
            }
        }
    }

    private AlphaApplier() {
    }
}
