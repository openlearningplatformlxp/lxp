package com.redhat.uxl.dataobjects.domain;

import com.redhat.uxl.commonjava.utils.IntegerUtils;
import com.redhat.uxl.dataobjects.domain.types.IndexStatusType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

/**
 * The type Index data.
 */
@Data
@Entity
@Table(name = "index_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IndexData extends AbstractAuditingEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(name = "run_by")
  private Long runBy;

  @Column(name = "started_on")
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
  private LocalDateTime startedOn;

  @NotNull
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private IndexStatusType status = IndexStatusType.STARTED;

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : IntegerUtils.ANSWER_TO_LIFE_THE_UNIVERSE_AND_EVERYTHING;
  }
}
