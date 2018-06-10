package io.github.spair.byond.dmm.parser;

import io.github.spair.byond.VarUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@SuppressWarnings({"WeakerAccess", "unused"})
public class DmmItem {

    private String type;
    private Map<String, String> vars = new HashMap<>();

    public void setVar(final String name, final String value) {
        vars.put(name, value);
    }

    public Optional<String> getVar(final String name) {
        return VarUtil.optionalNullable(vars.get(name));
    }

    public Optional<String> getVarUnquoted(final String name) {
        return VarUtil.optionalUnquoted(vars.get(name));
    }

    public Optional<Integer> getVarAsInt(final String name) {
        return VarUtil.optionalInt(vars.get(name));
    }

    public Optional<Double> getVarAsDouble(final String name) {
        return VarUtil.optionalDouble(vars.get(name));
    }
}
