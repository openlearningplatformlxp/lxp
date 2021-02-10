package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;

/**
 * The type Learning locker i 18 name dto.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningLockerI18NameDTO implements Serializable {

  private String en;

}
