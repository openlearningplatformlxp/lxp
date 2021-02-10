package com.redhat.uxl.dataobjects.domain;

import com.redhat.uxl.commonjava.utils.IntegerUtils;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

/**
 * The type Person access.
 */
@Data
@Entity
@Table(name = "person_last_access")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PersonAccess extends AbstractAuditingEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(name = "persontotaraid")
  private Long personTotaraId;

  @NotNull
  @Column(name = "type")
  private ProgramType type;

  @NotNull
  @Column(name = "itemid")
  private Long itemId;

  @Column(name = "access")
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
  private LocalDateTime access;

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : IntegerUtils.ANSWER_TO_LIFE_THE_UNIVERSE_AND_EVERYTHING;
  }
}
