package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.uxl.dataobjects.domain.types.ContentSourceType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import java.io.IOException;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Search input filters dto.
 */
@Slf4j
@Data
public class SearchInputFiltersDTO {

  private List<ProgramType> type;
  private String status;
  private String topic;
  private Long role;
  private List<ContentSourceType> cms;
  private List<Integer> delivery;
  private String skillLevels;
  private String languages;
  private String country;
  private String city;
  private boolean adminSearch;

  public String toString() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      return mapper.writeValueAsString(this);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      return "";
    }
  }
}
