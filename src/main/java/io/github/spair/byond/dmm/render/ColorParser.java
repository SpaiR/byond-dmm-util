package io.github.spair.byond.dmm.render;

import java.awt.Color;
import java.util.Optional;

final class ColorParser {

    static Optional<Color> parse(final String color) {
        if (color.startsWith("#")) {
            return Optional.of(Color.decode(color));
        } else if (!color.isEmpty()) {
            String hex = ByondColor.hexFromColorName(color);
            if (!hex.isEmpty()) {
                return Optional.of(Color.decode(hex));
            }
        }
        return Optional.empty();
    }

    private ColorParser() {
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
