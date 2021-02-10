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

/**
 * The type Featured search instance.
 */
@Data
@Entity
@Table(name = "featured_search_instances")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FeaturedSearchInstance extends AbstractAuditingEntity
    implements Serializable, Comparable<FeaturedSearchInstance> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(name = "featured_search_id")
  private Long featuredSearchId;
  @NotNull
  @Column(name = "instance_id")
  private Long instanceId;
  @NotNull
  @Column(name = "instance_type")
  private ProgramType instanceType;

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : IntegerUtils.ANSWER_TO_LIFE_THE_UNIVERSE_AND_EVERYTHING;
  }

  @Override
  public int compareTo(FeaturedSearchInstance i) {
    return id.compareTo(i.id);
  }
}
