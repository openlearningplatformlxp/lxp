package com.redhat.uxl.services.service.demo.dto;

import lombok.Data;

/**
 * The type User activity dto.
 */
@Data
public class UserActivityDTO {
    /**
     * The User id.
     */
    Long userId;
    /**
     * The Activity id.
     */
    Long activityId;
    /**
     * The Status.
     */
    Boolean status;
}
