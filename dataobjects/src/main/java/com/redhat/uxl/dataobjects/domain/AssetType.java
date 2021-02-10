package com.redhat.uxl.dataobjects.domain;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * The type Asset type.
 */
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@Entity
@Table(name = "asset_type")
public class AssetType implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name", length = 64, nullable = false)
  @NotNull
  @Size(max = 64)
  private String name;

  @Column(name = "enabled", nullable = false)
  private boolean enabled = true;

    /**
     * The Subtypes.
     */
    @JoinColumn(name = "asset_type_id")
  @OneToMany
  @OrderBy("name")
  List<AssetSubtype> subtypes;
}
