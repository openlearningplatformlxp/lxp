package com.redhat.uxl.dataobjects.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The type Asset db.
 */
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@DiscriminatorValue("DB")
@Entity
@Table(name = "asset_db")
public class AssetDb extends Asset implements Serializable {
  @Column(name = "content")
  @Lob
  @Size(max = 1048576) // 1 meg max!
  private byte[] content;

  @Column(name = "content_type", length = 64)
  @Size(max = 64)
  private String contentType;
}
