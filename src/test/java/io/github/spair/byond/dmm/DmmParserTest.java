package io.github.spair.byond.dmm;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DmmParserTest {

    private DmmParser dmmParser;

    @Before
    public void setUp() {
        dmmParser = new DmmParser();
    }

    @Test
    public void testParseWithStandardMap() {
        Dmm dmm = dmmParser.parse(ResourceUtil.loadFile("standard_map.dmm"));

        assertNotNull(dmm.getZLevel(1));

        DmmItem sandItem = new DmmItem("/turf/simulated/floor/beach/sand");
        sandItem.setStringVar("tag", "icon-desert");
        sandItem.setStringVar("icon_state", "desert");

        DmmItem beachItem = new DmmItem("/area/holodeck/source_beach");

        TileInstance tileInstance = new TileInstance("a");
        tileInstance.addDmmItem(sandItem);
        tileInstance.addDmmItem(beachItem);

        Tile tile = new Tile(1, 1, 1, tileInstance);

        assertEquals(tile, dmm.getTile(1, 1, 1));

        sandItem.setStringVar("tag", "icon-beachcorner (NORTH)");
        sandItem.setStringVar("icon_state", "beachcorner");
        sandItem.setVar("dir", "1");

        tileInstance = new TileInstance("m");
        tileInstance.addDmmItem(sandItem);
        tileInstance.addDmmItem(beachItem);

        tile = new Tile(5, 6, 1, tileInstance);

        assertEquals(dmm.getTile(5, 6, 1), tile);
    }

    @Test
    public void testParseWithTGMMap() {
        Dmm dmm = dmmParser.parse(ResourceUtil.loadFile("tgm_map.dmm"));

        assertNotNull(dmm.getZLevel(1));

        DmmItem podItem = new DmmItem("/turf/closed/wall/mineral/titanium/survival/pod");
        DmmItem podArea = new DmmItem("/area/survivalpod");

        TileInstance tileInstance = new TileInstance("a");
        tileInstance.addDmmItem(podItem);
        tileInstance.addDmmItem(podArea);

        Tile tile = new Tile(1, 1, 1, tileInstance);

        assertEquals(tile, dmm.getTile(1, 1, 1));

        DmmItem signItem = new DmmItem("/obj/structure/sign/mining/survival");
        signItem.setVar("dir", "4");

        DmmItem mineralPodItem = new DmmItem("/turf/closed/wall/mineral/titanium/survival/pod");
        DmmItem survivalPodItem = new DmmItem("/area/survivalpod");

        tileInstance = new TileInstance("j");
        tileInstance.addDmmItem(signItem);
        tileInstance.addDmmItem(mineralPodItem);
        tileInstance.addDmmItem(survivalPodItem);

        tile = new Tile(3, 5, 1, tileInstance);

        assertEquals(tile, dmm.getTile(3 ,5 ,1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParserWithEmptyFile() {
        dmmParser.parse(ResourceUtil.loadFile("empty_file.txt"));
    }
}