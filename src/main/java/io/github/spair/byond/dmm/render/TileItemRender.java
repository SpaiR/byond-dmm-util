package io.github.spair.byond.dmm.render;

import io.github.spair.byond.dmi.Dmi;
import io.github.spair.byond.dmi.DmiSlurper;
import io.github.spair.byond.dmi.DmiSprite;
import io.github.spair.byond.dmi.SpriteDir;
import io.github.spair.byond.dmm.parser.TileItem;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

final class TileItemRender {

    private String dmeRootPath;
    private Map<String, Dmi> DMI_CACHE = new HashMap<>();

    TileItemRender(final String dmeRootPath) {
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

        if (Objects.isNull(itemSprite)) {  // TODO: maybe add placeholder for items without sprites?..
            return Optional.empty();
        }

        TileItemImage itemImage = new TileItemImage();

        itemImage.setXShift(VarExtractor.pixelX(item));
        itemImage.setYShift(VarExtractor.pixelY(item));
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
        if (DMI_CACHE.containsKey(itemIcon)) {
            return DMI_CACHE.get(itemIcon);
        } else {
            Dmi dmi = DmiSlurper.slurpUp(new File(dmeRootPath + File.separatorChar + itemIcon));
            DMI_CACHE.put(itemIcon, dmi);
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
        ColorModel cm = img.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = img.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
