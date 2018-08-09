package io.github.spair.byond.dmm;

import io.github.spair.byond.VarUtil;
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
@SuppressWarnings({"WeakerAccess", "unused"})
public class TileItem {

    @Getter(AccessLevel.NONE)
    private DmeItem dmeItem;

    private int x, y, z;
    private Map<String, String> customVars;

    public TileItem(
            final int x, final int y, final int z, final DmeItem dmeItem, final Map<String, String> customVars) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dmeItem = dmeItem;
        this.customVars = customVars;
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
        return dmeItem.getVars().containsKey(name);
    }

    public Optional<String> getCustomVar(final String name) {
        return VarUtil.optionalNullable(customVars.get(name));
    }

    public Optional<String> getCustomVarUnquoted(final String name) {
        return VarUtil.optionalUnquoted(customVars.get(name));
    }

    public Optional<Integer> getCustomVarAsInt(final String name) {
        return VarUtil.optionalInt(customVars.get(name));
    }

    public Optional<Double> getCustomVarAsDouble(final String name) {
        return VarUtil.optionalDouble(customVars.get(name));
    }

    public Map<String, String> getOriginalVars() {
        return dmeItem.getVars();
    }

    public Optional<String> getOriginalVar(final String name) {
        return dmeItem.getVar(name);
    }

    public Optional<String> getOriginalVarUnquoted(final String name) {
        return dmeItem.getVarUnquoted(name);
    }

    public Optional<Integer> getOriginalVarAsInt(final String name) {
        return dmeItem.getVarAsInt(name);
    }

    public Optional<Double> getOriginalVarAsDouble(final String name) {
        return dmeItem.getVarAsDouble(name);
    }

    public Optional<String> getCustomOrOriginalVar(final String name) {
        return VarUtil.optionalNullable(customVars.getOrDefault(name, dmeItem.getVars().get(name)));
    }

    public Optional<String> getCustomOrOriginalVarUnquoted(final String name) {
        return VarUtil.optionalUnquoted(customVars.getOrDefault(name, dmeItem.getVars().get(name)));
    }

    public Optional<Integer> getCustomOrOriginalVarAsInt(final String name) {
        return VarUtil.optionalInt(customVars.getOrDefault(name, dmeItem.getVars().get(name)));
    }

    public Optional<Double> getCustomOrOriginalVarAsDouble(final String name) {
        return VarUtil.optionalDouble(customVars.getOrDefault(name, dmeItem.getVars().get(name)));
    }
}
