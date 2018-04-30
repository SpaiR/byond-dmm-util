package io.github.spair.byond.dmm;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@SuppressWarnings("WeakerAccess")
public class TileInstance {

    private String type;
    private Map<String, DmmItem> dmmItems = new HashMap<>();

    public TileInstance(final String type) {
        this.type = type;
    }

    public void addDmmItem(final DmmItem dmmItem) {
        dmmItems.put(dmmItem.getType(), dmmItem);
    }

    public DmmItem getDmmItem(final String type) {
        return dmmItems.get(type);
    }
}
