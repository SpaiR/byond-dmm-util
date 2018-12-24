package io.github.spair.byond.dmm.render;

import io.github.spair.byond.ByondTypes;
import lombok.val;

import java.awt.Color;
import java.util.regex.Pattern;

final class ColorExtractor {

    private static final Pattern RGB_PATTERN = Pattern.compile("rgb\\((.*),(.*),(.*)\\)");
    private static final String RGB_PREFIX = "rgb(";

    static Color extract(final String colorFromVar) {
        String colorValue = colorFromVar;

        if (ByondTypes.NULL.equals(colorValue) || colorValue.isEmpty()) {
            colorValue = "";
        } else if (colorValue.startsWith(RGB_PREFIX)) {
            colorValue = parseRGBColor(colorValue);
        }

        if (colorValue.startsWith("#")) {
            return Color.decode(colorValue);
        } else if (!colorValue.isEmpty()) {
            val hex = ByondColor.hexFromColorName(colorValue);
            if (!hex.isEmpty()) {
                return Color.decode(hex);
            }
        }

        return null;
    }

    private static String parseRGBColor(final String rgb) {
        val rgbMatch = RGB_PATTERN.matcher(rgb);
        if (rgbMatch.find()) {
            final int r = Integer.parseInt(rgbMatch.group(1).trim());
            final int g = Integer.parseInt(rgbMatch.group(2).trim());
            final int b = Integer.parseInt(rgbMatch.group(3).trim());
            return String.format("#%02x%02x%02x", r, g, b);
        } else {
            return "";
        }
    }

    private ColorExtractor() {
    }

    private enum ByondColor {

        BLACK("#000000"),
        SILVER("#C0C0C0"),
        GRAY("#808080"), GREY(GRAY.hex),
        WHITE("#FFFFFF"),
        MAROON("#800000"),
        RED("#FF0000"),
        PURPLE("#800080"),
        FUCHSIA("#FF00FF"), MAGENTA(FUCHSIA.hex),
        GREEN("#00C000"),
        LIME("#00FF00"),
        OLIVE("#808000"),
        GOLD("#808000"),
        YELLOW("#FFFF00"),
        NAVY("#000080"),
        BLUE("#0000FF"),
        TEAL("#008080"),
        AQUA("#00FFFF"),
        CYAN("#00FFFF");

        private String hex;

        ByondColor(final String hex) {
            this.hex = hex;
        }

        private static String hexFromColorName(final String colorName) {
            for (ByondColor byondColor : values()) {
                if (byondColor.name().equals(colorName.toUpperCase())) {
                    return byondColor.hex;
                }
            }
            return "";
        }
    }
}
