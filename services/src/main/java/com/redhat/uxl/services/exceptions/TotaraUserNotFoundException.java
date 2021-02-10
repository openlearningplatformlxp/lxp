package com.redhat.uxl.services.exceptions;

/**
 * The type Totara user not found exception.
 */
public class TotaraUserNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Totara user not found exception.
     *
     * @param email the email
     */
    public TotaraUserNotFoundException(String email) {
        super("Person with email: " + email + " not found");
    }
}
