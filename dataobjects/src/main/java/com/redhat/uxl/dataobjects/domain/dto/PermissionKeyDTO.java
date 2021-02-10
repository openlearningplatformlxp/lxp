package com.redhat.uxl.dataobjects.domain.dto;

import com.redhat.uxl.dataobjects.domain.Permission;
import com.redhat.uxl.dataobjects.domain.types.PermissionOperation;
import lombok.Data;

import java.io.Serializable;

/**
 * The type Permission key dto.
 */
@Data
public class PermissionKeyDTO implements Serializable {
  private String groupName;
  private String permissionName;
  private PermissionOperation operation;

    /**
     * To permission permission.
     *
     * @return the permission
     */
    public Permission toPermission() {
    Permission permission = new Permission();

    permission.setGroupName(getGroupName());
    permission.setPermissionName(getPermissionName());
    permission.setOperation(getOperation());

    return permission;
  }
}
