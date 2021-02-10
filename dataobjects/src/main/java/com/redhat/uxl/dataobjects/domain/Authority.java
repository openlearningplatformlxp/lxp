package com.redhat.uxl.dataobjects.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Authority.
 */
@Data
@Entity
@Table(name = "authority")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Authority extends AbstractAuditingEntity implements Serializable {

  @Id
  @Column(length = 50)
  @NotNull
  @Pattern(regexp = "^ROLE_[A-Z_]*$")
  @Size(min = 0, max = 50)
  private String name;

  @JsonIgnore
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "authority_permission",
      joinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")},
      inverseJoinColumns = {
          @JoinColumn(name = "permission_group_name", referencedColumnName = "groupName"),
          @JoinColumn(name = "permission_permission_name", referencedColumnName = "permissionName"),
          @JoinColumn(name = "permission_operation", referencedColumnName = "operation")})
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Permission> permissions = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Authority authority = (Authority) o;

    if (name != null ? !name.equals(authority.name) : authority.name != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Authority{" + "name='" + name + '\'' + "}";
  }
}
