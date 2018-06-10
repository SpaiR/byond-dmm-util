package io.github.spair.byond.dmm.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DmmTest {

    private Dmm dmm;

    @Before
    public void setUp() {
        Tile[][] tiles = new Tile[10][15];

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 15; x++) {
                tiles[y][x] = new Tile(x + 1, y + 1, 1, null);
            }
        }

        dmm = new Dmm();
        dmm.setTiles(tiles);
    }

    @Test
    public void testSetTiles() {
        assertEquals(10, dmm.getMaxY());
        assertEquals(15, dmm.getMaxX());
    }

    @Test
    public void testGetTile() {
        assertNotNull(dmm.getTile(2, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTileWhenTileNotExist() {
        dmm.getTile(100, 100);
    }

    @Test
    public void testHasTile() {
        assertTrue(dmm.hasTile(5, 5));
        assertFalse(dmm.hasTile(100, 100));
    }
}