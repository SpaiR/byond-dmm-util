package io.github.spair.byond.dmm.parser;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = "tileInstance")
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

    @Nonnull
    @Override
    public Iterator<TileItem> iterator() {
        return tileItems.iterator();
    }
}
