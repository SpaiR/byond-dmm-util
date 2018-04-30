package io.github.spair.byond.dmm;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.NoSuchElementException;

@Data
@Setter(AccessLevel.NONE)
@SuppressWarnings("WeakerAccess")
public class ZLevel implements Iterable<Tile> {

    private int level;
    private int maxX;
    private int maxY;

    @Getter(AccessLevel.NONE)
    private Tile[][] tiles;

    public ZLevel(final int level) {
        this.level = level;
    }

    public void setTiles(final Tile[][] tiles) {
        this.tiles = tiles;
        maxX = tiles[0].length;
        maxY = tiles.length;
    }

    public void setTile(final Tile tile, final int x, final int y) {
        checkCoordinates(x, y);
        tiles[y - 1][x - 1] = tile;
    }

    public Tile getTile(final int x, final int y) {
        checkCoordinates(x, y);
        return tiles[y - 1][x - 1];
    }

    private void checkCoordinates(final int x, final int y) {
        if (x <= 0 || y <= 0) {
            throw new IllegalArgumentException("Coordinates should not be lesser than zero. X: " + x + ", Y: " + y);
        } else if (x > maxX || y > maxY) {
            throw new IllegalArgumentException(
                    "Nonexistent coordinates. X: " + x + ", Y: " + y + ". Max X: " + maxX + ", Max Y:" + maxY);
        }
    }

    @Nonnull
    @Override
    public Iterator<Tile> iterator() {
        return new ZLevelIterator();
    }

    private class ZLevelIterator implements Iterator<Tile> {

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

            return getTile(x, y);
        }
    }
}
