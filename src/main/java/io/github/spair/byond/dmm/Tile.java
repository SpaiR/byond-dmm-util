package io.github.spair.byond.dmm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tile {

    private int x, y, z;
    private TileInstance tileInstance;
}
