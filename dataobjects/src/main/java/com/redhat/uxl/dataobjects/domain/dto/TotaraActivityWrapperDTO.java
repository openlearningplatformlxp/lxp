package com.redhat.uxl.dataobjects.domain.dto;

import lombok.Data;

/**
 * The type Totara activity wrapper dto.
 */
@Data
public class TotaraActivityWrapperDTO {
  private Long courseId;
  private String scormName;
  private String quizName;
  private String moduleName;
  private String forumName;
}
