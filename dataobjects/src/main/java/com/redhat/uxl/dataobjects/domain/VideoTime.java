package com.redhat.uxl.dataobjects.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * The type Video time.
 */
@Data
@Entity
@Table(name = "video_time")
public class VideoTime extends AbstractAuditingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  private Long personId;

  @NotNull
  private Long moduleId;

  @NotNull
  private Long courseId;

  @NotNull
  private Double time;
}
