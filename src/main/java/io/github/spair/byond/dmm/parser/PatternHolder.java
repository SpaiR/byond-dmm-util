package io.github.spair.byond.dmm.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class PatternHolder {

    private final Pattern tile = Pattern.compile("\"(\\w+)\"\\s=\\s\\((.*)\\)\n");

    private final Pattern splitItem = Pattern.compile("(,|^)(?=/)(?![^{]*[}])");
    private final Pattern splitVars = Pattern.compile(";\\s?(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    private final Pattern splitVar = Pattern.compile("\\s=\\s");

    private final Pattern splitNewLine = Pattern.compile("\n");
    private final Pattern map = Pattern.compile("\\((\\d+?),(\\d+?),(\\d+?)\\)\\s=\\s\\{\"\\s*([\\na-zA-Z]+)\\s*\"}");

    private final Pattern itemWithVar = Pattern.compile("^(/.+)\\{(.*)}");

    Matcher mapMatcher(final String text) {
        return map.matcher(text);
    }

    Matcher tileMatcher(final String text) {
        return tile.matcher(text);
    }

    Matcher itemWithVarMatcher(final String text) {
        return itemWithVar.matcher(text);
    }

    String[] splitItem(final String text) {
        return splitItem.split(text);
    }

    String[] splitVars(final String text) {
        return splitVars.split(text);
    }

    String[] splitVar(final String text) {
        return splitVar.split(text);
    }

    String[] splitNewLine(final String text) {
        return splitNewLine.split(text);
    }
}
