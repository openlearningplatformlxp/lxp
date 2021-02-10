package com.redhat.uxl.dataobjects.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The type Allego content recipient.
 */
@Data
@Entity
@Table(name = "allego_content_recipient")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AllegoContentRecipient extends AbstractAuditingEntity
    implements Serializable, Searchable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, name = "content_id")
  private Long contentId;

  @JsonProperty(value = "Groups")
  @Column(nullable = false, name = "groups")
  private String groups;

}
