package com.redhat.uxl.services.exceptions;

/**
 * The type Totara manager not found exception.
 */
public class TotaraManagerNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Totara manager not found exception.
     *
     * @param id the id
     */
    public TotaraManagerNotFoundException(Long id) {
        super("Manager of user with id: " + id + " not found");
    }
}
