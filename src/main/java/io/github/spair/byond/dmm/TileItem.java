package io.github.spair.byond.dmm;

import io.github.spair.byond.VarWrapper;
import io.github.spair.byond.dme.DmeItem;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
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
        this.customVars = Collections.unmodifiableMap(customVars);
    }

    public String getType() {
        return dmeItem.getType();
    }

    public boolean isType(final String type) {
        return dmeItem.isType(type);
    }

    public boolean hasCustomVar(final String name) {
        return customVars.containsKey(name);
    }

    public boolean hasOriginalVar(final String name) {
        return getOriginalVars().containsKey(name);
    }

    public String getCustomVar(final String name) {
        return VarWrapper.rawValue(customVars.get(name));
    }

    public Optional<String> getCustomVarText(final String name) {
        return VarWrapper.optionalText(customVars.get(name));
    }

    public Optional<String> getCustomVarFilePath(final String name) {
        return VarWrapper.optionalFilePath(customVars.get(name));
    }

    public Optional<Integer> getCustomVarInt(final String name) {
        return VarWrapper.optionalInt(customVars.get(name));
    }

    public Optional<Double> getCustomVarDouble(final String name) {
        return VarWrapper.optionalDouble(customVars.get(name));
    }

    public Map<String, String> getOriginalVars() {
        return dmeItem.getAllVars();
    }

    public String getOriginalVar(final String name) {
        return dmeItem.getVar(name);
    }

    public Optional<String> getOriginalVarText(final String name) {
        return dmeItem.getVarText(name);
    }

    public Optional<String> getOriginalVarFilePath(final String name) {
        return dmeItem.getVarFilePath(name);
    }

    public Optional<Integer> getOriginalVarInt(final String name) {
        return dmeItem.getVarInt(name);
    }

    public Optional<Double> getOriginalVarDouble(final String name) {
        return dmeItem.getVarDouble(name);
    }

    public String getCustomOrOriginalVar(final String name) {
        return VarWrapper.rawValue(customVars.getOrDefault(name, dmeItem.getVar(name)));
    }

    public Optional<String> getCustomOrOriginalVarText(final String name) {
        return VarWrapper.optionalText(customVars.getOrDefault(name, dmeItem.getVar(name)));
    }

    public Optional<String> getCustomOrOriginalVarFilePath(final String name) {
        return VarWrapper.optionalFilePath(customVars.getOrDefault(name, dmeItem.getVar(name)));
    }

    public Optional<Integer> getCustomOrOriginalVarInt(final String name) {
        return VarWrapper.optionalInt(customVars.getOrDefault(name, dmeItem.getVar(name)));
    }

    public Optional<Double> getCustomOrOriginalVarDouble(final String name) {
        return VarWrapper.optionalDouble(customVars.getOrDefault(name, dmeItem.getVar(name)));
    }
}
