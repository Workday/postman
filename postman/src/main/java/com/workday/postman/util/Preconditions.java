package com.workday.postman.util;

/**
 * @author Nathan Taylor
 * @since 2015-08-04
 */
public class Preconditions {

    private Preconditions() {
    }

    public static void checkState(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void checkArgument(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkNotNull(Object reference, String message) {
        if (reference == null) {
            throw new NullPointerException(message);
        }
    }
}
