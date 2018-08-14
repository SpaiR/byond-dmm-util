package io.github.spair.byond.dmm;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

@Data
@SuppressWarnings("WeakerAccess")
public class Dmm implements Iterable<Tile> {

    public static final int DEFAULT_ICON_SIZE = 32;
    public static final Dmm EMPTY_MAP = new Dmm();

    private Map<String, TileInstance> tileInstances;

    @Getter(AccessLevel.NONE)
    private Tile[][] tiles;

    private String dmeRootPath = "";

    private int iconSize;
    private int maxX;
    private int maxY;

    public void setTiles(final Tile[][] tiles) {
        this.tiles = tiles;
        maxX = tiles[0].length;
        maxY = tiles.length;
    }

    public Tile getTile(final int x, final int y) {
        if (hasTile(x, y)) {
            return tiles[y - 1][x - 1];
        }
        throw new IllegalArgumentException("Nonexistent coordinates. X: " + x + ", Y: " + y);
    }

    public TileInstance getTileInstance(final String key) {
        return tileInstances.get(key);
    }

    public boolean hasTile(final int x, final int y) {
        return x > 0 && x <= maxX && y > 0 && y <= maxY;
    }

    @Override
    public Iterator<Tile> iterator() {
        return new DmmIterator();
    }

    private class DmmIterator implements Iterator<Tile> {

        private int x = 0;
        private int y = 1;

        @Override
        public boolean hasNext() {
            return (x + 1) <= maxX || (y + 1) <= maxY;
        }

        @Override
        public Tile next() {
            boolean isNewRow = false;

            if (x + 1 <= maxX) {
                x++;
            } else {
                x = 1;
                isNewRow = true;
            }

            if (isNewRow) {
                y++;
            }

            if (x > maxX || y > maxY) {
                throw new NoSuchElementException();
            }

            return tiles[y - 1][x - 1];
        }
    }
}
