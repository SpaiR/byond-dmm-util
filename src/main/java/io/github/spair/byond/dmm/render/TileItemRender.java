package io.github.spair.byond.dmm.render;

import io.github.spair.byond.dmi.Dmi;
import io.github.spair.byond.dmi.DmiSlurper;
import io.github.spair.byond.dmi.DmiSprite;
import io.github.spair.byond.dmi.SpriteDir;
import io.github.spair.byond.dmm.TileItem;
import lombok.val;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
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

        applyPixelEffects(item, itemImage.getImage());

        return Optional.of(itemImage);
    }

    private void applyPixelEffects(final TileItem item, final BufferedImage img) {
        val colorValue = VarExtractor.color(item);
        val alphaValue = VarExtractor.alpha(item);

        val hasColor = !colorValue.isEmpty();
        val notDefaultAlpha = alphaValue != VarExtractor.DEFAULT_ALPHA;

        val effectsToApply = new ArrayList<PixelEffect>();

        if (hasColor) {
            ColorParser.parse(colorValue).ifPresent(color -> effectsToApply.add(new ColorPixelEffect(color)));
        }
        if (notDefaultAlpha) {
            effectsToApply.add(new AlphaPixelEffect(alphaValue));
        }

        if (effectsToApply.isEmpty()) {
            return;
        }

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int pixel = img.getRGB(x, y);

                if (pixel == 0) {
                    continue;
                }

                for (val effect : effectsToApply) {
                    pixel = effect.apply(pixel);
                }

                img.setRGB(x, y, pixel);
            }
        }
    }

    private Dmi getCachedDmi(final String itemIcon) {
        if (dmiCache.containsKey(itemIcon)) {
            return dmiCache.get(itemIcon);
        } else {
            Dmi dmi = DmiSlurper.slurpUp(new File(dmeRootPath + File.separator + itemIcon));
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
