package com.redhat.uxl.webapp.web.rest.dto;

import lombok.Data;

/**
 * The type Send message dto.
 */
@Data
public class SendMessageDTO {
    private String title;
    private String message;
    private Long programId;
}
