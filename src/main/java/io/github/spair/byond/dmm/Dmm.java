package io.github.spair.byond.dmm;

import lombok.Data;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

@Data
public class Dmm {

    private Map<Integer, ZLevel> zLevels = new TreeMap<>();

    public ZLevel getZLevel(final int zLevel) {
        return zLevels.get(zLevel);
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
