package com.redhat.uxl.dataobjects.domain.dto;

import lombok.Data;

import java.util.Date;

/**
 * The type Personal program dto.
 */
@Data
public class PersonalProgramDTO extends TotaraProgramDTO {
  private Date programDueDate;
}
