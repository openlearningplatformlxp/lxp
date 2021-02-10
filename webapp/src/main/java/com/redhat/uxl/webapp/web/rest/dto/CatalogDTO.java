package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.services.service.dto.ProgramItemDTO;
import java.io.Serializable;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * The type Catalog dto.
 */
@Data
public class CatalogDTO implements Serializable {
    private String name;
    private String description;
    private Page<ProgramItemDTO> learningPaths;
    private Page<ProgramItemDTO> courses;
}
