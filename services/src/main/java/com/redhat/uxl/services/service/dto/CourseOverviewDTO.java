package com.redhat.uxl.services.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Course overview dto.
 */
@Data
public class CourseOverviewDTO implements Serializable {

  private String description;
  private List<CourseTagDTO> tagsTypes = new ArrayList<>();
}
