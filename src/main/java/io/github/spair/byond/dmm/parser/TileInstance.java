package io.github.spair.byond.dmm.parser;

import lombok.Data;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@SuppressWarnings("WeakerAccess")
public class TileInstance implements Iterable<DmmItem> {

    private String key;
    private List<DmmItem> dmmItems = new ArrayList<>();

    public TileInstance(final String key) {
        this.key = key;
    }

    public void addDmmItem(final DmmItem dmmItem) {
        dmmItems.add(dmmItem);
    }

    @Nonnull
    @Override
    public Iterator<DmmItem> iterator() {
        return dmmItems.iterator();
    }
}
