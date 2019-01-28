package io.github.spair.byond.dmm;

import lombok.Data;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Data
public class Tile implements Iterable<TileItem> {

    private final int x, y;
    private final List<TileItem> tileItems;

    Tile(final int x, final int y, final List<TileItem> tileItems) {
        this.x = x;
        this.y = y;
        this.tileItems = Collections.unmodifiableList(tileItems);
    }

    public boolean hasSameObjects(final Tile tile) {
        return Objects.equals(tileItems, tile.tileItems);
    }

    @Override
    public Iterator<TileItem> iterator() {
        return tileItems.iterator();
    }
}
