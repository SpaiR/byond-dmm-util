package io.github.spair.byond.dmm.comparator;

import io.github.spair.byond.dmm.MapRegion;
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

        assertEquals(10, mapRegion.getWidth());
        assertEquals(10, mapRegion.getHeight());
    }

    @Test
    public void testOfWithFullArgs() {
        MapRegion mapRegion = MapRegion.of(10, 15, 15, 25);

        assertEquals(10, mapRegion.getLowerX());
        assertEquals(15, mapRegion.getLowerY());
        assertEquals(15, mapRegion.getUpperX());
        assertEquals(25, mapRegion.getUpperY());

        assertEquals(5, mapRegion.getWidth());
        assertEquals(10, mapRegion.getHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfWithException() {
        MapRegion.of(0, 0);
    }

    @Test
    public void testSinglePoint() {
        MapRegion mapRegion = MapRegion.singlePoint(4, 4);

        assertEquals(4, mapRegion.getLowerX());
        assertEquals(4, mapRegion.getLowerY());
        assertEquals(4, mapRegion.getUpperX());
        assertEquals(4, mapRegion.getUpperY());

        assertEquals(1, mapRegion.getWidth());
        assertEquals(1, mapRegion.getHeight());
    }
}