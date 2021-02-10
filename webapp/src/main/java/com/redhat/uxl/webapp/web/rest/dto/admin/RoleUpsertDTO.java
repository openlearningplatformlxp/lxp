package com.redhat.uxl.webapp.web.rest.dto.admin;

import com.redhat.uxl.dataobjects.domain.dto.PermissionKeyDTO;
import java.util.List;
import lombok.Data;

/**
 * The type Role upsert dto.
 */
@Data
public class RoleUpsertDTO {
    private String roleName;
    private List<PermissionKeyDTO> permissionKeyDTOS;
}
