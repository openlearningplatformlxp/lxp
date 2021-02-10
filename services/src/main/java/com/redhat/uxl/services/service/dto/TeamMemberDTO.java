package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Team member dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberDTO {
  private String firstName;
  private String lastName;
  private String city;
  private String state;
  private String country;
  private int paths;
  private int courses;
  private int activities;
  private String avatar;
  private Long userId;
  private boolean manager;
  private Integer cecredits;

    /**
     * Gets display name.
     *
     * @return the display name
     */
    @JsonProperty("displayName")
  public String getDisplayName() {
    return String.format("%s %s", firstName, lastName);
  }
}
