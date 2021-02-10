package com.redhat.uxl.webapp.web.rest.dto;

import java.util.Set;
import lombok.Data;

/**
 * The type Cms blocks request dto.
 */
@Data
public class CmsBlocksRequestDTO {
    private Set<String> keys;
}
