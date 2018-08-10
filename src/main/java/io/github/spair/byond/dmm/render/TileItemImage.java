package io.github.spair.byond.dmm.render;

import lombok.Data;

import java.awt.image.BufferedImage;

@Data
final class TileItemImage {
    private int xShift;
    private int yShift;
    private BufferedImage image;
}
