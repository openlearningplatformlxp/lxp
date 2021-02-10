package com.redhat.uxl.services.service.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Search filter dto.
 */
@Data
public class SearchFilterDTO {

  private List<LocationDTO> locations = new ArrayList();
  private List<TagDTO> deliveries = new ArrayList<>();
  private List<TagDTO> skillLevels = new ArrayList<>();
  private List<TagDTO> languages = new ArrayList<>();

}
