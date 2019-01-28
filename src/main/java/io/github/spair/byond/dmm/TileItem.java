package io.github.spair.byond.dmm;

import io.github.spair.byond.VarWrapper;
import io.github.spair.byond.dme.DmeItem;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Optional;

@Getter
@ToString(exclude = "dmeItem")
@EqualsAndHashCode
public class TileItem {

    @Getter(AccessLevel.NONE)
    private final DmeItem dmeItem;

    private final int x, y;
    private Map<String, String> customVars;

    TileItem(final int x, final int y, final DmeItem dmeItem, final Map<String, String> customVars) {
        this.x = x;
        this.y = y;
        this.dmeItem = dmeItem;
        this.customVars = customVars;
    }

    public String getType() {
        return dmeItem.getType();
    }

    public boolean isType(final String type) {
        return dmeItem.isType(type);
    }

    public boolean hasVar(final String name) {
        return customVars.containsKey(name) || getOriginalVars().containsKey(name);
    }

    public Map<String, String> getOriginalVars() {
        return dmeItem.getAllVars();
    }

    public String getVar(final String name) {
        return VarWrapper.rawValue(customVars.getOrDefault(name, dmeItem.getVar(name)));
    }

    public Optional<String> getVarText(final String name) {
        return VarWrapper.optionalText(customVars.getOrDefault(name, dmeItem.getVar(name)));
    }

    public Optional<String> getVarFilePath(final String name) {
        return VarWrapper.optionalFilePath(customVars.getOrDefault(name, dmeItem.getVar(name)));
    }

    public Optional<Integer> getVarInt(final String name) {
        return VarWrapper.optionalInt(customVars.getOrDefault(name, dmeItem.getVar(name)));
    }

    public Optional<Double> getVarDouble(final String name) {
        return VarWrapper.optionalDouble(customVars.getOrDefault(name, dmeItem.getVar(name)));
    }

    public void setVar(final String name, final String value) {
        customVars.put(name, value);
    }

    public void setVarText(final String name, final String value) {
        customVars.put(name, '"' + value + '"');
    }

    public void setVarFilePath(final String name, final String value) {
        customVars.put(name, "'" + value + "'");
    }

    public void setVar(final String name, final Number value) {
        customVars.put(name, value.toString());
    }

    public void setEmptyVar(final String name) {
        customVars.put(name, "null");
    }
}
