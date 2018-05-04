package io.github.spair.byond.dmm.parser;

final class ParserFactory {

    private static final String TGM_HEADER =
            "//MAP CONVERTED BY dmm2tgm.py THIS HEADER COMMENT PREVENTS RECONVERSION, DO NOT REMOVE";

    static MapParser createFromText(final String dmmText) {
        return isTGM(dmmText) ? new TGMParser() : new StandardParser();
    }

    private static boolean isTGM(final String dmmText) {
        return dmmText.startsWith(TGM_HEADER);
    }

    private ParserFactory() {
    }
}
