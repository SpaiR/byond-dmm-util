package io.github.spair.byond.dmm.render;

import io.github.spair.byond.dmm.MapRegion;
import io.github.spair.byond.dmm.parser.Dmm;
import io.github.spair.byond.dmm.parser.TileItem;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Base64;
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

        final int width = (upperX - lowerY + 1) * dmm.getIconSize();
        final int height = (upperY - lowerY + 1) * dmm.getIconSize();
        this.finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public static BufferedImage renderToImage(final Dmm dmm) {
        return renderToImage(dmm, Collections.emptySet());
    }

    public static BufferedImage renderToImage(final Dmm dmm, final String... typesToIgnore) {
        return renderToImage(dmm, new HashSet<>(Arrays.asList(typesToIgnore)));
    }

    public static BufferedImage renderToImage(final Dmm dmm, final Set<String> typesToIgnore) {
        return renderToImage(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), typesToIgnore);
    }

    public static BufferedImage renderToImage(final Dmm dmm, final MapRegion mapRegion) {
        return renderToImage(dmm, mapRegion, Collections.emptySet());
    }

    public static BufferedImage renderToImage(final Dmm dmm, final MapRegion mapRegion, final String... typesToIgnore) {
        return renderToImage(dmm, mapRegion, new HashSet<>(Arrays.asList(typesToIgnore)));
    }

    public static BufferedImage renderToImage(
            final Dmm dmm, final MapRegion mapRegion, final Set<String> typesToIgnore) {
        final DmmRender dmmRender = new DmmRender(dmm, mapRegion);

        dmmRender.distributeToSortedPlanesAndLayers(typesToIgnore);
        dmmRender.placeAllItemsOnImage();

        return dmmRender.finalImage;
    }

    public static String renderToBase64(final Dmm dmm) {
        return renderToBase64(dmm, Collections.emptySet());
    }

    public static String renderToBase64(final Dmm dmm, final String... typesToIgnore) {
        return renderToBase64(dmm, new HashSet<>(Arrays.asList(typesToIgnore)));
    }

    public static String renderToBase64(final Dmm dmm, final Set<String> typesToIgnore) {
        return renderToBase64(dmm, MapRegion.of(1, 1, dmm.getMaxX(), dmm.getMaxY()), typesToIgnore);
    }

    public static String renderToBase64(final Dmm dmm, final MapRegion mapRegion) {
        return renderToBase64(dmm, mapRegion, Collections.emptySet());
    }

    public static String renderToBase64(final Dmm dmm, final MapRegion mapRegion, final String... typesToIgnore) {
        return renderToBase64(dmm, mapRegion, new HashSet<>(Arrays.asList(typesToIgnore)));
    }

    public static String renderToBase64(final Dmm dmm, final MapRegion mapRegion, final Set<String> typesToIgnore) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            BufferedImage image = renderToImage(dmm, mapRegion, typesToIgnore);
            ImageIO.write(image, "PNG", os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void distributeToSortedPlanesAndLayers(final Set<String> typesToIgnore) {
        dmm.forEach(tile ->
                tile.forEach(tileItem -> {
                    if (isIgnoredType(tileItem, typesToIgnore) || notInBounds(tileItem)) {
                        return;
                    }

                    final double itemPlane = VarExtractor.plane(tileItem);
                    final double itemLayer = VarExtractor.layer(tileItem);

                    planes.getPlane(itemPlane).getLayer(itemLayer).addItem(tileItem);
                })
        );

        planes.forEach(plane ->
                plane.forEach(layer ->
                        layer.items.sort(RenderPriority.COMPARATOR)
                )
        );
    }

    private boolean isIgnoredType(final TileItem item, final Set<String> typesToIgnore) {
        for (String type : typesToIgnore) {
            if (item.isType(type)) {
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
        final TileItemRender itemRender = new TileItemRender(dmm.getIconSize(), dmm.getDmeRootPath());
        final Graphics finalCanvas = finalImage.getGraphics();

        final int iconSize = dmm.getIconSize();

        planes.forEach(plane ->
                plane.forEach(layer ->
                        layer.forEach(tileItem ->
                                itemRender.renderItem(tileItem).ifPresent(itemImage -> {
                                    int xPos = ((tileItem.getX() - lowerX) * iconSize) + itemImage.getXShift();
                                    int yPos = ((upperY - tileItem.getY()) * iconSize) - itemImage.getYShift();

                                    finalCanvas.drawImage(itemImage.getImage(), xPos, yPos, null);
                                })
                        )
                )
        );

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

        @Nonnull
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

        @Nonnull
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

        @Nonnull
        @Override
        public Iterator<TileItem> iterator() {
            return items.iterator();
        }
    }
}
