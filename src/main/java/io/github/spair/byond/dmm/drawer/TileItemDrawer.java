package io.github.spair.byond.dmm.drawer;

import io.github.spair.byond.ByondVars;
import io.github.spair.byond.dmi.Dmi;
import io.github.spair.byond.dmi.DmiSprite;
import io.github.spair.byond.dmi.SpriteDir;
import io.github.spair.byond.dmi.slurper.DmiSlurper;
import io.github.spair.byond.dmm.TileItem;
import lombok.val;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class TileItemDrawer {

    private final int iconSize;
    private final String dmeRootPath;

    private final Map<String, Dmi> dmiCache = new HashMap<>();
    private final DmiSlurper dmiSlurper = new DmiSlurper();

    TileItemDrawer(final int iconSize, final String dmeRootPath) {
        this.iconSize = iconSize;
        this.dmeRootPath = dmeRootPath;
    }

    TileItemImage drawItem(final TileItem item) {
        // No need to do anything since item is invisible.
        if (item.getVarInt(ByondVars.ALPHA).orElse(255) == 0)
            return null;

        val itemIcon = item.getVarFilePath(ByondVars.ICON).orElse("");
        val itemIconState = item.getVarText(ByondVars.ICON_STATE).orElse("");

        if (itemIcon.isEmpty())
            return null;

        val itemDmi = getCachedDmi(itemIcon);
        if (itemDmi == null)
            return null;

        val itemDir = SpriteDir.valueOfByondDir(item.getVarInt(ByondVars.DIR).orElse(SpriteDir.SOUTH.dirValue));
        val itemSprite = getItemSprite(itemDmi, itemIconState, itemDir);
        if (itemSprite == null)
            return null;

        val itemImage = new TileItemImage();

        itemImage.setXShift(item.getVarDouble(ByondVars.PIXEL_X).orElse(0.0).intValue());
        itemImage.setYShift(item.getVarDouble(ByondVars.PIXEL_Y).orElse(0.0).intValue());
        itemImage.setImage(deepImageCopy(itemSprite.getSprite()));

        // BYOND renders objects from bottom to top, while DmmDrawer do it from top to bottom.
        // This additional shift helps to properly render objects, which have sprite height more then world icon_size.
        if (iconSize < itemDmi.getSpriteHeight()) {
            itemImage.setYShift(itemImage.getYShift() + itemDmi.getSpriteHeight() - iconSize);
        }

        applyPixelEffects(item, itemImage.getImage());

        return itemImage;
    }

    private void applyPixelEffects(final TileItem item, final BufferedImage img) {
        val effectsToApply = getEffectsList(item);

        if (effectsToApply.isEmpty())
            return;

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

    private List<PixelEffect> getEffectsList(final TileItem item) {
        val effectsList = new ArrayList<PixelEffect>();

        val colorValue = ColorExtractor.extract(item.getVarText(ByondVars.COLOR).orElse(""));
        val alphaValue = item.getVarInt(ByondVars.ALPHA).orElse(255);

        if (colorValue != null)
            effectsList.add(new PixelEffectColor(colorValue));
        if (alphaValue != 255)
            effectsList.add(new PixelEffectAlpha(alphaValue));

        return effectsList;
    }

    private Dmi getCachedDmi(final String itemIcon) {
        if (dmiCache.containsKey(itemIcon)) {
            return dmiCache.get(itemIcon);
        } else {
            File spriteFile = new File(dmeRootPath + File.separator + itemIcon);
            Dmi dmi = null;

            if (spriteFile.exists()) {
                dmi = dmiSlurper.slurpUp(spriteFile);
            }

            dmiCache.put(itemIcon, dmi);
            return dmi;
        }
    }

    private DmiSprite getItemSprite(final Dmi itemDmi, final String itemIconState, final SpriteDir itemDir) {
        // fallbacks to get at least some image
        // both are represent default byond behaviour
        return itemDmi.getStateSprite(itemIconState, itemDir)
                .orElseGet(() -> itemDmi.getStateSprite(itemIconState)
                        .orElseGet(() -> itemDmi.getStateSprite("")
                                .orElse(null)));
    }

    private BufferedImage deepImageCopy(final BufferedImage img) {
        val colorModel = img.getColorModel();
        val raster = img.copyData(null);
        val isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }
}
