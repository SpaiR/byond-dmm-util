package io.github.spair.byond.dmm;

import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;

import java.util.Map;

public final class TileItemFactory {

    public static TileItem create(final Map<String, String> customVars) {
        return new TileItem(1, 1, 1, new DmeItem("/obj/item", new Dme()), customVars);
    }
}
