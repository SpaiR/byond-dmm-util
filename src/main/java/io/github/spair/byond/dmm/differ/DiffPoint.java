package io.github.spair.byond.dmm.differ;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class DiffPoint {

    private final int x;
    private final int y;

    public static DiffPoint of(final int x, final int y) {
        return new DiffPoint(x, y);
    }
}
