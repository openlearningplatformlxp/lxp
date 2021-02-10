package com.redhat.uxl.dataobjects.domain;

import com.redhat.uxl.dataobjects.domain.types.AssetStoreType;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * The type Asset.
 */
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@DiscriminatorColumn(name = "asset_store_type", discriminatorType = DiscriminatorType.STRING,
    length = 128)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "asset")
public class Asset extends AbstractAuditingEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "asset_store_type", insertable = false, length = 128, nullable = false,
      updatable = false)
  @Enumerated(EnumType.STRING)
  @NotNull
  private AssetStoreType assetStoreType;

  @OneToOne
  @JoinColumn(name = "asset_type_id", nullable = false)
  @NotNull
  private AssetType assetType;

  @OneToOne
  @JoinColumn(name = "asset_subtype_id", nullable = false)
  @NotNull
  private AssetSubtype assetSubtype;

  @Column(name = "name", length = 255, nullable = false)
  @NotNull
  @Size(max = 255)
  private String name;

  @Column(name = "filename", length = 255, nullable = false)
  @NotNull
  @Size(max = 255)
  private String filename;

  @Column(name = "path", length = 4096)
  @Size(max = 4096)
  private String path;

  @Column(name = "description", length = 4096)
  @Size(max = 4096)
  private String description;
}
