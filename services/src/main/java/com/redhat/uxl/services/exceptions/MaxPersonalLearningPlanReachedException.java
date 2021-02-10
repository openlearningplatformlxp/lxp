package com.redhat.uxl.services.exceptions;

/**
 * The type Max personal learning plan reached exception.
 */
public class MaxPersonalLearningPlanReachedException extends RuntimeException {
    /**
     * Instantiates a new Max personal learning plan reached exception.
     *
     * @param max the max
     */
    public MaxPersonalLearningPlanReachedException(int max) {
        super("You can't create another personal learning path. Already reached your maximum of " + max);
    }
}
