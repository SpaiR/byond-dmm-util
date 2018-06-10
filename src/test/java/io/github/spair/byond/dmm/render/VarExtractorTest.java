package io.github.spair.byond.dmm.render;

import io.github.spair.byond.dmi.SpriteDir;
import io.github.spair.byond.dmm.parser.TileItemFactory;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VarExtractorTest {

    @Test
    public void testPlaneWithoutExpression() {
        assertEquals(5.5, VarExtractor.plane(
                TileItemFactory.create(createVariable("plane", "5.5"))
        ), 0);
        assertEquals(0.0, VarExtractor.plane(
                TileItemFactory.create(createVariable("notPlane", ""))
        ), 0);
    }

    @Test
    public void testPlaneWithExpression() {
        assertEquals(4.2, VarExtractor.plane(
                TileItemFactory.create(createVariable("plane", "4 + 0.2"))
        ), 0);
    }

    @Test
    public void testLayerWithoutExpression() {
        assertEquals(6.0, VarExtractor.layer(
                TileItemFactory.create(createVariable("layer", "6"))
        ), 0);
        assertEquals(0.0, VarExtractor.layer(
                TileItemFactory.create(createVariable("notLayer", ""))
        ), 0);
    }

    @Test
    public void testLayerWithExpression() {
        assertEquals(7.3, VarExtractor.layer(
                TileItemFactory.create(createVariable("layer", "4 + 3.3"))
        ), 0);
    }

    @Test
    public void testIcon() {
        assertEquals("iconValue", VarExtractor.icon(
                TileItemFactory.create(createVariable("icon", "'iconValue'"))
        ));
        assertTrue(VarExtractor.icon(
                TileItemFactory.create(createVariable("notIcon", ""))
        ).isEmpty());
    }

    @Test
    public void testIconState() {
        assertEquals("iconStateValue", VarExtractor.iconState(
                TileItemFactory.create(createVariable("icon_state", "\"iconStateValue\""))
        ));
        assertTrue(VarExtractor.icon(
                TileItemFactory.create(createVariable("notIcon", ""))
        ).isEmpty());
    }

    @Test
    public void testDir() {
        assertEquals(SpriteDir.EAST, VarExtractor.dir(
                TileItemFactory.create(createVariable("dir", "4"))
        ));
        assertEquals(SpriteDir.SOUTH, VarExtractor.dir(
                TileItemFactory.create(createVariable("notDir", ""))
        ));
    }

    @Test
    public void testPixelX() {
        assertEquals(12, VarExtractor.pixelX(
                TileItemFactory.create(createVariable("pixel_x", "12"))
        ));
        assertEquals(0, VarExtractor.pixelX(
                TileItemFactory.create(createVariable("notPixel_x", ""))
        ));
    }

    @Test
    public void testPixelY() {
        assertEquals(23, VarExtractor.pixelY(
                TileItemFactory.create(createVariable("pixel_y", "23"))
        ));
        assertEquals(0, VarExtractor.pixelY(
                TileItemFactory.create(createVariable("notPixel_y", ""))
        ));
    }

    @Test
    public void testAlpha() {
        assertEquals(128, VarExtractor.alpha(
                TileItemFactory.create(createVariable("alpha", "128"))
        ));
        assertEquals(255, VarExtractor.alpha(
                TileItemFactory.create(createVariable("notAlpha", ""))
        ));
    }

    @Test
    public void testColor() {
        assertEquals("#000", VarExtractor.color(
                TileItemFactory.create(createVariable("color", "\"#000\""))
        ));
        assertEquals("#ffffff", VarExtractor.color(
                TileItemFactory.create(createVariable("color", "rgb(255, 255, 255)"))
        ));
    }

    private Map<String, String> createVariable(final String varName, final String varValue) {
        return new HashMap<String, String>() {
            {
                put(varName, varValue);
            }
        };
    }
}