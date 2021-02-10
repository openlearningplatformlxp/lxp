package com.redhat.uxl.dataobjects.domain;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

/**
 * The type Persistent audit event.
 */
@Data
@Entity
@Table(name = "persistent_audit_event")
public class PersistentAuditEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "event_id")
  private Long id;

  @NotNull
  @Column(nullable = false)
  private String principal;

  @Column(name = "event_date")
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
  private LocalDateTime auditEventDate;

  @Column(name = "event_type")
  private String auditEventType;

  @ElementCollection
  @MapKeyColumn(name = "name")
  @Column(name = "value")
  @CollectionTable(name = "persistent_audit_event_data",
      joinColumns = @JoinColumn(name = "event_id"))
  private Map<String, String> data = new HashMap<>();
}
