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
        if (lowerX <= 0 || lowerY <= 0) {
            throw new IllegalArgumentException("Lower point could not be lesser then 0."
                    + " Lower X: " + lowerX + ", lower Y: " + lowerY);
        }
        return new MapRegion(lowerX, lowerY, upperX, upperY);
    }
}
