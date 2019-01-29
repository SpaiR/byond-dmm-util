package io.github.spair.byond.dmm.drawer;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.ByondVars;
import io.github.spair.byond.dmm.Dmm;
import io.github.spair.byond.dmm.MapRegion;
import io.github.spair.byond.dmm.TileItem;
import lombok.val;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.Comparator;

final class Drawer {

    private final BufferedImage finalImage;

    private final int lowerX;
    private final int lowerY;
    private final int upperX;
    private final int upperY;

    private final Dmm dmm;

    private final FilterMode filterMode;
    private final Set<String> types;

    private final TreeMap<Double, TreeMap<Double, List<TileItem>>> planesLayers = new TreeMap<>();
    private final RenderPriorityComparator comparator = new RenderPriorityComparator();

    private final int iconSize;
    private final RenderShell shell;
    private final TileItemDrawer itemDrawer;

    Drawer(final Dmm dmm, final List<File> scripts, final MapRegion mapRegion, final FilterMode filterMode, final Set<String> types) {
        this.dmm = dmm;

        this.filterMode = filterMode;
        this.types = types;

        this.lowerX = mapRegion.getLowerX();
        this.lowerY = mapRegion.getLowerY();
        this.upperX = mapRegion.getUpperX();
        this.upperY = mapRegion.getUpperY();

        final int width = (upperX - lowerX + 1) * dmm.getIconSize();
        final int height = (upperY - lowerY + 1) * dmm.getIconSize();

        this.finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        this.iconSize = dmm.getIconSize();
        this.shell = new RenderShell(dmm, scripts);
        this.itemDrawer = new TileItemDrawer(iconSize, dmm.getDmeRootPath(), shell);
    }

    BufferedImage draw() {
        // Execute var scripts and distribute all items to planes/layers.
        for (val tile : dmm) {
            for (val tileItem : tile) {
                if (allowedByFilterMode(tileItem, types, filterMode) && isInBounds(tileItem)) {
                    shell.executeVarScripts(tileItem);
                    getPlaneLayer(tileItem.getVarDouble(ByondVars.PLANE), tileItem.getVarDouble(ByondVars.LAYER)).add(tileItem);
                }
            }
        }

        // Sort items on layers to render them in proper order.
        for (val plane : planesLayers.values()) {
            for (val layer : plane.values()) {
                layer.sort(comparator);
            }
        }

        // Actual rendering starts here.
        val finalCanvas = finalImage.getGraphics();
        for (val plane : planesLayers.values()) {
            for (val layer : plane.values()) {
                for (val tileItem : layer) {
                    val itemImage = itemDrawer.drawItem(tileItem);

                    if (itemImage == null)
                        continue;

                    int xPos = ((tileItem.getX() - lowerX) * iconSize) + itemImage.getXShift();
                    int yPos = ((upperY - tileItem.getY()) * iconSize) - itemImage.getYShift();
                    finalCanvas.drawImage(itemImage.getImage(), xPos, yPos, null);
                }
            }
        }
        finalCanvas.dispose();

        return finalImage;
    }

    private boolean allowedByFilterMode(final TileItem tileItem, final Set<String> types, final FilterMode filterMode) {
        switch (filterMode) {
            case IGNORE:
                return !isInTypes(tileItem, types);
            case INCLUDE:
                return isInTypes(tileItem, types);
            case EQUAL:
                return isInEqualTypes(tileItem, types);
            default:
                return true;
        }
    }

    private boolean isInTypes(final TileItem item, final Set<String> types) {
        for (val type : types) {
            if (item.isType(type)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInEqualTypes(final TileItem item, final Set<String> types) {
        for (val type : types) {
            if (item.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInBounds(final TileItem tileItem) {
        return tileItem.getX() >= lowerX && tileItem.getX() <= upperX && tileItem.getY() >= lowerY && tileItem.getY() <= upperY;
    }

    private List<TileItem> getPlaneLayer(final Double planeValue, final Double layerValue) {
        return planesLayers.computeIfAbsent(planeValue, k -> new TreeMap<>()).computeIfAbsent(layerValue, k -> new ArrayList<>());
    }

    private static final class RenderPriorityComparator implements Comparator<TileItem> {
        private static final String[] RENDER_PRIORITY = {
                ByondTypes.TURF,
                ByondTypes.OBJ,
                ByondTypes.MOB,
                ByondTypes.AREA
        };

        @Override
        public int compare(final TileItem o1, final TileItem o2) {
            val type1 = o1.getType();
            val type2 = o2.getType();

            for (val type : RENDER_PRIORITY) {
                if (type1.startsWith(type) && !type2.startsWith(type)) {
                    return -1;
                } else if (!type1.startsWith(type) && type2.startsWith(type)) {
                    return 1;
                }
            }

            return 0;
        }
    }
}
