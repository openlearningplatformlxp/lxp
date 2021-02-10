package com.redhat.uxl.services.service.demo.dto;

import lombok.Data;

import java.util.List;

/**
 * The type Parent tag dto.
 */
@Data
public class ParentTagDTO {
    /**
     * The Id.
     */
    Long id;
    /**
     * The Subtags.
     */
    List<Long> subtags;
}
