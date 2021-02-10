package com.redhat.uxl.dataobjects.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The type Spring session.
 */
@Data
@Entity
@Table(name = "spring_session")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SpringSession implements Serializable {
  @Column(name = "session_id", nullable = false, columnDefinition = "char")
  @Id
  private String sessionId;

  @Column(name = "creation_time", nullable = false)
  @NotNull
  private long creationTime;

  @Column(name = "last_access_time", nullable = false)
  @NotNull
  private long lastAccessTime;

  @Column(name = "max_inactive_interval")
  private int maxInactiveInterval; // in seconds

  @Column(name = "principal_name", length = 100)
  private String principalName;
}
