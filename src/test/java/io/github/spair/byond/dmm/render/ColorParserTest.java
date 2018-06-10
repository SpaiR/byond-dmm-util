package io.github.spair.byond.dmm.render;

import org.junit.Test;

import java.awt.*;
import java.util.Optional;

import static org.junit.Assert.*;

public class ColorParserTest {

    @Test
    public void testParseWithHex() {
        Optional<Color> color = ColorParser.parse("#FF0000");
        assertTrue(color.isPresent());
        assertEquals(Color.RED, color.get());
    }

    @Test
    public void testParseWithColorName() {
        Optional<Color> color = ColorParser.parse("red");
        assertTrue(color.isPresent());
        assertEquals(Color.RED, color.get());
    }

    @Test
    public void testParseWithNoColor() {
        Optional<Color> color = ColorParser.parse("qwerty");
        assertFalse(color.isPresent());
    }
}