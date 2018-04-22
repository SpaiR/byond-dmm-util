package io.github.spair.byond.dmm;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class ZLevel {

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
}
