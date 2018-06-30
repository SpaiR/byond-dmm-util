package io.github.spair.byond.dmm;

import io.github.spair.byond.dmm.parser.Dmm;
import lombok.AllArgsConstructor;

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
        List<Point> pointsOfDiffs = findPointsOfDifference(dmmToCompare, dmmCompareWith);

        if (pointsOfDiffs.isEmpty()) {
            return Optional.empty();
        }

        int lowerX = Integer.MAX_VALUE;
        int lowerY = Integer.MAX_VALUE;
        int upperX = Integer.MIN_VALUE;
        int upperY = Integer.MIN_VALUE;

        for (Point point : pointsOfDiffs) {
            lowerX = Math.min(lowerX, point.x);
            lowerY = Math.min(lowerY, point.y);
            upperX = Math.max(upperX, point.x);
            upperY = Math.max(upperY, point.y);
        }

        return Optional.of(MapRegion.of(lowerX, lowerY, upperX, upperY));
    }

    /**
     * Method to compare two {@link Dmm}'s and get list {@link MapRegion} with areas of changes.
     * Since maps could have different sizes,
     * generated list shows difference only in context of first argument.
     * <p>Unlike {@link #compare(Dmm, Dmm)} returns list of regions,
     * which could show map differences more accurate.
     *
     * @param dmmToCompare   map {@link MapRegion} for which will be generated
     * @param dmmCompareWith map with which 'dmmToCompare' will be compared
     * @return list of {@link MapRegion} with areas of changes
     */
    public static Optional<List<MapRegion>> compareByChunks(final Dmm dmmToCompare, final Dmm dmmCompareWith) {
        List<Point> pointsOfDiffs = findPointsOfDifference(dmmToCompare, dmmCompareWith);

        if (pointsOfDiffs.isEmpty()) {
            return Optional.empty();
        }

        List<MapRegion> chunks = new ArrayList<>();

        for (Point point : pointsOfDiffs) {
            final int x = point.x;
            final int y = point.y;

            if (chunks.isEmpty()) {
                chunks.add(MapRegion.singlePoint(x, y));
                continue;
            }

            boolean isExpanded = false;

            for (MapRegion chunk : chunks) {
                if (chunk.isInBounds(x, y)) {
                    chunk.expandBounds(x, y);
                    isExpanded = true;
                    break;
                }
            }

            if (!isExpanded) {
                chunks.add(MapRegion.singlePoint(x, y));
            }
        }

        return Optional.of(chunks);
    }

    private static List<Point> findPointsOfDifference(final Dmm dmmToCompare, final Dmm dmmCompareWith) {
        List<Point> pointsOfDiffs = new ArrayList<>();

        for (int x = 1; x <= dmmToCompare.getMaxX(); x++) {
            for (int y = 1; y <= dmmToCompare.getMaxY(); y++) {
                if (!dmmCompareWith.hasTile(x, y)
                        || !dmmToCompare.getTile(x, y).hasSameObjects(dmmCompareWith.getTile(x, y))) {
                    pointsOfDiffs.add(new Point(x, y));
                }
            }
        }

        return pointsOfDiffs;
    }

    private DmmComparator() {
    }

    @AllArgsConstructor
    private static final class Point {
        private int x;
        private int y;
    }
}
