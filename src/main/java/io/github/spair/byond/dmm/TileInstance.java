package io.github.spair.byond.dmm;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TileInstance {

    private String key;
    private List<DmmItem> dmmItems = new ArrayList<>();

    public TileInstance(final String key) {
        this.key = key;
    }

    public void addDmmItem(final DmmItem dmmItem) {
        dmmItems.add(dmmItem);
    }
}
