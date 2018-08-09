package io.github.spair.byond.dmm;

import lombok.Data;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Data
@SuppressWarnings("WeakerAccess")
public class Tile implements Iterable<TileItem> {

    private int x;
    private int y;
    private int z;

    private TileInstance tileInstance;
    private List<TileItem> tileItems;

    public Tile(final int x, final int y, final int z, final TileInstance tileInstance) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tileInstance = tileInstance;
    }

    public boolean hasSameObjects(final Tile tile) {
        return Objects.equals(tileInstance.getDmmItems(), tile.tileInstance.getDmmItems());
    }

    @Nonnull
    @Override
    public Iterator<TileItem> iterator() {
        return tileItems.iterator();
    }
}
