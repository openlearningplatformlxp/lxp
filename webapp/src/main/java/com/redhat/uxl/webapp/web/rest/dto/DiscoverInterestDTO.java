package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.services.service.dto.ProgramItemDTO;
import com.redhat.uxl.services.service.dto.TagDTO;
import java.io.Serializable;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * The type Discover interest dto.
 */
@Data
public class DiscoverInterestDTO implements Serializable {
    private TagDTO tag;
    private Page<ProgramItemDTO> programs;
}
