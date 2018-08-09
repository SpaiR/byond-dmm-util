package io.github.spair.byond.dmm.parser;

import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dmm.Dmm;

interface MapParser {

    Dmm parse(final String dmmText, final Dme dme);
}
