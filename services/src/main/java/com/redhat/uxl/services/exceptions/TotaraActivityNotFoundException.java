package com.redhat.uxl.services.exceptions;

/**
 * The type Totara activity not found exception.
 */
public class TotaraActivityNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Totara activity not found exception.
     *
     * @param instance the instance
     */
    public TotaraActivityNotFoundException(Long instance) {
        super("Activity with instanceId: " + instance + " not found");
    }
}
