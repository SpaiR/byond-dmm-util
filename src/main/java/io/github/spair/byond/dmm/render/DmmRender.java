package io.github.spair.byond.dmm.render;

import io.github.spair.byond.dmm.MapRegion;
import io.github.spair.byond.dmm.Dmm;
import io.github.spair.byond.dmm.TileItem;
import io.github.spair.byond.dmm.comparator.DiffPoint;
import lombok.val;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;

@SuppressWarnings("WeakerAccess")
public final class DmmRender {

    private final Planes planes;
    private final Dmm dmm;
    private final BufferedImage finalImage;

    private final int lowerX;
    private final int lowerY;
    private final int upperX;
    private final int upperY;

    private DmmRender(final Dmm dmm, final MapRegion mapRegion) {
        this.dmm = dmm;
        this.planes = new Planes();

        int minX = mapRegion.getLowerX();
        int minY = mapRegion.getLowerY();
        int maxX = mapRegion.getUpperX();
        int maxY = mapRegion.getUpperY();

        if (mapRegion.isSinglePoint()) {
            minX = Math.max(1, minX - 1);
            minY = Math.max(1, minY - 1);
            maxX = Math.min(dmm.getMaxX(), maxX + 1);
            maxY = Math.min(dmm.getMaxY(), maxY + 1);
        }

        this.lowerX = minX;
        this.lowerY = minY;
        this.upperX = maxX;
        this.upperY = maxY;

        final int width = (upperX - lowerX + 1) * dmm.getIconSize();
        final int height = (upperY - lowerY + 1) * dmm.getIconSize();
        this.finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Renders full map with all atoms on it.
     *
     * @param dmm map to render
     * @return image of rendered map
     */
    public static BufferedImage render(final Dmm dmm) {
        return render(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), FilterMode.NONE);
    }

    /**
     * Renders region of map with all atoms on it.
     *
     * @param dmm map to render
     * @param mapRegion region to render
     * @return image of rendered map
     */
    public static BufferedImage render(final Dmm dmm, final MapRegion mapRegion) {
        return render(dmm, mapRegion, FilterMode.NONE);
    }

    /**
     * Renders full map with type filtering.
     *
     * @param dmm map to render
     * @param filterMode filter mode to apply
     * @param types types to filter
     * @return image of rendered map
     */
    public static BufferedImage render(final Dmm dmm, final FilterMode filterMode, final String... types) {
        return render(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), filterMode, types);
    }

    /**
     * Renders region of map with type filtering.
     *
     * @param dmm map to render
     * @param mapRegion region to render
     * @param filterMode filter mode to apply
     * @param types types to filter
     * @return image of rendered map
     */
    public static BufferedImage render(
            final Dmm dmm, final MapRegion mapRegion, final FilterMode filterMode, final String... types) {
        val dmmRender = new DmmRender(dmm, mapRegion);
        val typesSet = new HashSet<String>(Arrays.asList(types));

        try {
            dmmRender.distributeToSortedPlanesAndLayers(typesSet, filterMode);
            dmmRender.placeAllItemsOnImage();
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Unable to render dmm image. Dme root path: %s Map region: %s Filter mode: %s Types: %s",
                    dmm.getDmeRootPath(), mapRegion, filterMode, typesSet
            ), e);
        }

        return dmmRender.finalImage;
    }

    /**
     * Renders full map with only diff points of provided {@link MapRegion}.
     *
     * @param dmm map to render
     * @param mapRegion {@link MapRegion} with diff points to render
     * @return image of rendered diff points
     */
    public static BufferedImage renderDiffPoints(final Dmm dmm, final MapRegion mapRegion) {
        val region = MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY());
        region.addDiffPoint(mapRegion.getDiffPoints());

        val dmmRender = new DmmRender(dmm, region);

        try {
            dmmRender.drawDiffPoints(region);
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Unable to render dmm diff points. Dme root path: %s Map region: %s", dmm.getDmeRootPath(), region
            ), e);
        }

        return dmmRender.finalImage;
    }

    private void distributeToSortedPlanesAndLayers(final Set<String> types, final FilterMode filterMode) {
        for (val tile : dmm) {
            for (val tileItem : tile) {
                if (allowedByFilterMode(tileItem, types, filterMode) && inBounds(tileItem)) {
                    val itemPlane = VarExtractor.plane(tileItem);
                    val itemLayer = VarExtractor.layer(tileItem);
                    planes.getPlane(itemPlane).getLayer(itemLayer).addItem(tileItem);
                }
            }
        }

        for (val plane : planes) {
            for (val layer : plane) {
                layer.items.sort(RenderPriority.COMPARATOR);
            }
        }
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
        for (String type : types) {
            if (item.isType(type)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInEqualTypes(final TileItem item, final Set<String> types) {
        for (String type : types) {
            if (item.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    private boolean inBounds(final TileItem tileItem) {
        return tileItem.getX() >= lowerX && tileItem.getX() <= upperX
                && tileItem.getY() >= lowerY && tileItem.getY() <= upperY;
    }

    private void placeAllItemsOnImage() {
        val iconSize = dmm.getIconSize();
        val itemRender = new TileItemRender(iconSize, dmm.getDmeRootPath());
        val finalCanvas = finalImage.getGraphics();

        for (val plane : planes) {
            for (val layer : plane) {
                for (val tileItem : layer) {
                    itemRender.renderItem(tileItem).ifPresent(itemImage -> {
                        int xPos = ((tileItem.getX() - lowerX) * iconSize) + itemImage.getXShift();
                        int yPos = ((upperY - tileItem.getY()) * iconSize) - itemImage.getYShift();

                        finalCanvas.drawImage(itemImage.getImage(), xPos, yPos, null);
                    });
                }
            }
        }

        finalCanvas.dispose();
    }

    private void drawDiffPoints(final MapRegion mapRegion) {
        val iconSize = dmm.getIconSize();
        val finalCanvas = finalImage.getGraphics();

        finalCanvas.setColor(Color.RED);

        for (DiffPoint diffPoint : mapRegion.getDiffPoints()) {
            val xPos = (diffPoint.getX() - lowerX) * iconSize;
            val yPos = (upperY - diffPoint.getY()) * iconSize;

            finalCanvas.drawRect(xPos, yPos, iconSize, iconSize);
            finalCanvas.fillRect(xPos, yPos, iconSize, iconSize);
        }

        finalCanvas.dispose();
    }

    private static final class Planes implements Iterable<Plane> {
        private Map<Double, Plane> planes = new TreeMap<>();

        Plane getPlane(final double planeValue) {
            return planes.computeIfAbsent(planeValue, k -> new Plane());
        }

        @Override
        public Iterator<Plane> iterator() {
            return planes.values().iterator();
        }
    }

    private static final class Plane implements Iterable<Layer> {
        private Map<Double, Layer> layers = new TreeMap<>();

        Layer getLayer(final double layerValue) {
            return layers.computeIfAbsent(layerValue, k -> new Layer());
        }

        @Override
        public Iterator<Layer> iterator() {
            return layers.values().iterator();
        }
    }

    private static final class Layer implements Iterable<TileItem> {
        private List<TileItem> items = new ArrayList<>();

        private void addItem(final TileItem tileItem) {
            items.add(tileItem);
        }

        @Override
        public Iterator<TileItem> iterator() {
            return items.iterator();
        }
    }
}
