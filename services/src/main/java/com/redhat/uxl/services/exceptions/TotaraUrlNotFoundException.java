package com.redhat.uxl.services.exceptions;

/**
 * The type Totara url not found exception.
 */
public class TotaraUrlNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Totara url not found exception.
     *
     * @param totaraUrl the totara url
     */
    public TotaraUrlNotFoundException(String totaraUrl) {
        super("Course with external url: " + totaraUrl + " not found");
    }
}
