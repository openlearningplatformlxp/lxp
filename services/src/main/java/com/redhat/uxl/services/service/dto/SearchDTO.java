package com.redhat.uxl.services.service.dto;

import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * The type Search dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDTO implements Serializable {
    private Set<ProgramItemDTO> featuredItems;
    private Page<ProgramItemDTO> items;

    /**
     * Instantiates a new Search dto.
     *
     * @param items the items
     */
    public SearchDTO(Page<ProgramItemDTO> items) {
        this.items = items;
    }
}
