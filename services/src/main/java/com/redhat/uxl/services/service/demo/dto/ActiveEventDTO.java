package com.redhat.uxl.services.service.demo.dto;

import lombok.Data;

import java.util.List;

/**
 * The type Active event dto.
 */
@Data
public class ActiveEventDTO {
    /**
     * The Event ids.
     */
    List<Long> eventIds;
}
