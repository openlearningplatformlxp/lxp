package com.redhat.uxl.dataobjects.domain;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The type Abstract auditing entity.
 */
@Data
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity {

  @CreatedBy
  @NotNull
  @Column(name = "created_by", nullable = false, length = 50, updatable = false)
  private String createdBy;

  @CreatedDate
  @NotNull
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  @Column(name = "created_date", nullable = false)
  private DateTime createdDate = DateTime.now();

  @LastModifiedBy
  @Column(name = "last_modified_by", length = 50)
  private String lastModifiedBy;

  @LastModifiedDate
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  @Column(name = "last_modified_date")
  private DateTime lastModifiedDate = DateTime.now();

}
