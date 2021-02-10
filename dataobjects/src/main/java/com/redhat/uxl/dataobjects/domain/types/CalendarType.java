package com.redhat.uxl.dataobjects.domain.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;

/**
 * The enum Calendar type.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CalendarType {
    /**
     * Professional calendar type.
     */
    PROFESSIONAL,
    /**
     * Sales calendar type.
     */
    SALES,
    /**
     * Technical calendar type.
     */
    TECHNICAL,
    /**
     * Manager calendar type.
     */
    MANAGER,
    /**
     * Other calendar type.
     */
    OTHER;

    /**
     * Build from calendar type.
     *
     * @param value the value
     * @return the calendar type
     */
    public static CalendarType buildFrom(String value) {
        value = StringUtils.lowerCase(value);
        value = StringUtils.trim(value);
        if (StringUtils.equals("technical", value)) {
            return TECHNICAL;
        } else if (StringUtils.equals("professional development and individual leadership", value)) {
            return PROFESSIONAL;
        } else if (StringUtils.equals("sales", value)) {
            return SALES;
        } else if (StringUtils.equals("manager development and team leadership", value)) {
            return MANAGER;
        } else {
            return OTHER;
        }
    }
}
