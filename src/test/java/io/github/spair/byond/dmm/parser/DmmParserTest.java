package io.github.spair.byond.dmm.parser;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;
import io.github.spair.byond.dmm.Dmm;
import io.github.spair.byond.dmm.ResourceUtil;
import io.github.spair.dmm.io.TileContent;
import io.github.spair.dmm.io.TileObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DmmParserTest {

    private Dme dme;

    @Before
    public void setUp() {
        dme = new Dme();
        dme.setAbsoluteRootPath("/test/root/path");
        dme.addItem(new DmeItem(ByondTypes.WORLD, dme));
        dme.addItem(new DmeItem("/turf/simple", dme));
        dme.addItem(new DmeItem("/obj/item", dme));
    }

    @Test
    public void testParseWithStandardMap() {
        commonChecks(DmmParser.parse(ResourceUtil.readResourceFile("map_standard.dmm"), dme));
    }

    @Test
    public void testParseWithTGMMap() {
        commonChecks(DmmParser.parse(ResourceUtil.readResourceFile("map_tgm.dmm"), dme));
    }

    private void commonChecks(final Dmm dmm) {
        assertEquals("/test/root/path", dmm.getDmeRootPath());

        assertNotNull(dmm.getTileContentByKey("a"));
        assertNotNull(dmm.getTileContentByKey("b"));
        assertNotNull(dmm.getTileContentByKey("c"));

        TileContent ti = dmm.getTileContentByKey("b");

        TileObject item = ti.getTileObjects().get(0);
        TileObject turf = ti.getTileObjects().get(1);

        assertEquals("/obj/item", item.getType());
        assertEquals("/turf/simple", turf.getType());

        Map<String, String> turfVars = turf.getVars();
        Map<String, String> itemVars = item.getVars();

        assertEquals("\"turf\"", turfVars.get("icon_state"));
        assertEquals("1", turfVars.get("dir"));
        assertEquals(2, turfVars.size());

        assertTrue(itemVars.isEmpty());

        ti = dmm.getTileContentByKey("c");

        item = ti.getTileObjects().get(0);
        turf = ti.getTileObjects().get(1);

        assertEquals("/obj/item", item.getType());
        assertEquals("/turf/simple", turf.getType());

        turfVars = turf.getVars();
        itemVars = item.getVars();

        assertEquals("newlist(/obj/item,/obj/item/weapon)", turfVars.get("contents"));
        assertEquals(1, turfVars.size());

        assertTrue(itemVars.isEmpty());
    }
}