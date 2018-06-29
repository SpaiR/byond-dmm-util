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
     *
     * @param dmmToCompare map {@link MapRegion} for which will be generated
     * @param dmmCompareWith map with which 'dmmToCompare' will be compared
     * @return {@link MapRegion} with area where changes found
     */
    public static Optional<MapRegion> compare(final Dmm dmmToCompare, final Dmm dmmCompareWith) {
        List<Point> pointsOfDiffs = new ArrayList<>();

        for (int x = 1; x <= dmmToCompare.getMaxX(); x++) {
            for (int y = 1; y <= dmmToCompare.getMaxY(); y++) {
                if (!dmmCompareWith.hasTile(x, y)
                        || !dmmToCompare.getTile(x, y).hasSameObjects(dmmCompareWith.getTile(x, y))) {
                    pointsOfDiffs.add(new Point(x, y));
                }
            }
        }

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

        final int maxX = Math.max(dmmToCompare.getMaxX(), upperX);
        final int maxY = Math.max(dmmToCompare.getMaxY(), upperY);

        lowerX = decreaseIfAble(lowerX);
        lowerY = decreaseIfAble(lowerY);
        upperX = increaseIfAble(upperX, maxX);
        upperY = increaseIfAble(upperY, maxY);

        return Optional.of(MapRegion.of(lowerX, lowerY, upperX, upperY));
    }

    private static int decreaseIfAble(final int value) {
        final int decreasedValue = value - 1;
        return decreasedValue > 0 ? decreasedValue : value;
    }

    private static int increaseIfAble(final int value, final int maxCap) {
        final int increasedValue = value + 1;
        return increasedValue <= maxCap ? increasedValue : value;
    }

    private DmmComparator() {
    }

    @AllArgsConstructor
    private static final class Point {
        private int x;
        private int y;
    }
}
