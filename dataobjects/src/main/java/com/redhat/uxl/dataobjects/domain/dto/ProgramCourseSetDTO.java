package com.redhat.uxl.dataobjects.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.redhat.uxl.dataobjects.domain.types.TotaraCourseSetNextSetOperatorType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * The type Program course set dto.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramCourseSetDTO implements Serializable {

  private String name;
  private String summary;
  private Integer duration;
  private Integer sortOrder;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
  private Date dueDate;
  private Long id;

  private TotaraCourseSetNextSetOperatorType nextSetOperator;

  private List<ProgramCourseDTO> courses;
}
