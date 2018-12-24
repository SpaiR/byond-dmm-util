package io.github.spair.byond.dmm.drawer;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.parser.DmeParser;
import io.github.spair.byond.dmm.Dmm;
import io.github.spair.byond.dmm.MapRegion;
import io.github.spair.byond.dmm.ResourceUtil;
import io.github.spair.dmm.io.reader.DmmReader;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

public class DmmDrawerTest {

    private static final String DME_PATH = "render_proj/proj.dme";
    private static final String MAP_PATH = "render_proj/map.dmm";

    private static final String FULL_RENDER_IMG_PATH = "render_proj/result/img_full_render.png";
    private static final String PARTIAL_RENDER_IMG_PATH = "render_proj/result/img_partial_render.png";
    private static final String FULL_RENDER_REGION_IMG_PATH = "render_proj/result/img_full_render_region.png";
    private static final String PARTIAL_RENDER_REGION_IMG_PATH = "render_proj/result/img_partial_render_region.png";
    private static final String FULL_RENDER_WITH_TYPES_IMG_PATH = "render_proj/result/img_with_types_full_render.png";
    private static final String FULL_RENDER_REGION_WITH_TYPES_IMG_PATH = "render_proj/result/img_with_types_full_render_region.png";
    private static final String FULL_RENDER_WITH_EQ_TYPES_IMG_PATH = "render_proj/result/img_with_eq_types_full_render.png";
    private static final String FULL_RENDER_REGION_WITH_EQ_TYPES_IMG_PATH = "render_proj/result/img_with_eq_types_full_render_region.png";

    private static Dmm DMM;

    private static BufferedImage FULL_RENDER_IMG;
    private static BufferedImage PARTIAL_RENDER_IMG;
    private static BufferedImage FULL_RENDER_REGION_IMG;
    private static BufferedImage PARTIAL_RENDER_REGION_IMG;
    private static BufferedImage FULL_RENDER_WITH_TYPES_IMG;
    private static BufferedImage FULL_RENDER_REGION_WITH_TYPES_IMG;
    private static BufferedImage FULL_RENDER_WITH_EQ_TYPES_IMG;
    private static BufferedImage FULL_RENDER_REGION_WITH_EQ_TYPES_IMG;

    @BeforeClass
    public static void initialize() throws Exception {
        Dme dme = DmeParser.parse(ResourceUtil.readResourceFile(DME_PATH));
        DMM = new Dmm(DmmReader.readMap(ResourceUtil.readResourceFile(MAP_PATH)), dme);

        FULL_RENDER_IMG = ImageIO.read(ResourceUtil.readResourceFile(FULL_RENDER_IMG_PATH));
        PARTIAL_RENDER_IMG = ImageIO.read(ResourceUtil.readResourceFile(PARTIAL_RENDER_IMG_PATH));
        FULL_RENDER_REGION_IMG = ImageIO.read(ResourceUtil.readResourceFile(FULL_RENDER_REGION_IMG_PATH));
        PARTIAL_RENDER_REGION_IMG = ImageIO.read(ResourceUtil.readResourceFile(PARTIAL_RENDER_REGION_IMG_PATH));
        FULL_RENDER_WITH_TYPES_IMG = ImageIO.read(ResourceUtil.readResourceFile(FULL_RENDER_WITH_TYPES_IMG_PATH));
        FULL_RENDER_REGION_WITH_TYPES_IMG = ImageIO.read(ResourceUtil.readResourceFile(FULL_RENDER_REGION_WITH_TYPES_IMG_PATH));
        FULL_RENDER_WITH_EQ_TYPES_IMG = ImageIO.read(ResourceUtil.readResourceFile(FULL_RENDER_WITH_EQ_TYPES_IMG_PATH));
        FULL_RENDER_REGION_WITH_EQ_TYPES_IMG = ImageIO.read(ResourceUtil.readResourceFile(FULL_RENDER_REGION_WITH_EQ_TYPES_IMG_PATH));
    }

    @Test
    public void testDrawMapToImageWithFullRender() {
        assertRgb(FULL_RENDER_IMG, DmmDrawer.drawMap(DMM));
    }

    @Test
    public void testDrawMapToImageWithPartialRender() {
        assertRgb(PARTIAL_RENDER_IMG, DmmDrawer.drawMap(DMM, FilterMode.IGNORE, ByondTypes.AREA));
    }

    @Test
    public void testDrawMapToImageWithFullRenderAndMapRegion() {
        assertRgb(FULL_RENDER_REGION_IMG, DmmDrawer.drawMap(DMM, MapRegion.of(2, 5)));
    }

    @Test
    public void testDrawMapToImageWithPartialRenderAndMapRegion() {
        assertRgb(PARTIAL_RENDER_REGION_IMG, DmmDrawer.drawMap(DMM, MapRegion.of(2, 5), FilterMode.IGNORE, ByondTypes.AREA));
    }

    @Test
    public void testDrawMapToImageWithTypesWithFullRender() {
        assertRgb(FULL_RENDER_WITH_TYPES_IMG, DmmDrawer.drawMap(DMM, FilterMode.INCLUDE, ByondTypes.TURF));
    }

    @Test
    public void testDrawMapToImageWithTypesAndMapRegion() {
        assertRgb(FULL_RENDER_REGION_WITH_TYPES_IMG, DmmDrawer.drawMap(DMM, MapRegion.of(2, 5), FilterMode.INCLUDE, ByondTypes.TURF));
    }

    @Test
    public void testDrawMapToImageWithEqTypesWithFullRender() {
        assertRgb(FULL_RENDER_WITH_EQ_TYPES_IMG, DmmDrawer.drawMap(DMM, FilterMode.EQUAL, "/obj/item"));
    }

    @Test
    public void testDrawMapToImageWithEqTypesAndMapRegion() {
        assertRgb(FULL_RENDER_REGION_WITH_EQ_TYPES_IMG, DmmDrawer.drawMap(DMM, MapRegion.of(2, 5), FilterMode.EQUAL, "/obj/item"));
    }

    private void assertRgb(final BufferedImage expected, final BufferedImage actual) {
        for (int x = 0; x < expected.getWidth(); x++) {
            for (int y = 0; y < expected.getHeight(); y++) {
                assertEquals(expected.getRGB(x, y), actual.getRGB(x, y));
            }
        }
    }
}