package dev.the_fireplace.libtest.setup;

public class TestFailedError extends Error
{
    public TestFailedError() {
    }

    public TestFailedError(String message) {
        super(message);
    }

    public TestFailedError(String message, Throwable cause) {
        super(message, cause);
    }

    public TestFailedError(Throwable cause) {
        super(cause);
    }

    public TestFailedError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
