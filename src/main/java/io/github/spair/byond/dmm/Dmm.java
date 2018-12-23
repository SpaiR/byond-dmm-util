package io.github.spair.byond.dmm;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.ByondVars;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dmi.Dmi;
import io.github.spair.dmm.io.DmmData;
import io.github.spair.dmm.io.TileContent;
import io.github.spair.dmm.io.TileLocation;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.val;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Abstraction over {@link DmmData} and {@link Dme} classes. Helps to get items and there vars from one point.
 */
@Data
public class Dmm implements Iterable<Tile> {

    private final Map<String, TileContent> tileContents;

    @Getter(AccessLevel.NONE)
    private final Tile[][] tiles;

    private final String dmeRootPath;
    private final int iconSize;
    private final int maxX, maxY;

    public Dmm(final DmmData dmmData, final Dme dme) {
        dmeRootPath = dme.getAbsoluteRootPath();
        iconSize = dme.getItem(ByondTypes.WORLD).getVarInt(ByondVars.ICON_SIZE).orElse(Dmi.DEFAULT_SPRITE_SIZE);
        maxX = dmmData.getMaxX();
        maxY = dmmData.getMaxY();
        tileContents = Collections.unmodifiableMap(dmmData.getTileContentsByKey());

        tiles = new Tile[maxY][maxX];

        for (int x = 1; x <= maxX; x++) {
            for (int y = maxY; y >= 1; y--) {
                val tileContent = dmmData.getTileContentByLocation(TileLocation.of(x, y));
                val tileItems = new ArrayList<TileItem>();

                for (val tileObject : tileContent) {
                    tileItems.add(new TileItem(x, y, dme.getItem(tileObject.getType()), tileObject.getVars()));
                }

                tiles[y - 1][x - 1] = new Tile(x, y, tileContent, tileItems);
            }
        }
    }

    public Tile getTile(final int x, final int y) {
        if (hasTile(x, y)) {
            return tiles[y - 1][x - 1];
        }
        throw new IllegalArgumentException("Nonexistent coordinates. X: " + x + ", Y: " + y);
    }

    public TileContent getTileContentByKey(final String key) {
        return tileContents.get(key);
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
