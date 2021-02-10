package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import lombok.Data;

/**
 * The type Scorm value set dto.
 */
@Data
public class ScormValueSetDTO {
    private String key;
    private String value;
    private boolean allowCompletion;
}
