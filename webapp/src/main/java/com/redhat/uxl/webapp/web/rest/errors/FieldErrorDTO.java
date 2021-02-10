package com.redhat.uxl.webapp.web.rest.errors;

import java.io.Serializable;

/**
 * The type Field error dto.
 */
public class FieldErrorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String objectName;

    private final String field;

    private final String message;

    /**
     * Instantiates a new Field error dto.
     *
     * @param dto     the dto
     * @param field   the field
     * @param message the message
     */
    FieldErrorDTO(String dto, String field, String message) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
    }

}
