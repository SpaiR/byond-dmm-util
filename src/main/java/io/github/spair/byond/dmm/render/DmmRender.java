package io.github.spair.byond.dmm.render;

import io.github.spair.byond.dmm.MapRegion;
import io.github.spair.byond.dmm.Dmm;
import io.github.spair.byond.dmm.TileItem;
import io.github.spair.byond.dmm.comparator.DiffPoint;
import lombok.val;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collection;
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

    ////////////////////////////////////////
    // Render with ignored types
    //

    public static BufferedImage renderToImage(final Dmm dmm, final String... typesToIgnore) {
        return renderToImage(dmm, Arrays.asList(typesToIgnore));
    }

    public static BufferedImage renderToImage(final Dmm dmm, final Collection<String> typesToIgnore) {
        return renderToImage(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), typesToIgnore);
    }

    public static BufferedImage renderToImage(final Dmm dmm, final MapRegion mapRegion, final String... typesToIgnore) {
        return renderToImage(dmm, mapRegion, Arrays.asList(typesToIgnore));
    }

    public static BufferedImage renderToImage(
            final Dmm dmm, final MapRegion mapRegion, final Collection<String> typesToIgnore) {
        val dmmRender = new DmmRender(dmm, mapRegion);

        try {
            dmmRender.distributeToSortedPlanesAndLayers(typesToIgnore, DistributeType.IGNORE);
            dmmRender.placeAllItemsOnImage();
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Unable to render dmm image. Dme root path: %s Map region: %s Types to ignore: %s",
                    dmm.getDmeRootPath(), mapRegion, typesToIgnore
            ), e);
        }

        return dmmRender.finalImage;
    }

    ////////////////////////////////////////
    // Render with included types
    //

    public static BufferedImage renderToImageWithTypes(final Dmm dmm, final String... typesToInclude) {
        return renderToImageWithTypes(dmm, Arrays.asList(typesToInclude));
    }

    public static BufferedImage renderToImageWithTypes(final Dmm dmm, final Collection<String> typesToInclude) {
        return renderToImageWithTypes(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), typesToInclude);
    }

    public static BufferedImage renderToImageWithTypes(
            final Dmm dmm, final MapRegion mapRegion, final String... typesToInclude) {
        return renderToImageWithTypes(dmm, mapRegion, Arrays.asList(typesToInclude));
    }

    public static BufferedImage renderToImageWithTypes(
            final Dmm dmm, final MapRegion mapRegion, final Collection<String> typesToInclude) {
        val dmmRender = new DmmRender(dmm, mapRegion);

        try {
            dmmRender.distributeToSortedPlanesAndLayers(typesToInclude, DistributeType.INCLUDE);
            dmmRender.placeAllItemsOnImage();
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Unable to render dmm image. Dme root path: %s Map region: %s Types to include: %s",
                    dmm.getDmeRootPath(), mapRegion, typesToInclude
            ), e);
        }

        return dmmRender.finalImage;
    }

    ////////////////////////////////////////
    // Render with equal types only
    //

    public static BufferedImage renderToImageWithEqTypes(final Dmm dmm, final String... equalTypes) {
        return renderToImageWithEqTypes(dmm, Arrays.asList(equalTypes));
    }

    public static BufferedImage renderToImageWithEqTypes(final Dmm dmm, final Collection<String> equalTypes) {
        return renderToImageWithEqTypes(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), equalTypes);
    }

    public static BufferedImage renderToImageWithEqTypes(
            final Dmm dmm, final MapRegion mapRegion, final String... equalTypes) {
        return renderToImageWithEqTypes(dmm, mapRegion, Arrays.asList(equalTypes));
    }

    public static BufferedImage renderToImageWithEqTypes(
            final Dmm dmm, final MapRegion mapRegion, final Collection<String> equalTypes) {
        val dmmRender = new DmmRender(dmm, mapRegion);

        try {
            dmmRender.distributeToSortedPlanesAndLayers(equalTypes, DistributeType.EQUAL);
            dmmRender.placeAllItemsOnImage();
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Unable to render dmm image. Dme root path: %s Map region: %s Equal types: %s",
                    dmm.getDmeRootPath(), mapRegion, equalTypes
            ), e);
        }

        return dmmRender.finalImage;
    }

    ////////////////////////////////////////
    // Render with diff points only
    //

    public static BufferedImage renderDiffPoints(final Dmm dmm, final MapRegion mapRegion) {
        val region = MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY());
        region.addDiffPoint(mapRegion.getDiffPoints());

        val dmmRender = new DmmRender(dmm, region);

        try {
            dmmRender.drawDiffPoints(region);
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Unable to render dmm image. Dme root path: %s Map region: %s", dmm.getDmeRootPath(), region), e
            );
        }

        return dmmRender.finalImage;
    }

    ////////////////////////////////////////

    private void distributeToSortedPlanesAndLayers(
            final Collection<String> types, final DistributeType distributeType) {
        for (val tile : dmm) {
            for (val tileItem : tile) {
                if (notAllowedByDistributeType(tileItem, types, distributeType) || notInBounds(tileItem)) {
                    continue;
                }

                val itemPlane = VarExtractor.plane(tileItem);
                val itemLayer = VarExtractor.layer(tileItem);

                planes.getPlane(itemPlane).getLayer(itemLayer).addItem(tileItem);
            }
        }

        for (val plane : planes) {
            for (val layer : plane) {
                layer.items.sort(RenderPriority.COMPARATOR);
            }
        }
    }

    private boolean notAllowedByDistributeType(
            final TileItem tileItem, final Collection<String> types, final DistributeType distributeType) {
        val typesSet = new HashSet<String>(types);
        switch (distributeType) {
            case IGNORE:
                return isInTypes(tileItem, typesSet);
            case INCLUDE:
                return !isInTypes(tileItem, typesSet);
            case EQUAL:
                return !isInEqualTypes(tileItem, typesSet);
            default:
                return false;
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

    private boolean notInBounds(final TileItem tileItem) {
        return tileItem.getX() < lowerX || tileItem.getX() > upperX
                || tileItem.getY() < lowerY || tileItem.getY() > upperY;
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
            Plane plane = planes.get(planeValue);
            if (plane == null) {
                plane = new Plane();
                planes.put(planeValue, plane);
            }
            return plane;
        }

        @Override
        public Iterator<Plane> iterator() {
            return planes.values().iterator();
        }
    }

    private static final class Plane implements Iterable<Layer> {
        private Map<Double, Layer> layers = new TreeMap<>();

        Layer getLayer(final double layerValue) {
            Layer layer = layers.get(layerValue);
            if (layer == null) {
                layer = new Layer();
                layers.put(layerValue, layer);
            }
            return layer;
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

    private enum DistributeType {
        IGNORE, INCLUDE, EQUAL
    }
}
