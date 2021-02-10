package com.redhat.uxl.dataobjects.domain;

import com.redhat.uxl.commonjava.utils.IntegerUtils;
import com.redhat.uxl.dataobjects.domain.types.DiscoveryProgramType;
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
 * The type Discovery program.
 */
@Data
@Entity
@Table(name = "discovery_programs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DiscoveryProgram extends AbstractAuditingEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "program_id")
  private Long programId;

  @Column(name = "active")
  private boolean active = false;

  @NotNull
  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private DiscoveryProgramType type;

  @Column(name = "discovery_program_text")
  private String discoveryProgramText;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DiscoveryProgram dp = (DiscoveryProgram) o;

    if (!id.equals(dp.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : IntegerUtils.ANSWER_TO_LIFE_THE_UNIVERSE_AND_EVERYTHING;
  }

}
