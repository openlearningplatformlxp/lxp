package com.redhat.uxl.services.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Profile preferences dto.
 */
@Data
public class ProfilePreferencesDTO implements Serializable {

  private String pictureUrl;
  private List<TagDTO> interests = new ArrayList<>();
  private List<TagDTO> allRoles = new ArrayList<>();
  private List<TagDTO> roles = new ArrayList<>();
  private NotificationPreferencesDTO notifications = new NotificationPreferencesDTO();
}
