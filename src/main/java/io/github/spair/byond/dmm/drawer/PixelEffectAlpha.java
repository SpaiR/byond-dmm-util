package io.github.spair.byond.dmm.drawer;

@SuppressWarnings("checkstyle:MagicNumber")
final class PixelEffectAlpha extends PixelEffect {

    private final int newAlpha;

    PixelEffectAlpha(final int newAlpha) {
        this.newAlpha = newAlpha;
    }

    @Override
    public Integer apply(final Integer pixel) {
        return resolvePixel(newAlpha, (pixel >> 16) & 0xff, (pixel >> 8) & 0xff, pixel & 0xff);
    }
}
