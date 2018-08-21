package io.github.spair.byond.dmm.parser;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.ByondVars;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dmm.Dmm;
import io.github.spair.byond.dmm.Tile;
import io.github.spair.byond.dmm.TileItem;
import io.github.spair.dmm.io.DmmData;
import io.github.spair.dmm.io.TileLocation;
import io.github.spair.dmm.io.TileObject;
import io.github.spair.dmm.io.reader.DmmReader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("WeakerAccess")
public final class DmmParser {

    private Dmm dmm = new Dmm();

    public static Dmm parse(final File dmmFile, final Dme dme) {
        DmmParser parser = new DmmParser();
        parser.prepareDmm(dme);

        DmmData dmmData = DmmReader.readMap(dmmFile);
        parser.processDmmData(dmmData, dme);

        return parser.dmm;
    }

    private void prepareDmm(final Dme dme) {
        dmm.setDmeRootPath(dme.getAbsoluteRootPath());
        dmm.setIconSize(dme.getItem(ByondTypes.WORLD).getVarAsInt(ByondVars.ICON_SIZE).orElse(Dmm.DEFAULT_ICON_SIZE));
    }

    private void processDmmData(final DmmData dmmData, final Dme dme) {
        val maxX = dmmData.getMaxX();
        val maxY = dmmData.getMaxY();

        dmm.setMaxX(maxX);
        dmm.setMaxY(maxY);
        dmm.setTileContents(dmmData.getTileContentsByKey());

        val tiles = new Tile[maxY][maxX];

        for (int x = 1; x <= maxX; x++) {
            for (int y = maxY; y >= 1; y--) {
                val tileContent = dmmData.getTileContentByLocation(TileLocation.of(x, y));
                val tile = new Tile(x, y, tileContent);

                for (TileObject tileObject : tileContent) {
                    val tileItem = new TileItem(x, y, dme.getItem(tileObject.getType()), tileObject.getVars());
                    tile.addTileItem(tileItem);
                }

                tiles[y - 1][x - 1] = tile;
            }
        }

        dmm.setTiles(tiles);
    }
}
