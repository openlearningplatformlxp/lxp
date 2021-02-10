package com.redhat.uxl.dataobjects.domain;

import com.redhat.uxl.dataobjects.domain.types.SystemMutexType;
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

/**
 * The type System mutex.
 */
@Data
@Entity
@Table(name = "system_mutex")
public class SystemMutex extends AbstractAuditingEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "mutex_type", length = 50)
  @Enumerated(EnumType.STRING)
  @NotNull
  private SystemMutexType mutexType;

  @Column(name = "acquired", nullable = false)
  private boolean acquired = false;

  @Column(name = "acquired_by_system", length = 256)
  private String acquiredBySystem;
}
