package io.github.spair.byond.dmm.differ;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;
import io.github.spair.byond.dmm.Dmm;
import io.github.spair.byond.dmm.MapRegion;
import io.github.spair.byond.dmm.ResourceUtil;
import io.github.spair.dmm.io.reader.DmmReader;
import lombok.val;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class DmmDifferTest {

    private Dmm orig;
    private Dmm mod;

    @Before
    public void setUp() {
        Dme dme = new Dme();
        dme.setAbsoluteRootPath("");
        dme.addItem(new DmeItem(ByondTypes.WORLD, dme));
        dme.addItem(new DmeItem("/turf/simple", dme));
        dme.addItem(new DmeItem("/obj/item", dme));
        orig = new Dmm(DmmReader.readMap(ResourceUtil.readResourceFile("dmm_diff_orig.dmm")), dme);
        mod = new Dmm(DmmReader.readMap(ResourceUtil.readResourceFile("dmm_diff_mod.dmm")), dme);
    }

    @Test
    public void testFindDiffRegion() {
        val diffRegion = DmmDiffer.findDiffRegion(orig, mod);
        assertTrue(diffRegion.isPresent());
        assertEquals(MapRegion.of(3, 2, 3, 2), diffRegion.get());
    }

    @Test
    public void testFindDiffPoints() {
        val diffPoints = DmmDiffer.findDiffPoints(orig, mod);
        assertFalse(diffPoints.isEmpty());
        assertEquals(Collections.singletonList(DiffPoint.of(3, 2)), diffPoints);
    }
}