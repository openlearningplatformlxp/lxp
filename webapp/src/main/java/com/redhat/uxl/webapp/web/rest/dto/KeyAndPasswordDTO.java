package com.redhat.uxl.webapp.web.rest.dto;

import lombok.Data;

/**
 * The type Key and password dto.
 */
@Data
public class KeyAndPasswordDTO {
    private String key;
    private String newPassword;
}
