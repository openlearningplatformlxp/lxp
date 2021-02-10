package com.redhat.uxl.webapp.web.rest.dto;

import lombok.Data;

/**
 * The type Paged search order dto.
 */
@Data
public class PagedSearchOrderDTO {
    private String field;
    private String direction;

    /**
     * Value of paged search order dto.
     *
     * @param field     the field
     * @param direction the direction
     * @return the paged search order dto
     */
    public static PagedSearchOrderDTO valueOf(String field, String direction) {
        PagedSearchOrderDTO dto = new PagedSearchOrderDTO();

        dto.setField(field);
        dto.setDirection(direction);

        return dto;
    }
}
