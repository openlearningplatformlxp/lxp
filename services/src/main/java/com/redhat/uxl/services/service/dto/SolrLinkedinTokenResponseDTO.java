package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

/**
 * The type Solr linkedin token response dto.
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolrLinkedinTokenResponseDTO implements Searchable {

  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("expires_in")
  private Long expiresIn;
}
