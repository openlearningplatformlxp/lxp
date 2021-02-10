package com.redhat.uxl.webapp.web.rest.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * The type Course material dto.
 */
@Data
public class CourseMaterialDTO implements Serializable {

    private String name;
    private String type;
    private String size;
    private String pictureUrl;
    private String downloadLink;
}
