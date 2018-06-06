package io.github.spair.byond.dmm;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeParser;
import io.github.spair.byond.dmm.parser.Dmm;
import io.github.spair.byond.dmm.parser.DmmParser;
import io.github.spair.byond.dmm.render.DmmRender;
import io.github.spair.byond.dmm.render.MapRegion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;

public class Main {
    public static void main(String[] args) throws Exception {
        long s = System.currentTimeMillis();

        Dme dme = DmeParser.parse(new File("TauCeti/taucetistation.dme"));
        Dmm dmm = new DmmParser().parse(new File("TauCeti/maps/z1.dmm"), dme);

        BufferedImage image = DmmRender.renderToImage(dmm, ByondTypes.AREA);

        File file = new File("result.png");
        if (!file.exists()) file.createNewFile();
        ImageIO.write(image, "PNG", file);

        System.out.println(System.currentTimeMillis() - s);
    }
}
