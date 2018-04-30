package io.github.spair.byond.dmm;

import io.github.spair.byond.dme.DmeItem;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString(exclude = "dmeItem")
@EqualsAndHashCode
@SuppressWarnings("WeakerAccess")
public class TileItem {

    @Getter(AccessLevel.NONE)
    private DmeItem dmeItem;

    private int x, y, z;
    private Map<String, String> customVars;

    TileItem(final int x, final int y, final int z, final DmeItem dmeItem, final DmmItem dmmItem) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dmeItem = dmeItem;
        customVars = dmmItem.getVars();
    }

    public boolean hasCustomVars() {
        return !customVars.isEmpty();
    }

    public String getType() {
        return dmeItem.getType();
    }

    public Map<String, String> getOriginalVars() {
        return dmeItem.getVars();
    }

    public String getCustomVar(final String name) {
        return customVars.get(name);
    }

    public String getOriginalVar(final String name) {
        return dmeItem.getVar(name);
    }

    public String getCustomOrOriginalVar(final String name) {
        return customVars.getOrDefault(name, dmeItem.getVar(name));
    }

    public boolean isType(final String type) {
        return dmeItem.isType(type);
    }
}
