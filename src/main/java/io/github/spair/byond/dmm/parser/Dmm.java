package io.github.spair.byond.dmm.parser;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

@Data
@SuppressWarnings("WeakerAccess")
public class Dmm implements Iterable<Tile> {

    public static final int DEFAULT_ICON_SIZE = 32;

    private Map<String, TileInstance> tileInstances;

    @Getter(AccessLevel.NONE)
    private Tile[][] tiles;

    private String dmeRootPath = "";

    private int iconSize;
    private int maxX;
    private int maxY;

    void setTiles(final Tile[][] tiles) {
        this.tiles = tiles;
        maxX = tiles[0].length;
        maxY = tiles.length;
    }

    void setTile(final Tile tile, final int x, final int y) {
        tiles[y - 1][x - 1] = tile;
    }

    public final Tile getTile(final int x, final int y) {
        if (x <= 0 || y <= 0) {
            throw new IllegalArgumentException("Coordinates should not be lesser than zero. X: " + x + ", Y: " + y);
        } else if (x > maxX || y > maxY) {
            throw new IllegalArgumentException(
                    "Nonexistent coordinates. X: " + x + ", Y: " + y + ". Max X: " + maxX + ", Max Y:" + maxY);
        }
        return tiles[y - 1][x - 1];
    }

    @Nonnull
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
