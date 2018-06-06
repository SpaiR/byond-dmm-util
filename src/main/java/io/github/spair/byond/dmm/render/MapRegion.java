package io.github.spair.byond.dmm.render;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class MapRegion {

    private int lowerX;
    private int lowerY;

    private int upperX;
    private int upperY;

    public static MapRegion of(final int lowerPoint, final int upperPoint) {
        return of(lowerPoint, lowerPoint, upperPoint, upperPoint);
    }

    public static MapRegion of(final int lowerX, final int lowerY, final int upperX, final int upperY) {
        if (lowerX >= upperX || lowerY >= upperY) {
            throw new IllegalArgumentException("Lower point could not be the same or bigger than upper one."
                    + " Lower X: " + lowerX + ", lower Y: " + lowerY + ", upper X: " + upperX + ", upper Y: " + upperY);
        }
        return new MapRegion(lowerX, lowerY, upperX, upperY);
    }
}
