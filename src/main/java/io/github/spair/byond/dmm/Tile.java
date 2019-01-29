package io.github.spair.byond.dmm;

import io.github.spair.byond.ByondTypes;
import lombok.Data;
import lombok.val;
import lombok.var;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Data
public class Tile implements Iterable<TileItem> {

    private final int x, y;
    private final String area;
    private final List<TileItem> tileItems;

    Tile(final int x, final int y, final List<TileItem> tileItems) {
        this.x = x;
        this.y = y;
        this.tileItems = Collections.unmodifiableList(tileItems);

        var area = ByondTypes.AREA;
        for (val tileItem : tileItems) {
            if (tileItem.isType(ByondTypes.AREA)) {
                area = tileItem.getType();
                break;
            }
        }
        this.area = area;
    }

    public boolean hasSameObjects(final Tile tile) {
        return Objects.equals(tileItems, tile.tileItems);
    }

    @Override
    public Iterator<TileItem> iterator() {
        return tileItems.iterator();
    }
}
