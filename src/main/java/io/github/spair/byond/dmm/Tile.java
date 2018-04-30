package io.github.spair.byond.dmm;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@SuppressWarnings("WeakerAccess")
public class Tile {

    private int x, y, z;
    private TileInstance tileInstance;
    private List<TileItem> tileItems;

    public Tile(int x, int y, int z, TileInstance tileInstance) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tileInstance = tileInstance;
    }

    public boolean hasTileItems() {
        return Objects.nonNull(tileItems) && !tileItems.isEmpty();
    }

    public TileItem getTileItem(final String type) {
        for (TileItem tileItem : tileItems) {
            if (tileItem.getType().equals(type)) {
                return tileItem;
            }
        }
        return null;
    }
}
