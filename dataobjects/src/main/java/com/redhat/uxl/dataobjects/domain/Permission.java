package com.redhat.uxl.dataobjects.domain;

import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.dataobjects.domain.dto.PermissionKeyDTO;
import com.redhat.uxl.dataobjects.domain.types.PermissionOperation;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * The type Permission.
 */
@Data
@Entity
@IdClass(PermissionKeyDTO.class)
@Table(name = "permission")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Permission implements Serializable {

  @Column(length = 50)
  @Id
  @Size(min = 0, max = 50)
  private String groupName; // e.g. ADMIN, CMS, USER, etc...

  @Column(length = 50)
  @Id
  @Size(min = 0, max = 50)
  private String permissionName; // e.g. PERSON, NOTIFICATION, etc...

  @Column(length = 50)
  @Enumerated(EnumType.STRING)
  @Id
  @Size(min = 0, max = 50)
  private PermissionOperation operation; // e.g. VIEW, ADD, EDIT, DELETE, etc...

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Permission permission = (Permission) o;

    if (StrUtils.isAnyBlank(getGroupName(), getPermissionName()) || getOperation() == null) {
      return false;
    } else if (StrUtils.isAnyBlank(permission.getGroupName(), permission.getPermissionName())
        || permission.getOperation() == null) {
      return false;
    }

    return (getGroupName().equals(permission.getGroupName())
        && getPermissionName().equals(permission.getPermissionName())
        && getOperation().equals(permission.getOperation()));
  }
}
