package io.github.spair.byond.dmm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class MapDeclaration {

    private int x, y, z;
    private String mapText;
}
