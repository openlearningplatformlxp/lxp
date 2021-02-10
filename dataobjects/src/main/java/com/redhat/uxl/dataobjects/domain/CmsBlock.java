package com.redhat.uxl.dataobjects.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * The type Cms block.
 */
@Data
@Entity
@Table(name = "cms_block")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CmsBlock extends AbstractAuditingEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Size(max = 128)
  @Column(name = "key", length = 128)
  private String key;

  @Size(max = 32)
  @Column(name = "name", length = 32)
  private String name;

  @Size(max = 1024)
  @Column(name = "description", length = 1024)
  private String description;

  @Column(name = "content", columnDefinition = "CLOB")
  @Lob
  @Type(type = "org.hibernate.type.TextType")
  private String content;
}
