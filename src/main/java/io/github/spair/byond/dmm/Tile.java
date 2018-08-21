package io.github.spair.byond.dmm;

import io.github.spair.dmm.io.TileContent;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Data
@SuppressWarnings("WeakerAccess")
public class Tile implements Iterable<TileItem> {

    private int x;
    private int y;

    private TileContent tileContent;
    private List<TileItem> tileItems = new ArrayList<>();

    public Tile(final int x, final int y, final TileContent tileContent) {
        this.x = x;
        this.y = y;
        this.tileContent = tileContent;
    }

    public boolean hasSameObjects(final Tile tile) {
        return Objects.equals(tileContent.getTileObjects(), tile.tileContent.getTileObjects());
    }

    public void addTileItem(final TileItem tileItem) {
        tileItems.add(tileItem);
    }

    @Override
    public Iterator<TileItem> iterator() {
        return tileItems.iterator();
    }
}
