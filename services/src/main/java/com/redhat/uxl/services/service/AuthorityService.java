package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.Authority;
import com.redhat.uxl.dataobjects.domain.Permission;
import com.redhat.uxl.dataobjects.domain.dto.PermissionKeyDTO;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Authority service.
 */
public interface AuthorityService {
    /**
     * Create authority authority.
     *
     * @param roleName          the role name
     * @param permissionKeyDTOS the permission key dtos
     * @return the authority
     */
    Authority createAuthority(String roleName, List<PermissionKeyDTO> permissionKeyDTOS);

    /**
     * Delete authority.
     *
     * @param authorityName the authority name
     */
    void deleteAuthority(String authorityName);

    /**
     * Find by name fetch permissions authority.
     *
     * @param roleName the role name
     * @return the authority
     */
    Authority findByNameFetchPermissions(String roleName);

    /**
     * Find for paged search page.
     *
     * @param searchOperation the search operation
     * @param searchValue     the search value
     * @param pageable        the pageable
     * @return the page
     */
    Page<Authority> findForPagedSearch(String searchOperation, String searchValue, Pageable pageable);

    /**
     * Gets all permissions.
     *
     * @return the all permissions
     */
    List<Permission> getAllPermissions();

    /**
     * Gets available authorities.
     *
     * @return the available authorities
     */
    List<Authority> getAvailableAuthorities();

    /**
     * To authority set set.
     *
     * @param authoritiesNames the authorities names
     * @return the set
     */
    Set<Authority> toAuthoritySet(Set<String> authoritiesNames);

    /**
     * Update authority fetch permissions authority.
     *
     * @param roleName          the role name
     * @param permissionKeyDTOS the permission key dtos
     * @return the authority
     */
    Authority updateAuthorityFetchPermissions(String roleName, List<PermissionKeyDTO> permissionKeyDTOS);
}
