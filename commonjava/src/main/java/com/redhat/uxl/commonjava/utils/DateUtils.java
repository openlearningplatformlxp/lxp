package com.redhat.uxl.commonjava.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The type Date utils.
 */
public final class DateUtils {
    private DateUtils() {
    }

    /**
     * Parse date date time.
     *
     * @param date the date
     * @return the date time
     */
    public static DateTime parseDate(String date) {
        if (StringUtils.isNotEmpty(date)) {
            date = StringUtils.replace(date, "\"", "");
            return DateTime.parse(date);
        }
        return null;
    }

    /**
     * Format string.
     *
     * @param date   the date
     * @param format the format
     * @return the string
     */
    public static String format(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        if (date != null) {
            return formatter.format(date);
        } else {
            return "";
        }
    }
}
