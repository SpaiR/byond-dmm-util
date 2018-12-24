package io.github.spair.byond.dmm.drawer;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ColorExtractorTest {

    @Test
    public void testExtractWithHex() {
        assertEquals(Color.RED, ColorExtractor.extract("#FF0000"));
    }

    @Test
    public void testExtractWithColorName() {
        assertEquals(Color.RED, ColorExtractor.extract("red"));
    }

    @Test
    public void testExtractWithRgbName() {
        assertEquals(Color.BLACK, ColorExtractor.extract("rgb(0, 0, 0)"));
    }

    @Test
    public void testExtractWithNoColor() {
        assertNull(ColorExtractor.extract("qwerty"));
        assertNull(ColorExtractor.extract(""));
    }
}