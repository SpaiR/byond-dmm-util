package io.github.spair.byond.dmm.render;

@SuppressWarnings("checkstyle:MagicNumber")
final class AlphaPixelEffect extends PixelEffect {

    private final int newAlpha;

    AlphaPixelEffect(final int newAlpha) {
        this.newAlpha = newAlpha;
    }

    @Override
    public Integer apply(final Integer pixel) {
        return resolvePixel(newAlpha, (pixel >> 16) & 0xff, (pixel >> 8) & 0xff, pixel & 0xff);
    }
}
