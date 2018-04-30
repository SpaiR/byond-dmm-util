package io.github.spair.byond.dmm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ZLevelTest {

    @Test
    public void testSetTiles() {
        ZLevel zLevel = new ZLevel(1);
        zLevel.setTiles(new Tile[15][20]);

        assertEquals(20, zLevel.getMaxX());
        assertEquals(15, zLevel.getMaxY());
    }

    @Test
    public void testSetTile() {
        ZLevel zLevel = new ZLevel(1);
        zLevel.setTiles(new Tile[1][1]);
        zLevel.setTile(createTile(1, 1), 1, 1);

        assertEquals(createTile(1, 1), zLevel.getTile(1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTileWithWrongCoords() {
        ZLevel zLevel = new ZLevel(1);
        zLevel.getTile(-1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTileWithWithoutTiles() {
        ZLevel zLevel = new ZLevel(1);
        zLevel.getTile(10, 10);
    }

    @Test
    public void testGetTile() {
        ZLevel zLevel = new ZLevel(1);
        zLevel.setTiles(new Tile[][]{
                {
                    createTile(1, 1), createTile(1, 2)}, {
                    createTile(2, 1), createTile(2, 2)
                }
        });
    }

    private Tile createTile(final int x, final int y) {
        return new Tile(x, y, 1, null);
    }
}