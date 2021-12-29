package dev.the_fireplace.lib.io.access;

import java.util.regex.Pattern;

public final class JsonFileConstants
{
    @SuppressWarnings("HardcodedFileSeparator")
    static final Pattern JSON_FILE_REGEX = Pattern.compile('^' + SchemaValidator.SCHEMA_PATTERN_STRING + "\\.json$");
    static final Pattern JSON_EXTENSION_LITERAL = Pattern.compile(".json", Pattern.LITERAL);
}
