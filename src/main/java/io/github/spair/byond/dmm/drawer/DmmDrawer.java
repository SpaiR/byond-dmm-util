package io.github.spair.byond.dmm.drawer;

import io.github.spair.byond.dmm.MapRegion;
import io.github.spair.byond.dmm.Dmm;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;

public final class DmmDrawer {

    public static BufferedImage drawMap(final Dmm dmm) {
        return drawMap(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), FilterMode.NONE);
    }

    public static BufferedImage drawMap(final Dmm dmm, final MapRegion mapRegion) {
        return drawMap(dmm, mapRegion, FilterMode.NONE);
    }

    public static BufferedImage drawMap(final Dmm dmm, final FilterMode filterMode, final String... types) {
        return drawMap(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), filterMode, types);
    }

    public static BufferedImage drawMap(final Dmm dmm, final MapRegion mapRegion, final FilterMode filterMode, final String... types) {
        return new Drawer(dmm, mapRegion, filterMode, new HashSet<>(Arrays.asList(types))).draw();
    }

    private DmmDrawer() {
    }
}
