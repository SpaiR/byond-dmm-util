package io.github.spair.byond.dmm.render;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeParser;
import io.github.spair.byond.dmm.MapRegion;
import io.github.spair.byond.dmm.ResourceUtil;
import io.github.spair.byond.dmm.parser.Dmm;
import io.github.spair.byond.dmm.parser.DmmParser;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

public class DmmRenderTest {

    private static final String DME_PATH = "render_proj/proj.dme";
    private static final String MAP_PATH = "render_proj/map.dmm";

    private static final String FULL_RENDER_RESULT_PATH = "render_proj/result/img_full_render.png";
    private static final String PARTIAL_RENDER_RESULT_PATH = "render_proj/result/img_partial_render.png";
    private static final String FULL_RENDER_REGION_RESULT_PATH = "render_proj/result/img_full_render_region.png";
    private static final String PARTIAL_RENDER_REGION_RESULT_PATH = "render_proj/result/img_partial_render_region.png";

    private static Dmm DMM;

    private static BufferedImage FULL_RENDER_IMG;
    private static BufferedImage PARTIAL_RENDER_IMG;
    private static BufferedImage FULL_RENDER_REGION_IMG;
    private static BufferedImage PARTIAL_RENDER_REGION_IMG;

    @BeforeClass
    public static void initialize() throws Exception {
        Dme dme = DmeParser.parse(ResourceUtil.readResourceFile(DME_PATH));
        DMM = DmmParser.parse(ResourceUtil.readResourceFile(MAP_PATH), dme);

        FULL_RENDER_IMG = ImageIO.read(ResourceUtil.readResourceFile(FULL_RENDER_RESULT_PATH));
        PARTIAL_RENDER_IMG = ImageIO.read(ResourceUtil.readResourceFile(PARTIAL_RENDER_RESULT_PATH));
        FULL_RENDER_REGION_IMG = ImageIO.read(ResourceUtil.readResourceFile(FULL_RENDER_REGION_RESULT_PATH));
        PARTIAL_RENDER_REGION_IMG = ImageIO.read(ResourceUtil.readResourceFile(PARTIAL_RENDER_REGION_RESULT_PATH));
    }

    @Test
    public void testRenderToImageWithFullRender() {
        assertRgb(FULL_RENDER_IMG, DmmRender.renderToImage(DMM));
    }

    @Test
    public void testRenderToImageWithPartialRender() {
        assertRgb(PARTIAL_RENDER_IMG, DmmRender.renderToImage(DMM, ByondTypes.AREA));
    }

    @Test
    public void testRenderToImageWithFullRenderAndMapRegion() {
        assertRgb(FULL_RENDER_REGION_IMG, DmmRender.renderToImage(DMM, MapRegion.of(2, 5)));
    }

    @Test
    public void testRenderToImageWithPartialRenderAndMapRegion() {
        assertRgb(PARTIAL_RENDER_REGION_IMG, DmmRender.renderToImage(DMM, MapRegion.of(2, 5), ByondTypes.AREA));
    }

    private void assertRgb(final BufferedImage expected, final BufferedImage actual) {
        for (int x = 0; x < expected.getWidth(); x++) {
            for (int y = 0; y < expected.getHeight(); y++) {
                assertEquals(expected.getRGB(x, y), actual.getRGB(x, y));
            }
        }
    }
}