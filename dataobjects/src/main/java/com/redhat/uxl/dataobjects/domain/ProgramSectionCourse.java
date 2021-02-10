package com.redhat.uxl.dataobjects.domain;

import java.util.Date;
import lombok.Data;

/**
 * The type Program section course.
 */
@Data
public class ProgramSectionCourse {

  private Long id;
  private String type;
  private String cms;
  private Long sectionId;
  private Long itemId;
  private String title;
  private String url;
  private String description;
  private Date dueDate;
}
