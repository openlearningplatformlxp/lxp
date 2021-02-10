package com.redhat.uxl.dataobjects.domain.types;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The enum Calendar color type.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)

// Matt Perkins, 9/23/19
// This is being overridden in calendar.controller.js

public enum CalendarColorType {
    /**
     * Light blue calendar color type.
     */
    LIGHT_BLUE("#4f90cb", "#3f6c96"),
    /**
     * Red calendar color type.
     */
    RED("#9f3530", "#762d29"),
    /**
     * Light green calendar color type.
     */
    LIGHT_GREEN("#6ca45e", "#537b4a"),
    /**
     * Grey calendar color type.
     */
    GREY("#4d555b", "#3e4347"),
    /**
     * Light grey calendar color type.
     */
    LIGHT_GREY("#747a7f", "#5d6165");

    private String backgroundColor;
    private String borderColor;

    CalendarColorType(String backgroundColor, String borderColor) {
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
    }

    /**
     * Gets background color.
     *
     * @return the background color
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Gets border color.
     *
     * @return the border color
     */
    public String getBorderColor() {
        return borderColor;
    }

    /**
     * Build from calendar color type.
     *
     * @param type the type
     * @return the calendar color type
     */
    public static CalendarColorType buildFrom(CalendarType type) {
        switch (type) {
        case PROFESSIONAL:
            return RED;
        case MANAGER:
            return LIGHT_BLUE;
        case SALES:
            return LIGHT_GREEN;
        case TECHNICAL:
            return GREY;
        case OTHER:
        default:
            return LIGHT_GREY;
        }
    }
}
