package com.redhat.uxl.services.service.dto;

import com.redhat.uxl.dataobjects.domain.types.WikipageTreeType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Wikipage tree node dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WikipageTreeNodeDTO {

  private Long id;
  private WikipageTreeType type;
  private String slug;
  private List<WikipageTreeNodeDTO> columns;

}
