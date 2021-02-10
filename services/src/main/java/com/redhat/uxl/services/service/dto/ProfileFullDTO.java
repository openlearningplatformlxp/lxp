package com.redhat.uxl.services.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Profile full dto.
 */
@Data
public class ProfileFullDTO extends ProfileDTO implements Serializable {

  private String emailAddress;
  private String language;
  private String timezone;

  private List<ProfileDTO> managers = new ArrayList<>();
}
