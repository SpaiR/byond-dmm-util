package io.github.spair.byond.dmm;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;
import io.github.spair.byond.dmm.parser.Dmm;
import io.github.spair.byond.dmm.parser.DmmParser;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DmmComparatorTest {

    @Test
    public void testCompare() {
        Dme dme = new Dme();
        dme.addItem(new DmeItem(ByondTypes.WORLD, dme));
        dme.addItem(new DmeItem("/turf/simple", dme));
        dme.addItem(new DmeItem("/obj/item", dme));

        Dmm orig = DmmParser.parse(ResourceUtil.readResourceFile("dmm_diff_orig.dmm"), dme);
        Dmm mod = DmmParser.parse(ResourceUtil.readResourceFile("dmm_diff_mod.dmm"), dme);

        Optional<MapRegion> diffRegion = DmmComparator.compare(orig, mod);

        assertTrue(diffRegion.isPresent());
        assertEquals(MapRegion.of(2, 1, 4, 3), diffRegion.get());
    }
}