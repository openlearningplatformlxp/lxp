package com.redhat.uxl.webapp.web.rest.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * The type Course help link dto.
 */
@Data
public class CourseHelpLinkDTO implements Serializable {
    private String link;
    private String description;
}
