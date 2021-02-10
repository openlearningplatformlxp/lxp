package com.redhat.uxl.dataobjects.domain;

import com.redhat.uxl.dataobjects.domain.types.LearningLockerJobExecutionDetailResultType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The type Learning locker job execution detail.
 */
@Data
@Entity
@Table(name = "job_execution_learning_locker_detail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LearningLockerJobExecutionDetail extends AbstractAuditingEntity
    implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne()
  @JoinColumn(nullable = false, name = "job_execution_learning_locker_id")
  private LearningLockerJobExecution jobExecutionLearningLocker;

  @Column(nullable = false, name = "type")
  private LearningLockerJobExecutionDetailResultType type;

  @Column(nullable = false, name = "url_key")
  private String urlKey;

  @Column(nullable = false, name = "url")
  private String url;

  @Column(nullable = false, name = "author")
  private String author;

  @Column(nullable = false, name = "verb")
  private String verb;

  @Column(nullable = true, name = "timestamp")
  private String timestamp;

  @Column(name = "person_id")
  private Long personId;

}
