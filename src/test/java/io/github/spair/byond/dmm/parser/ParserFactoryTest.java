package io.github.spair.byond.dmm.parser;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ParserFactoryTest {

    @Test
    public void testCreateFromText() {
        String standardText = "1234567890";
        String tgmText = "//MAP CONVERTED BY dmm2tgm.py THIS HEADER COMMENT PREVENTS RECONVERSION, DO NOT REMOVE\n1234567890";

        assertTrue(ParserFactory.createFromText(standardText) instanceof StandardParser);
        assertTrue(ParserFactory.createFromText(tgmText) instanceof TGMParser);
    }
}