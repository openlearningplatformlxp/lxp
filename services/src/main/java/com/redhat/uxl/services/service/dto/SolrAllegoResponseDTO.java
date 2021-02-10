package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.redhat.uxl.dataobjects.domain.dto.AllegoCourseDTO;
import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

import java.util.List;

/**
 * The type Solr allego response dto.
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolrAllegoResponseDTO implements Searchable {

  private List<AllegoCourseDTO> items;
}
