package io.github.spair.byond.dmm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapRegionTest {

    @Test
    public void testOfWithPartlyArgs() {
        MapRegion mapRegion = MapRegion.of(10, 20);

        assertEquals(10, mapRegion.getLowerX());
        assertEquals(10, mapRegion.getLowerY());
        assertEquals(20, mapRegion.getUpperX());
        assertEquals(20, mapRegion.getUpperY());
    }

    @Test
    public void testOfWithFullArgs() {
        MapRegion mapRegion = MapRegion.of(10, 15, 20, 25);

        assertEquals(10, mapRegion.getLowerX());
        assertEquals(15, mapRegion.getLowerY());
        assertEquals(20, mapRegion.getUpperX());
        assertEquals(25, mapRegion.getUpperY());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfWithException() {
        MapRegion.of(10, 10);
    }
}