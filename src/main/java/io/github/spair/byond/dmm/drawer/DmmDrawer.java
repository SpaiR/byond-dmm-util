package io.github.spair.byond.dmm.drawer;

import io.github.spair.byond.dmm.MapRegion;
import io.github.spair.byond.dmm.Dmm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public final class DmmDrawer {

    public static BufferedImage drawMap(final Dmm dmm) {
        return drawMap(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), FilterMode.NONE);
    }

    public static BufferedImage drawMap(final Dmm dmm, final List<File> scripts) {
        return drawMap(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), FilterMode.NONE);
    }

    public static BufferedImage drawMap(final Dmm dmm, final MapRegion mapRegion) {
        return drawMap(dmm, mapRegion, FilterMode.NONE);
    }

    public static BufferedImage drawMap(final Dmm dmm, final List<File> scripts, final MapRegion mapRegion) {
        return drawMap(dmm, scripts, mapRegion, FilterMode.NONE);
    }

    public static BufferedImage drawMap(final Dmm dmm, final FilterMode filterMode, final String... types) {
        return drawMap(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), filterMode, types);
    }

    public static BufferedImage drawMap(final Dmm dmm, final List<File> scripts, final FilterMode filterMode, final String... types) {
        return drawMap(dmm, scripts, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), filterMode, types);
    }

    public static BufferedImage drawMap(final Dmm dmm, final MapRegion mapRegion, final FilterMode filterMode, final String... types) {
        return new Drawer(dmm, Collections.emptyList(), mapRegion, filterMode, new HashSet<>(Arrays.asList(types))).draw();
    }

    public static BufferedImage drawMap(
            final Dmm dmm, final List<File> scripts, final MapRegion mapRegion, final FilterMode filterMode, final String... types) {
        return new Drawer(dmm, scripts, mapRegion, filterMode, new HashSet<>(Arrays.asList(types))).draw();
    }

    private DmmDrawer() {
    }
}
