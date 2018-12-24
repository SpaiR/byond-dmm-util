package io.github.spair.byond.dmm.differ;

import io.github.spair.byond.dmm.Dmm;
import io.github.spair.byond.dmm.MapRegion;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("WeakerAccess")
public final class DmmDiffer {

    public static Optional<MapRegion> findDiffRegion(final Dmm dmmToCompare, final Dmm dmmCompareWith) {
        val diffPoints = findDiffPoints(dmmToCompare, dmmCompareWith);

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

        return Optional.of(MapRegion.of(lowerX, lowerY, upperX, upperY));
    }

    public static List<DiffPoint> findDiffPoints(final Dmm dmmToCompare, final Dmm dmmCompareWith) {
        List<DiffPoint> diffPoints = new ArrayList<>();

        for (int x = 1; x <= dmmToCompare.getMaxX(); x++) {
            for (int y = 1; y <= dmmToCompare.getMaxY(); y++) {
                if (!dmmCompareWith.hasTile(x, y) || !dmmToCompare.getTile(x, y).hasSameObjects(dmmCompareWith.getTile(x, y))) {
                    diffPoints.add(DiffPoint.of(x, y));
                }
            }
        }

        return diffPoints;
    }

    private DmmDiffer() {
    }
}
