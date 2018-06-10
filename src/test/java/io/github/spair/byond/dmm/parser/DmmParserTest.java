package io.github.spair.byond.dmm.parser;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;
import io.github.spair.byond.dmm.ResourceUtil;
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

        assertNotNull(dmm.getTileInstance("a"));
        assertNotNull(dmm.getTileInstance("b"));
        assertNotNull(dmm.getTileInstance("c"));

        TileInstance ti = dmm.getTileInstance("b");

        DmmItem turf = ti.getDmmItems().get(0);
        DmmItem item = ti.getDmmItems().get(1);

        assertEquals("/turf/simple", turf.getType());
        assertEquals("/obj/item", item.getType());

        Map<String, String> turfVars = turf.getVars();
        Map<String, String> itemVars = item.getVars();

        assertEquals("\"turf\"", turfVars.get("icon_state"));
        assertEquals("1", turfVars.get("dir"));
        assertEquals(2, turfVars.size());

        assertTrue(itemVars.isEmpty());

        ti = dmm.getTileInstance("c");

        turf = ti.getDmmItems().get(0);
        item = ti.getDmmItems().get(1);

        assertEquals("/turf/simple", turf.getType());
        assertEquals("/obj/item", item.getType());

        turfVars = turf.getVars();
        itemVars = item.getVars();

        assertEquals("newlist(/obj/item,/obj/item/weapon)", turfVars.get("contents"));
        assertEquals(1, turfVars.size());

        assertTrue(itemVars.isEmpty());
    }
}