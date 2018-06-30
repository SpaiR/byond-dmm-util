package io.github.spair.byond.dmm.render;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dmm.parser.TileItem;

import java.util.Comparator;

final class RenderPriority {

    private static final String[] RENDER_PRIORITY = {
            ByondTypes.TURF,
            ByondTypes.OBJ,
            ByondTypes.MOB,
            ByondTypes.AREA
    };

    static final Comparator<TileItem> COMPARATOR = (o1, o2) -> {
        final String type1 = o1.getType();
        final String type2 = o2.getType();

        for (String type : RENDER_PRIORITY) {
            if (type1.startsWith(type) && !type2.startsWith(type)) {
                return -1;
            } else if (!type1.startsWith(type) && type2.startsWith(type)) {
                return 1;
            }
        }

        return 0;
    };

    private RenderPriority() {
    }
}
