package com.redhat.uxl.services.service;

/**
 * The interface Database settings service.
 */
public interface DatabaseSettingsService {

    /**
     * The interface Constants.
     */
    interface Constants {
        /**
         * The constant DISCOVER_ELEMENTS_KEY.
         */
        String DISCOVER_ELEMENTS_KEY = "settings_discover_cards";
    }

    /**
     * Find value string.
     *
     * @param name the name
     * @return the string
     */
    String findValue(String name);

    /**
     * Find int value integer.
     *
     * @param name the name
     * @return the integer
     */
    Integer findIntValue(String name);
}
