package com.redhat.uxl.services.service.demo.dto;

import lombok.Data;

import java.util.List;

/**
 * The type Program audience dto.
 */
@Data
public class ProgramAudienceDTO {
    /**
     * The Program id.
     */
    Long ProgramId;
    /**
     * The User ids.
     */
    List<Long> UserIds;
}
