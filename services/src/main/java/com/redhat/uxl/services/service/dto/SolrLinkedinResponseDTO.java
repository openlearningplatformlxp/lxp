package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.uxl.dataobjects.domain.dto.LinkedinCourseDTO;
import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

import java.util.List;

/**
 * The type Solr linkedin response dto.
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolrLinkedinResponseDTO implements Searchable {

  @JsonProperty("elements")
  private List<LinkedinCourseDTO> items;
  private SolrLinkedinPagingDTO paging;
}
