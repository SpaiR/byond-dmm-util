package io.github.spair.byond.dmm;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class MapRegion {

    private final int lowerX;
    private final int lowerY;

    private final int upperX;
    private final int upperY;

    public static MapRegion of(final int lowerPoint, final int upperPoint) {
        return of(lowerPoint, lowerPoint, upperPoint, upperPoint);
    }

    public static MapRegion of(final int lowerX, final int lowerY, final int upperX, final int upperY) {
        if (lowerX > upperX)
            throw new IllegalArgumentException(String.format("Lower X (%d) can't be higher than upper X (%d)", lowerX, upperX));
        if (lowerY > upperY)
            throw new IllegalArgumentException(String.format("Lower Y (%d) can't be higher than upper Y (%d)", lowerY, upperY));
        if (lowerX <= 0)
            throw new IllegalArgumentException(String.format("Lower X (%d) can't be lesser then 0", lowerX));
        if (lowerY <= 0)
            throw new IllegalArgumentException(String.format("Lower Y (%d) can't be lesser then 0", lowerY));
        return new MapRegion(lowerX, lowerY, upperX, upperY);
    }

    public static MapRegion singlePoint(final int x, final int y) {
        return of(x, y, x, y);
    }

    public boolean isSinglePoint() {
        return lowerX == upperX && lowerY == upperY;
    }

    public boolean isInBounds(final int x, final int y) {
        return lowerX <= x && upperX >= x && lowerY <= y && upperY >= y;
    }

    public int getWidth() {
        return Math.max(upperX - lowerX, 1);
    }

    public int getHeight() {
        return Math.max(upperY - lowerY, 1);
    }
}
