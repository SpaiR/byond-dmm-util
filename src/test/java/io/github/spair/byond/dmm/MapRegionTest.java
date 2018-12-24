package io.github.spair.byond.dmm;

import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testOfWithException1() {
        MapRegion.of(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfWithException2() {
        MapRegion.of(2, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfWithException3() {
        MapRegion.of(0, 0, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfWithException4() {
        MapRegion.of(2, 1, 1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfWithException5() {
        MapRegion.of(1, 2, 2, 1);
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

    @Test
    public void testIsInBounds() {
        assertTrue(MapRegion.of(5, 10).isInBounds(6, 6));
        assertTrue(MapRegion.of(5, 10, 10, 15).isInBounds(6, 12));
        assertFalse(MapRegion.of(5, 10).isInBounds(4, 4));
    }
}
