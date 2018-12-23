package io.github.spair.byond.dmm;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;
import io.github.spair.dmm.io.reader.DmmReader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DmmTest {

    private Dmm dmm;

    @Before
    public void setUp() {
        Dme dme = new Dme();
        dme.setAbsoluteRootPath("/test/root/path");
        dme.addItem(new DmeItem(ByondTypes.WORLD, dme));
        dme.addItem(new DmeItem("/turf/simple", dme));
        dme.addItem(new DmeItem("/obj/item", dme));
        dmm = new Dmm(DmmReader.readMap(ResourceUtil.readResourceFile("map_tgm.dmm")), dme);
    }

    @Test
    public void testSetTiles() {
        assertEquals(3, dmm.getMaxY());
        assertEquals(3, dmm.getMaxX());
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
        assertTrue(dmm.hasTile(2, 2));
        assertFalse(dmm.hasTile(100, 100));
    }
}