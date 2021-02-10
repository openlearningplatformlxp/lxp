package com.redhat.uxl.services.service.demo.dto;

import lombok.Data;

import java.util.List;

/**
 * The type Course list dto.
 */
@Data
public class CourseListDTO {
    /**
     * The Course ids.
     */
    List<Long> courseIds;
}
