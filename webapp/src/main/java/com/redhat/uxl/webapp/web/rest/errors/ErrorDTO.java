package com.redhat.uxl.webapp.web.rest.errors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Error dto.
 */
public class ErrorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String message;
    private final String description;

    private List<FieldErrorDTO> fieldErrors;

    /**
     * Instantiates a new Error dto.
     *
     * @param message the message
     */
    ErrorDTO(String message) {
        this(message, null);
    }

    /**
     * Instantiates a new Error dto.
     *
     * @param message     the message
     * @param description the description
     */
    ErrorDTO(String message, String description) {
        this.message = message;
        this.description = description;
    }

    /**
     * Instantiates a new Error dto.
     *
     * @param message     the message
     * @param description the description
     * @param fieldErrors the field errors
     */
    ErrorDTO(String message, String description, List<FieldErrorDTO> fieldErrors) {
        this.message = message;
        this.description = description;
        this.fieldErrors = fieldErrors;
    }

    /**
     * Add.
     *
     * @param objectName the object name
     * @param field      the field
     * @param message    the message
     */
    public void add(String objectName, String field, String message) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new FieldErrorDTO(objectName, field, message));
    }

}
