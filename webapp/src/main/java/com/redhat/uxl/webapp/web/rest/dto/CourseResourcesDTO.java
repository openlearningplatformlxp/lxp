package com.redhat.uxl.webapp.web.rest.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Course resources dto.
 */
@Data
public class CourseResourcesDTO implements Serializable {

    private List<CourseMaterialDTO> materials = new ArrayList<>();
    private List<CourseHelpLinkDTO> links = new ArrayList<>();
}
