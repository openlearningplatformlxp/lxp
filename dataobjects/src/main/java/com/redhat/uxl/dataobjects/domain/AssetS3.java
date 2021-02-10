package com.redhat.uxl.dataobjects.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The type Asset s 3.
 */
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@DiscriminatorValue("S3")
@Entity
@Table(name = "asset_s3")
public class AssetS3 extends Asset implements Serializable {
  @Column(name = "content_type", length = 64)
  @Size(max = 64)
  private String contentType;
}
