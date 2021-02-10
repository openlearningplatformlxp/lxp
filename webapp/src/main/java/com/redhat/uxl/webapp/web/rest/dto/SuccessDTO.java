package com.redhat.uxl.webapp.web.rest.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * The type Success dto.
 */
@Data
public class SuccessDTO implements Serializable {
    private boolean success = true;
    private int entitiesUpdated = 0;
    private String message;
    private Object data;

    /**
     * Sets exception.
     *
     * @param exception the exception
     */
    public void setException(Exception exception) {
        message = exception.getMessage();
        success = false;
    }
}
