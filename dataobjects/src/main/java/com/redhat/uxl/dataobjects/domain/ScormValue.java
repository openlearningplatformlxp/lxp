package com.redhat.uxl.dataobjects.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * The type Scorm value.
 */
@Data
@Entity
@Table(name = "scorm_value")
public class ScormValue {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  private Long personId;

  @NotNull
  private Long moduleId;

  @Length(max = 250)
  @NotNull
  private String key;

  @Length(max = 4096)
  @NotNull
  private String value;

  @NotNull
  private Long attempt = 1l;
}
