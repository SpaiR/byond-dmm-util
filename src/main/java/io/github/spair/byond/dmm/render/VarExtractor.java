package io.github.spair.byond.dmm.render;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.ByondVars;
import io.github.spair.byond.dmi.SpriteDir;
import io.github.spair.byond.dmm.TileItem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("WeakerAccess")
final class VarExtractor {

    static final String EMPTY_STRING = "";
    static final String DEFAULT_LAYER_PLANE = "0";
    static final int DEFAULT_DIR = 2;
    static final Double DEFAULT_PIXEL_SHIFT = 0.0;
    static final int DEFAULT_ALPHA = 255;

    private static final Pattern RGB_PATTERN = Pattern.compile("rgb\\((.*),(.*),(.*)\\)");
    private static final String RGB_PREFIX = "rgb(";

    static double plane(final TileItem item) {
        return Double.parseDouble(item.getCustomOrOriginalVar(ByondVars.PLANE).orElse(DEFAULT_LAYER_PLANE));
    }

    static double layer(final TileItem item) {
        return Double.parseDouble(item.getCustomOrOriginalVar(ByondVars.LAYER).orElse(DEFAULT_LAYER_PLANE));
    }

    static String icon(final TileItem item) {
        return item.getCustomOrOriginalVarUnquoted(ByondVars.ICON).orElse(EMPTY_STRING);
    }

    static String iconState(final TileItem item) {
        return item.getCustomOrOriginalVarUnquoted(ByondVars.ICON_STATE).orElse(EMPTY_STRING);
    }

    static SpriteDir dir(final TileItem item) {
        return SpriteDir.valueOfByondDir(item.getCustomOrOriginalVarAsInt(ByondVars.DIR).orElse(DEFAULT_DIR));
    }

    static int pixelX(final TileItem item) {
        return item.getCustomOrOriginalVarAsDouble(ByondVars.PIXEL_X).orElse(DEFAULT_PIXEL_SHIFT).intValue();
    }

    static int pixelY(final TileItem item) {
        return item.getCustomOrOriginalVarAsDouble(ByondVars.PIXEL_Y).orElse(DEFAULT_PIXEL_SHIFT).intValue();
    }

    static int alpha(final TileItem item) {
        return item.getCustomOrOriginalVarAsInt(ByondVars.ALPHA).orElse(DEFAULT_ALPHA);
    }

    static String color(final TileItem item) {
        String colorValue = item.getCustomOrOriginalVar(ByondVars.COLOR).orElse(EMPTY_STRING);
        if (ByondTypes.NULL.equals(colorValue) || colorValue.isEmpty()) {
            return "";
        }
        if (colorValue.startsWith(RGB_PREFIX)) {
            return parseRGBColor(colorValue);
        }
        return colorValue.substring(1, colorValue.length() - 1);
    }

    @SuppressWarnings("MagicNumber")
    private static String parseRGBColor(final String rgb) {
        Matcher rgbMatch = RGB_PATTERN.matcher(rgb);
        if (rgbMatch.find()) {
            final int r = Integer.parseInt(rgbMatch.group(1).trim());
            final int g = Integer.parseInt(rgbMatch.group(2).trim());
            final int b = Integer.parseInt(rgbMatch.group(3).trim());
            return String.format("#%02x%02x%02x", r, g, b);
        } else {
            return EMPTY_STRING;
        }
    }

    private VarExtractor() {
    }
}
