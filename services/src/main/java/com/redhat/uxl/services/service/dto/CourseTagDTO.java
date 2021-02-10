package com.redhat.uxl.services.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Course tag dto.
 */
@Data
public class CourseTagDTO implements Serializable {

  private String name;
  private List<String> tags = new ArrayList<>();
}
