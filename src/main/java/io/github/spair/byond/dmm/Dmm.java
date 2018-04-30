package io.github.spair.byond.dmm;

import io.github.spair.byond.dme.Dme;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Objects;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@Data
@SuppressWarnings("WeakerAccess")
public class Dmm {

    @Setter(AccessLevel.NONE)
    private boolean isDmeInjected = false;
    private Map<Integer, ZLevel> zLevels = new TreeMap<>();

    public void injectDme(final Dme dme) {
        zLevels.values().forEach(zLevel ->
                zLevel.forEach(tile -> {
                    List<TileItem> tileItems = new ArrayList<>();

                    tile.getTileInstance().getDmmItems().forEach((type, dmmItem) ->
                            tileItems.add(
                                    new TileItem(tile.getX(), tile.getY(), tile.getZ(), dme.getItem(type), dmmItem)
                            )
                    );

                    tile.setTileItems(tileItems);
                })
        );

        isDmeInjected = true;
    }

    public ZLevel getZLevel(final int levelNum) {
        return zLevels.get(levelNum);
    }

    public ZLevel getZLevelOrCreate(final int levelNum) {
        ZLevel zLevel = zLevels.getOrDefault(levelNum, new ZLevel(levelNum));
        zLevels.putIfAbsent(levelNum, zLevel);
        return zLevel;
    }

    public Tile getTile(final int x, final int y, final int z) {
        ZLevel zLevel = zLevels.get(z);

        if (Objects.nonNull(zLevel)) {
            return zLevel.getTile(x, y);
        } else {
            return null;
        }
    }

    public Set<Integer> listAvailableZLevels() {
        return zLevels.keySet();
    }
}
