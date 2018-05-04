package io.github.spair.byond.dmm.render;

import lombok.Data;

import java.awt.image.BufferedImage;

@Data
class TileItemImage {

    private int xShift, yShift;
    private BufferedImage image;
}
