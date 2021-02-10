package com.redhat.uxl.webapp.security;

import org.springframework.security.core.AuthenticationException;

/**
 * The type Person not activated exception.
 */
public class PersonNotActivatedException extends AuthenticationException {

    /**
     * Instantiates a new Person not activated exception.
     *
     * @param message the message
     */
    public PersonNotActivatedException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Person not activated exception.
     *
     * @param message the message
     * @param t       the t
     */
    public PersonNotActivatedException(String message, Throwable t) {
        super(message, t);
    }
}
