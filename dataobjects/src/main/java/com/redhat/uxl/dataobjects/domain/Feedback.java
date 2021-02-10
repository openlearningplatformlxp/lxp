package com.redhat.uxl.dataobjects.domain;

import com.redhat.uxl.dataobjects.domain.types.FeedbackType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The type Feedback.
 */
@Data
@Entity
@Table(name = "feedback")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Feedback extends AbstractAuditingEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private FeedbackType type;

  @NotNull
  @Column(name = "person_id")
  private Long totaraId;

  @NotNull
  @Column(name = "message")
  private String message;

  @NotNull
  @Column(name = "url")
  private String url;

  @Column(name = "business_line")
  private String businessLine;

  @Column(name = "region")
  private String region;

  @Column(name = "job_title")
  private String jobTitle;

  @Column(name = "email")
  private String email;
}
