package com.redhat.uxl.services.exceptions;

/**
 * The type Totara activity not completed exception.
 */
public class TotaraActivityNotCompletedException extends RuntimeException {
    /**
     * Instantiates a new Totara activity not completed exception.
     *
     * @param courseId the course id
     */
    public TotaraActivityNotCompletedException(Long courseId) {
        super("Activity with courseId: " + courseId + " not completed");
    }
}
