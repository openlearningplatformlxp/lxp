package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.services.service.dto.TagDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Parent tag dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentTagDTO {
    private TagDTO parentTag;
    private List<TagDTO> childTags;
}
