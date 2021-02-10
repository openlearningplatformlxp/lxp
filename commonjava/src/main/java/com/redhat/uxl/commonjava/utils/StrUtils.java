package com.redhat.uxl.commonjava.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * The type Str utils.
 */
public class StrUtils extends StringUtils {

    /**
     * First not blank string.
     *
     * @param values the values
     * @return the string
     */
    public static String firstNotBlank(String... values) {
        for (String value : values) {
            if (isNotBlank(value)) {
                return value;
            }
        }

        return null;
    }

    /**
     * Is all not blank boolean.
     *
     * @param value the value
     * @return the boolean
     */
    public static boolean isAllNotBlank(final String... value) {
        return !isAnyBlank(value);
    }

    /**
     * Is any blank boolean.
     *
     * @param values the values
     * @return the boolean
     */
    public static boolean isAnyBlank(final String... values) {
        for (final String value : values) {
            if (isBlank(value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Is not blank boolean.
     *
     * @param value the value
     * @return the boolean
     */
    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    /**
     * Is not empty boolean.
     *
     * @param value the value
     * @return the boolean
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

}
