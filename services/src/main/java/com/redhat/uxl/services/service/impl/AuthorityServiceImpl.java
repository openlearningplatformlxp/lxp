package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.datalayer.repository.AuthorityRepository;
import com.redhat.uxl.datalayer.repository.PermissionRepository;
import com.redhat.uxl.dataobjects.domain.Authority;
import com.redhat.uxl.dataobjects.domain.Permission;
import com.redhat.uxl.dataobjects.domain.dto.PermissionKeyDTO;
import com.redhat.uxl.services.service.AuthorityService;
import com.redhat.uxl.services.service.search.SearchBuilder;
import com.redhat.uxl.services.service.search.SearchSpec;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Authority service.
 */
@Service
@Slf4j
@Transactional
public class AuthorityServiceImpl implements AuthorityService {
  @Inject
  private AuthorityRepository authorityRepository;

  @Inject
  private PermissionRepository permissionRepository;

  @Override
  @Timed
  @Transactional
  public Authority createAuthority(String name, List<PermissionKeyDTO> permissionKeyDTOS) {
    if (StrUtils.isBlank(name)) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST, "Role Name cannot be blank.");
    }

    Set<Permission> permissions = new HashSet<>();
    Authority authority = new Authority();

    authority.setName(name);

    if (permissionKeyDTOS != null && permissionKeyDTOS.size() > 0) {
      for (PermissionKeyDTO permissionKeyDTO : permissionKeyDTOS) {
        permissions.add(permissionKeyDTO.toPermission());
      }
    }

    authority.setPermissions(permissions);

    return authorityRepository.save(authority);
  }

  @Override
  @Timed
  @Transactional
  public void deleteAuthority(String authorityName) {
    if (StrUtils.isBlank(authorityName)) {
      throw new GeneralException(ErrorCodeGeneral.NOT_FOUND);
    }

    Authority authority = authorityRepository.findOne(authorityName);

    if (authority == null) {
      throw new GeneralException(ErrorCodeGeneral.NOT_FOUND);
    }

    authorityRepository.delete(authority);
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public Authority findByNameFetchPermissions(String roleName) {
    if (StrUtils.isBlank(roleName)) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST, "Role Name must be provided!");
    }

    return authorityRepository.findByNameFetchPermissions(roleName);
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public Page<Authority> findForPagedSearch(String searchOperation, String searchValue,
      Pageable pageable) {
    SearchBuilder<Authority> searchBuilder = new SearchBuilder<>(authorityRepository);

    if ("equal".equalsIgnoreCase(searchOperation) || "iequal".equalsIgnoreCase(searchOperation)) {
      searchValue = "ROLE_" + searchValue;
    } else if ("starts with".equalsIgnoreCase(searchOperation)
        || "istarts with".equalsIgnoreCase(searchOperation)) {
      searchValue = "ROLE_" + searchValue;
    }

    Page<Authority> authoritiesPage =
        searchBuilder.where(SearchSpec.valueOf("name", searchOperation, searchValue))
            .findForPagedSearch(pageable);

    return authoritiesPage;
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public List<Permission> getAllPermissions() {
    return permissionRepository.findAll();
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public List<Authority> getAvailableAuthorities() {
    return authorityRepository.findAll();
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public Set<Authority> toAuthoritySet(Set<String> authoritiesNames) {
    Set<Authority> authorities = new HashSet<>();

    if (authoritiesNames != null) {
      for (String name : authoritiesNames) {
        Authority authority = authorityRepository.findByName(name);

        if (authority != null) {
          authorities.add(authority);
        } else {
          throw new GeneralException(ErrorCodeGeneral.NOT_FOUND,
              "Authority " + name + " requested but does not exist.");
        }
      }
    }

    return authorities;
  }

  @Override
  @Timed
  @Transactional
  public Authority updateAuthorityFetchPermissions(String roleName,
      List<PermissionKeyDTO> permissionKeyDTOS) {
    if (StrUtils.isBlank(roleName)) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST, "Role Name cannot be blank.");
    }

    Set<Permission> permissions = new HashSet<>();
    Authority updateAuthority = authorityRepository.findByName(roleName);

    if (permissionKeyDTOS != null && permissionKeyDTOS.size() > 0) {
      for (PermissionKeyDTO permissionKeyDTO : permissionKeyDTOS) {
        permissions.add(permissionKeyDTO.toPermission());
      }
    }

    updateAuthority.setPermissions(permissions);

    return authorityRepository.save(updateAuthority);
  }
}
