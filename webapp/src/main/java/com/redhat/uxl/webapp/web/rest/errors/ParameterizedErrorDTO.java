package com.redhat.uxl.webapp.web.rest.errors;

import java.io.Serializable;

/**
 * The type Parameterized error dto.
 */
public class ParameterizedErrorDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String message;
    private final String[] params;

    /**
     * Instantiates a new Parameterized error dto.
     *
     * @param message the message
     * @param params  the params
     */
    public ParameterizedErrorDTO(String message, String... params) {
        this.message = message;
        this.params = params;
    }

}
