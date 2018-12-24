package io.github.spair.byond.dmm.drawer;

public enum FilterMode {
    /** No filter will be applied */
    NONE,

    /** Listed types and all subtypes of them won't be rendered. */
    IGNORE,

    /** <b>Only</b> listed types and all subtypes of them will be rendered. */
    INCLUDE,

    /** <b>Only</b> listed types <b>(without subtypes)</b> will be rendered. */
    EQUAL
}
