package io.github.spair.byond.dmm;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParserFactoryTest {

    @Test
    public void testCreateFromTextWithTGMHeader() {
        MapParser parser = ParserFactory.createFromText("//MAP CONVERTED BY dmm2tgm.py THIS HEADER COMMENT PREVENTS RECONVERSION, DO NOT REMOVE");
        assertTrue(parser instanceof TGMParser);
    }

    @Test
    public void testCreateFromTextWithoutTGMHeader() {
        MapParser parser = ParserFactory.createFromText("Placeholder text");
        assertTrue(parser instanceof StandardParser);
    }
}