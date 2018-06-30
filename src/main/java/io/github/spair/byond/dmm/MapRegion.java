package io.github.spair.byond.dmm;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("WeakerAccess")
public final class MapRegion {

    private static final int EXPAND_DISTANCE = 10;

    private int lowerX;
    private int lowerY;

    private int upperX;
    private int upperY;

    public static MapRegion of(final int lowerPoint, final int upperPoint) {
        return of(lowerPoint, lowerPoint, upperPoint, upperPoint);
    }

    public static MapRegion of(final int lowerX, final int lowerY, final int upperX, final int upperY) {
        if (lowerX > upperX || lowerY > upperY) {
            throw new IllegalArgumentException("Lower point could not be the bigger than upper one."
                    + " Lower X: " + lowerX + ", lower Y: " + lowerY + ", upper X: " + upperX + ", upper Y: " + upperY);
        }
        if (lowerX <= 0 || lowerY <= 0) {
            throw new IllegalArgumentException("Lower point could not be lesser then 0."
                    + " Lower X: " + lowerX + ", lower Y: " + lowerY);
        }
        return new MapRegion(lowerX, lowerY, upperX, upperY);
    }

    public static MapRegion singlePoint(final int x, final int y) {
        return MapRegion.of(x, y, x, y);
    }

    public void expandBoundsByOne() {
        lowerX = Math.max(1, lowerX - 1);
        lowerY = Math.max(1, lowerY - 1);
        upperX += 1;
        upperY += 1;
    }

    public int getUpperXSafe(final int xCap) {
        return Math.min(upperX, xCap);
    }

    public int getUpperYSafe(final int yCap) {
        return Math.min(upperY, yCap);
    }

    boolean isInBounds(final int x, final int y) {
        return (isInBoundsOfLowerX(x) && (isInBoundsOfLowerY(y) || isInBoundsOfUpperY(y)))
                || (isInBoundsOfUpperX(x) && (isInBoundsOfLowerY(y) || isInBoundsOfUpperY(y)))
                || (isInBoundsOfLowerY(y) && (isInBoundsOfLowerX(x) || isInBoundsOfUpperX(x)))
                || (isInBoundsOfUpperY(y) && (isInBoundsOfLowerX(x) || isInBoundsOfUpperX(x)));
    }

    void expandBounds(final int x, final int y) {
        boolean xBoundsChanged = true;
        boolean yBoundsChanged = true;

        if (isInBoundsOfLowerX(x)) {
            lowerX = Math.min(x, lowerX);
        } else if (isInBoundsOfUpperX(x)) {
            upperX = Math.max(x, upperX);
        } else {
            xBoundsChanged = false;
        }

        if (isInBoundsOfLowerY(y)) {
            lowerY = Math.min(y, lowerY);
        } else if (isInBoundsOfUpperY(y)) {
            upperY = Math.max(y, upperY);
        } else {
            yBoundsChanged = false;
        }

        if (!xBoundsChanged && !yBoundsChanged) {
            throw new IllegalArgumentException("Args are not in bounds of region. X: " + x + " Y:" + y);
        }
    }

    private boolean isInBoundsOfLowerX(final int x) {
        return lowerX - x > 0 && lowerX - x <= EXPAND_DISTANCE;
    }

    private boolean isInBoundsOfLowerY(final int y) {
        return lowerY - y > 0 && lowerY - y <= EXPAND_DISTANCE;
    }

    private boolean isInBoundsOfUpperX(final int x) {
        return x - upperX <= EXPAND_DISTANCE;
    }

    private boolean isInBoundsOfUpperY(final int y) {
        return y - upperY <= EXPAND_DISTANCE;
    }
}
