package com.redhat.uxl.services.service.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * The type Featured search instance dto.
 */
@Data
public class FeaturedSearchInstanceDTO
    implements Serializable, Comparable<FeaturedSearchInstanceDTO> {

  private Long instanceId;
  private String instanceType;
  private String instanceName;

  @Override
  public int compareTo(FeaturedSearchInstanceDTO o) {
    return instanceId.compareTo(o.instanceId);
  }
}
