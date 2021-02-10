package com.redhat.uxl.webapp.web.rest.dto.account;

import lombok.Data;

/**
 * The type Change password dto.
 */
@Data
public class ChangePasswordDTO {
    private String currentPassword;
    private String newPassword;
}
