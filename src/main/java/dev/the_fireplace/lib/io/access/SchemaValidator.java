package dev.the_fireplace.lib.io.access;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SchemaValidator
{
    @SuppressWarnings("HardcodedFileSeparator")
    static final String SCHEMA_PATTERN_STRING = "[a-zA-Z0-9_\\-]+";
    private static final Pattern VALID_SCHEMA_REGEX = Pattern.compile('^' + SCHEMA_PATTERN_STRING + '$');
    private static final Pattern DASHES = Pattern.compile("-", Pattern.LITERAL);

    public static boolean isValid(String s) {
        return VALID_SCHEMA_REGEX.matcher(s).matches();
    }

    public static String minimizeSchema(String s) {
        return DASHES.matcher(s).replaceAll(Matcher.quoteReplacement("")).toLowerCase(Locale.ROOT);
    }
}
