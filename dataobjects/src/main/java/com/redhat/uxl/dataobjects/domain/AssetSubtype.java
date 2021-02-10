package com.redhat.uxl.dataobjects.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The type Asset subtype.
 */
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@Entity
@Table(name = "asset_subtype")
public class AssetSubtype implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name", length = 64, nullable = false)
  @NotNull
  @Size(max = 64)
  private String name;

  @Column(name = "default_content_type", length = 64)
  @Size(max = 64)
  private String defaultContentType;

  @Column(name = "is_text", nullable = false)
  private boolean text = false;

  @Column(name = "enabled", nullable = false)
  private boolean enabled = true;
}
