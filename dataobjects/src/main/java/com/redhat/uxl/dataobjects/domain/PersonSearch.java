package com.redhat.uxl.dataobjects.domain;

import com.redhat.uxl.commonjava.utils.IntegerUtils;
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
 * The type Person search.
 */
@Data
@Entity
@Table(name = "person_search")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PersonSearch extends AbstractAuditingEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(name = "persontotaraid")
  private Long personTotaraId;

  @NotNull
  @Column(name = "search_term")
  private String searchTerm;

  @NotNull
  @Column(name = "filters")
  private String filters;

  @NotNull
  @Column(name = "results")
  private Long results;

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : IntegerUtils.ANSWER_TO_LIFE_THE_UNIVERSE_AND_EVERYTHING;
  }
}
