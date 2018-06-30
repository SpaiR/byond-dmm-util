package io.github.spair.byond.dmm;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;
import io.github.spair.byond.dmm.parser.Dmm;
import io.github.spair.byond.dmm.parser.DmmParser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DmmComparatorTest {

    private Dme dme;

    @Before
    public void setUp() {
        dme = new Dme();
        dme.setAbsoluteRootPath("");
        dme.addItem(new DmeItem(ByondTypes.WORLD, dme));
        dme.addItem(new DmeItem("/turf/simple", dme));
        dme.addItem(new DmeItem("/obj/item", dme));
    }

    @Test
    public void testCompare() {
        Dmm orig = DmmParser.parse(ResourceUtil.readResourceFile("dmm_diff_orig.dmm"), dme);
        Dmm mod = DmmParser.parse(ResourceUtil.readResourceFile("dmm_diff_mod.dmm"), dme);

        Optional<MapRegion> diffRegion = DmmComparator.compare(orig, mod);

        assertTrue(diffRegion.isPresent());
        assertEquals(MapRegion.of(3, 2, 3, 2), diffRegion.get());
    }

    @Test
    public void testCompareByChunks() {
        Dmm orig = DmmParser.parse(ResourceUtil.readResourceFile("dmm_diff_big_orig.dmm"), dme);
        Dmm mod = DmmParser.parse(ResourceUtil.readResourceFile("dmm_diff_big_mod.dmm"), dme);

        Optional<List<MapRegion>> diffRegions = DmmComparator.compareByChunks(orig, mod);

        assertTrue(diffRegions.isPresent());

        List<MapRegion> chunks = diffRegions.get();

        assertEquals(MapRegion.of(1, 2, 4, 8), chunks.get(0));
        assertEquals(MapRegion.of(4, 23, 4, 23), chunks.get(1));
        assertEquals(MapRegion.of(19, 2, 25, 9), chunks.get(2));
        assertEquals(MapRegion.of(25, 26, 26, 27), chunks.get(3));
    }
}