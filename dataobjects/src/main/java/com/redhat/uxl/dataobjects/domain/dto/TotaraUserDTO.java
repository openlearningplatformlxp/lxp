package com.redhat.uxl.dataobjects.domain.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * The type Totara user dto.
 */
@Data
public class TotaraUserDTO {
  private String firstName;
  private String lastName;
  private String email;
  private String city;
  private String country;
  private String timezone;
  private String language;
  private Long id;
  private String certId;
  private String rhnId;

    /**
     * Gets display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
    return StringUtils.capitalize(firstName) + " " + StringUtils.capitalize(lastName);
  }
}
