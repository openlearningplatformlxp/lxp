package com.redhat.uxl.webapp.web.rest.dto.admin;

import com.redhat.uxl.dataobjects.domain.Authority;
import com.redhat.uxl.dataobjects.domain.Permission;
import com.redhat.uxl.webapp.web.rest.dto.AuthorityDTO;
import com.redhat.uxl.webapp.web.rest.dto.PermissionDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.Data;

/**
 * The type Add edit user role page dto.
 */
@Data
public class AddEditUserRolePageDTO {
    private AuthorityDTO authority;
    private List<PermissionGroup> permissionGroups = new ArrayList<>();

    /**
     * Add permission.
     *
     * @param permission the permission
     */
    public void addPermission(Permission permission) {
        PermissionGroup permissionGroup;

        if (permissionGroups.size() > 0
                && permissionGroups.get(permissionGroups.size() - 1).getGroupName().equals(permission.getGroupName())) {
            permissionGroup = permissionGroups.get(permissionGroups.size() - 1);
        } else {
            permissionGroup = new PermissionGroup();
            permissionGroup.setGroupName(permission.getGroupName());
            permissionGroups.add(permissionGroup);
        }

        PermissionName permissionName;

        if (permissionGroup.getPermissionNames().size() > 0
                && permissionGroup.getPermissionNames().get(permissionGroup.getPermissionNames().size() - 1)
                        .getPermissionName().equals(permission.getPermissionName())) {
            permissionName = permissionGroup.getPermissionNames().get(permissionGroup.getPermissionNames().size() - 1);
        } else {
            permissionName = new PermissionName();
            permissionName.setPermissionName(permission.getPermissionName());
            permissionGroup.getPermissionNames().add(permissionName);
        }

        permissionName.getPermissions().add(PermissionDTO.valueOf(permission));
    }

    /**
     * Value of add edit user role page dto.
     *
     * @param permissions the permissions
     * @return the add edit user role page dto
     */
    public static AddEditUserRolePageDTO valueOf(List<Permission> permissions) {
        return valueOf(null, permissions);
    }

    /**
     * Value of add edit user role page dto.
     *
     * @param authority   the authority
     * @param permissions the permissions
     * @return the add edit user role page dto
     */
    public static AddEditUserRolePageDTO valueOf(Authority authority, List<Permission> permissions) {
        AddEditUserRolePageDTO dto = new AddEditUserRolePageDTO();

        dto.setAuthority(AuthorityDTO.valueOf(authority, true));

        Collections.sort(permissions, new Comparator<Permission>() {
            public int compare(Permission o1, Permission o2) {
                if (!o1.getGroupName().equals(o2.getGroupName())) {
                    return o1.getGroupName().compareTo(o2.getGroupName());
                }

                if (!o1.getPermissionName().equals(o2.getPermissionName())) {
                    return o1.getPermissionName().compareTo(o2.getPermissionName());
                }

                return o1.getOperation().getSequence() - o2.getOperation().getSequence();
            }
        });

        for (Permission permission : permissions) {
            dto.addPermission(permission);
        }

        return dto;
    }

    @Data
    private class PermissionGroup {
        private String groupName;
        private List<PermissionName> permissionNames = new ArrayList<>();
    }

    @Data
    private class PermissionName {
        private String permissionName;
        private List<PermissionDTO> permissions = new ArrayList<>();
    }
}
