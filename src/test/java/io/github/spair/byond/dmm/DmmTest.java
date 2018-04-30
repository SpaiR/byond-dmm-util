package io.github.spair.byond.dmm;

import io.github.spair.byond.dme.Dme;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.*;

public class DmmTest {

    @Test
    public void testGetZLevel() {
        Dmm dmm = new Dmm();
        dmm.setZLevels(getZLevels());

        assertNotNull(dmm.getZLevel(1));
        assertNull(dmm.getZLevel(0));
    }

    @Test
    public void testGetTile() {
        Dmm dmm = new Dmm();
        dmm.setZLevels(getZLevels());

        assertNotNull(dmm.getTile(2, 2, 1));
        assertNull(dmm.getTile(2, 2, 0));
    }

    @Test
    public void testListAvailableZLevels() {
        Dmm dmm = new Dmm();
        dmm.setZLevels(getZLevels());

        assertEquals(1, dmm.listAvailableZLevels().size());
        assertEquals(new HashSet<Integer>() {{ add(1); }}, dmm.listAvailableZLevels());
    }

    @Test
    public void listAvailableZLevels() {
    }

    @Test
    public void testInjectDme() {
        Dmm dmm = new Dmm();
        assertFalse(dmm.isDmeInjected());
        dmm.injectDme(new Dme());
        assertTrue(dmm.isDmeInjected());
    }

    private Map<Integer, ZLevel> getZLevels() {
        ZLevel zLevel = new ZLevel(1);
        zLevel.setTiles(new Tile[10][10]);
        zLevel.setTile(new Tile(2, 2, 1, null), 2, 2);

        return Collections.singletonMap(1, zLevel);
    }
}