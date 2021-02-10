package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.dataobjects.domain.Permission;
import com.redhat.uxl.dataobjects.domain.types.PermissionOperation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The type Permission dto.
 */
@Data
public class PermissionDTO {
    private String groupName;
    private String permissionName;
    private PermissionOperation operation;

    private boolean selected;

    /**
     * Value of permission dto.
     *
     * @param permission the permission
     * @return the permission dto
     */
    public static PermissionDTO valueOf(Permission permission) {
        if (permission == null) {
            return new PermissionDTO();
        }

        PermissionDTO dto = new PermissionDTO();

        dto.setGroupName(permission.getGroupName());
        dto.setPermissionName(permission.getPermissionName());
        dto.setOperation(permission.getOperation());

        return dto;
    }

    /**
     * Value of list.
     *
     * @param permissions the permissions
     * @return the list
     */
    public static List<PermissionDTO> valueOf(Set<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return new ArrayList<>(0);
        }

        List<PermissionDTO> dtos = new ArrayList<>(permissions.size());

        for (Permission permission : permissions) {
            dtos.add(PermissionDTO.valueOf(permission));
        }

        return dtos;
    }
}
