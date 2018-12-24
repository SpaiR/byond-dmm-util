package io.github.spair.byond.dmm.render;

import io.github.spair.byond.dmm.MapRegion;
import io.github.spair.byond.dmm.Dmm;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;

public final class DmmRender {

    public static BufferedImage render(final Dmm dmm) {
        return render(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), FilterMode.NONE);
    }

    public static BufferedImage render(final Dmm dmm, final MapRegion mapRegion) {
        return render(dmm, mapRegion, FilterMode.NONE);
    }

    public static BufferedImage render(final Dmm dmm, final FilterMode filterMode, final String... types) {
        return render(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), filterMode, types);
    }

    public static BufferedImage render(final Dmm dmm, final MapRegion mapRegion, final FilterMode filterMode, final String... types) {
        return new Drawer(dmm, mapRegion, filterMode, new HashSet<>(Arrays.asList(types))).draw();
    }

    private DmmRender() {
    }
}
