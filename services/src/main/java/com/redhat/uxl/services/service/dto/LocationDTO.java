package com.redhat.uxl.services.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Location dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO implements Comparable<LocationDTO> {

  private String country;
  private List<String> city;

  @Override
  public int compareTo(LocationDTO o) {
    return this.country.compareTo(o.country);
  }
}
