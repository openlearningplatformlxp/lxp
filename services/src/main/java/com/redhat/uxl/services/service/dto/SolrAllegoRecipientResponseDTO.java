package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.redhat.uxl.dataobjects.domain.AllegoContentRecipient;
import com.redhat.uxl.dataobjects.domain.Searchable;
import java.util.List;
import lombok.Data;

/**
 * The type Solr allego recipient response dto.
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolrAllegoRecipientResponseDTO implements Searchable {

  private List<AllegoContentRecipient> items;
}
