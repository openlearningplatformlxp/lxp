package com.redhat.uxl.commonjava.utils;

/**
 * The type Long utils.
 */
public final class LongUtils {
    private LongUtils() {
    }

    /**
     * Safe value of long.
     *
     * @param value the value
     * @return the long
     */
    public static Long safeValueOf(String value) {
        if (StrUtils.isBlank(value)) {
            return null;
        }

        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
