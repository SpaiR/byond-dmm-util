package io.github.spair.byond.dmm.comparator;

import io.github.spair.byond.dmm.Dmm;
import io.github.spair.byond.dmm.MapRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("WeakerAccess")
public final class DmmComparator {

    /**
     * Method to compare two {@link Dmm}'s and get {@link MapRegion} with area of changes.
     * Since maps could have different sizes,
     * generated {@link MapRegion} shows difference only in context of first argument.
     * <p>Result {@link MapRegion} will cover every changes on map, so it could be pretty big.
     * To get more accurate comparison result {@link #compareByChunks(Dmm, Dmm)} should be used.
     *
     * @param dmmToCompare   map {@link MapRegion} for which will be generated
     * @param dmmCompareWith map with which 'dmmToCompare' will be compared
     * @return {@link MapRegion} with area where changes found
     */
    public static Optional<MapRegion> compare(final Dmm dmmToCompare, final Dmm dmmCompareWith) {
        List<DiffPoint> diffPoints = findDiffPoints(dmmToCompare, dmmCompareWith);

        if (diffPoints.isEmpty()) {
            return Optional.empty();
        }

        int lowerX = Integer.MAX_VALUE;
        int lowerY = Integer.MAX_VALUE;
        int upperX = Integer.MIN_VALUE;
        int upperY = Integer.MIN_VALUE;

        for (DiffPoint diffPoint : diffPoints) {
            lowerX = Math.min(lowerX, diffPoint.getX());
            lowerY = Math.min(lowerY, diffPoint.getY());
            upperX = Math.max(upperX, diffPoint.getX());
            upperY = Math.max(upperY, diffPoint.getY());
        }

        return Optional.of(MapRegion.of(lowerX, lowerY, upperX, upperY).addDiffPoint(diffPoints));
    }

    /**
     * Method to compare two {@link Dmm}'s and get list of {@link MapRegion} with areas of changes.
     * Since maps could have different sizes,
     * generated list shows difference only in context of first argument.
     * <p>Unlike {@link #compare(Dmm, Dmm)} returns list of regions,
     * which could show map differences more accurately.
     *
     * @param dmmToCompare   map {@link MapRegion} for which will be generated
     * @param dmmCompareWith map with which 'dmmToCompare' will be compared
     * @return list of {@link MapRegion} with areas of changes
     */
    public static Optional<List<MapRegion>> compareByChunks(final Dmm dmmToCompare, final Dmm dmmCompareWith) {
        List<DiffPoint> diffPoints = findDiffPoints(dmmToCompare, dmmCompareWith);

        if (diffPoints.isEmpty()) {
            return Optional.empty();
        }

        List<MapRegion> chunks = new ArrayList<>();

        for (DiffPoint diffPoint : diffPoints) {
            final int x = diffPoint.getX();
            final int y = diffPoint.getY();

            if (chunks.isEmpty()) {
                chunks.add(MapRegion.singlePoint(x, y).addDiffPoint(diffPoint));
                continue;
            }

            boolean isExpanded = false;

            for (MapRegion chunk : chunks) {
                if (chunk.isInBounds(x, y)) {
                    chunk.expandBounds(x, y);
                    chunk.addDiffPoint(diffPoint);
                    isExpanded = true;
                    break;
                }
            }

            if (!isExpanded) {
                chunks.add(MapRegion.singlePoint(x, y).addDiffPoint(diffPoint));
            }
        }

        return Optional.of(chunks);
    }

    private static List<DiffPoint> findDiffPoints(final Dmm dmmToCompare, final Dmm dmmCompareWith) {
        List<DiffPoint> diffPoints = new ArrayList<>();

        for (int x = 1; x <= dmmToCompare.getMaxX(); x++) {
            for (int y = 1; y <= dmmToCompare.getMaxY(); y++) {
                if (!dmmCompareWith.hasTile(x, y)
                        || !dmmToCompare.getTile(x, y).hasSameObjects(dmmCompareWith.getTile(x, y))) {
                    diffPoints.add(DiffPoint.of(x, y));
                }
            }
        }

        return diffPoints;
    }

    private DmmComparator() {
    }

}
