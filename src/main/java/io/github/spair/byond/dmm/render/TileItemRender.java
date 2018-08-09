package io.github.spair.byond.dmm.render;

import io.github.spair.byond.dmi.Dmi;
import io.github.spair.byond.dmi.DmiSlurper;
import io.github.spair.byond.dmi.DmiSprite;
import io.github.spair.byond.dmi.SpriteDir;
import io.github.spair.byond.dmm.TileItem;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class TileItemRender {

    private final int iconSize;
    private final String dmeRootPath;
    private Map<String, Dmi> dmiCache = new HashMap<>();

    TileItemRender(final int iconSize, final String dmeRootPath) {
        this.iconSize = iconSize;
        this.dmeRootPath = dmeRootPath;
    }

    Optional<TileItemImage> renderItem(final TileItem item) {
        final String itemIcon = VarExtractor.icon(item);
        final String itemIconState = VarExtractor.iconState(item);

        if (itemIcon.isEmpty()) {
            return Optional.empty();
        }

        final Dmi itemDmi = getCachedDmi(itemIcon);
        final DmiSprite itemSprite = getItemSprite(itemDmi, itemIconState, VarExtractor.dir(item));

        if (itemSprite == null) {  // TODO: add placeholder for items without sprites
            return Optional.empty();
        }

        TileItemImage itemImage = new TileItemImage();

        itemImage.setXShift(VarExtractor.pixelX(item));
        itemImage.setYShift(VarExtractor.pixelY(item));

        // BYOND renders objects from bottom to top, while DmmRender do it from top to bottom.
        // This additional shift helps to properly render objects, which have sprite height more then world icon_size.
        if (iconSize < itemDmi.getMetadata().getSpritesHeight()) {
            itemImage.setYShift(itemImage.getYShift() + itemDmi.getMetadata().getSpritesHeight() - iconSize);
        }

        itemImage.setImage(deepImageCopy(itemSprite.getSprite()));

        applyColorValue(VarExtractor.color(item), itemImage.getImage());
        applyAlphaValue(VarExtractor.alpha(item), itemImage.getImage());

        return Optional.of(itemImage);
    }

    private void applyColorValue(final String colorValue, final BufferedImage img) {
        if (!colorValue.isEmpty()) {
            ColorParser.parse(colorValue).ifPresent(color -> ColorApplier.apply(color, img));
        }
    }

    private void applyAlphaValue(final int alphaValue, final BufferedImage img) {
        if (alphaValue != VarExtractor.DEFAULT_ALPHA) {
            AlphaApplier.apply(alphaValue, img);
        }
    }

    private Dmi getCachedDmi(final String itemIcon) {
        if (dmiCache.containsKey(itemIcon)) {
            return dmiCache.get(itemIcon);
        } else {
            Dmi dmi = DmiSlurper.slurpUp(new File(dmeRootPath + File.separatorChar + itemIcon));
            dmiCache.put(itemIcon, dmi);
            return dmi;
        }
    }

    private DmiSprite getItemSprite(final Dmi itemDmi, final String itemIconState, final SpriteDir itemDir) {
        return Optional
                .ofNullable(
                        itemDmi.getStateSprite(itemIconState, itemDir))
                .orElseGet(
                        () -> itemDmi.getStateSprite(itemIconState)
                );
    }

    private BufferedImage deepImageCopy(final BufferedImage img) {
        final ColorModel cm = img.getColorModel();
        final WritableRaster raster = img.copyData(null);
        final boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
