package io.github.spair.byond.dmm.render;

@SuppressWarnings("checkstyle:MagicNumber")
final class AlphaPixelEffect extends AbstractPixelEffect {

    private final int newAlpha;

    AlphaPixelEffect(final int newAlpha) {
        this.newAlpha = newAlpha;
    }

    @Override
    public int apply(final int pixel) {
        return resolvePixel(newAlpha, (pixel >> 16) & 0xff, (pixel >> 8) & 0xff, pixel & 0xff);
    }
}
