package dev.the_fireplace.lib.api.math.exceptions;

public class ParsingException extends Exception
{
    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
