package io.github.spair.byond.dmm;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class DmmItem {

    private String type;
    private Map<String, String> vars = new HashMap<>();

    public DmmItem(final String type) {
        this.type = type;
    }

    public void setVar(final String name, final String value) {
        vars.put(name, value);
    }

    public void setStringVar(final String name, final String value) {
        vars.put(name, '"' + value + '"');
    }
}
