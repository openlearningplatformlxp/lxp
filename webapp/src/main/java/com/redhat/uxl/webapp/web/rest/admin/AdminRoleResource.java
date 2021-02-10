package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.dataobjects.domain.Authority;
import com.redhat.uxl.dataobjects.domain.Permission;
import com.redhat.uxl.services.service.AuthorityService;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import com.redhat.uxl.webapp.web.rest.dto.AuthorityDTO;
import com.redhat.uxl.webapp.web.rest.dto.PagedSearchDTO;
import com.redhat.uxl.webapp.web.rest.dto.admin.AddEditUserRolePageDTO;
import com.redhat.uxl.webapp.web.rest.dto.admin.RoleUpsertDTO;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Admin role resource.
 */
@RestController
@RequestMapping(value = "/api/admin/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminRoleResource extends BaseResource {
    private static final String ROLE_PREFIX = "ROLE_";

    @Inject
    private AuthorityService authorityService;

    /**
     * Create role authority dto.
     *
     * @param roleUpsertDTO the role upsert dto
     * @return the authority dto
     */
    @ApiOperation(value = "Create a new Role.", notes = "<p>Create a new Role.</p>")
    @PreAuthorize("hasAuthority('USERS_ROLE_CREATE')")
    @RequestMapping(method = RequestMethod.PUT)
    @Timed
    public AuthorityDTO createRole(@RequestBody RoleUpsertDTO roleUpsertDTO) {
        Authority authority = authorityService.createAuthority(roleUpsertDTO.getRoleName(),
                roleUpsertDTO.getPermissionKeyDTOS());

        return AuthorityDTO.valueOf(authority, false);
    }

    /**
     * Delete role.
     *
     * @param roleName the role name
     */
    @ApiOperation(value = "Delete the given person.", notes = "<p>Delete the given person.</p>")
    @PreAuthorize("hasAuthority('USERS_ROLE_DELETE')")
    @RequestMapping(value = "/{roleName}", method = RequestMethod.DELETE)
    @Timed
    public void deleteRole(@PathVariable String roleName) {
        authorityService.deleteAuthority(roleName);
    }

    /**
     * Gets page upsert.
     *
     * @return the page upsert
     */
    @ApiOperation(value = "Get Role Create page.", notes = "<p>Get Role Create page.</p>")
    @PreAuthorize("hasAuthority('USERS_ROLE_CREATE')")
    @RequestMapping(value = "/page/upsert", method = RequestMethod.GET)
    @Timed
    public AddEditUserRolePageDTO getPageUpsert() {
        List<Permission> permissions = authorityService.getAllPermissions();

        return AddEditUserRolePageDTO.valueOf(permissions);
    }

    /**
     * Gets page upsert.
     *
     * @param mangledRoleName the mangled role name
     * @return the page upsert
     */
    @ApiOperation(value = "Get Role View/Edit page.", notes = "<p>Get Role View/Edit page.</p>")
    @PreAuthorize("hasAnyAuthority('USERS_ROLE_VIEW', 'USERS_ROLE_CREATE', 'USERS_ROLE_UPDATE', 'USERS_ROLE_DELETE')")
    @RequestMapping(value = "/page/upsert/{mangledRoleName}", method = RequestMethod.GET)
    @Timed
    public AddEditUserRolePageDTO getPageUpsert(@PathVariable String mangledRoleName) {
        List<Permission> permissions = authorityService.getAllPermissions();
        Authority authority = authorityService.findByNameFetchPermissions(ROLE_PREFIX + mangledRoleName);

        if (authority == null) {
            throw new GeneralException(ErrorCodeGeneral.NOT_FOUND, "Role not found.");
        }

        return AddEditUserRolePageDTO.valueOf(authority, permissions);
    }

    /**
     * Update role authority dto.
     *
     * @param roleName      the role name
     * @param roleUpsertDTO the role upsert dto
     * @return the authority dto
     */
    @ApiOperation(value = "Update role.", notes = "<p>Update role.</p>")
    @PreAuthorize("hasAuthority('USERS_ROLE_UPDATE')")
    @RequestMapping(value = "/{roleName}", method = RequestMethod.PUT)
    @Timed
    public AuthorityDTO updateRole(@PathVariable String roleName, @RequestBody RoleUpsertDTO roleUpsertDTO) {
        if (roleUpsertDTO == null || roleUpsertDTO.getRoleName() == null) {
            throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST);
        }

        Authority authority = authorityService.updateAuthorityFetchPermissions(roleUpsertDTO.getRoleName(),
                roleUpsertDTO.getPermissionKeyDTOS());

        return AuthorityDTO.valueOf(authority, true);
    }

    /**
     * Search roles page.
     *
     * @param pagedSearchDTO the paged search dto
     * @return the page
     */
    @ApiOperation(value = "Search for Roles.", notes = "<p>Search for Roles.</p>")
    @PreAuthorize("hasAuthority('USERS_ROLE_VIEW')")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @Timed
    Page<AuthorityDTO> searchRoles(@RequestBody PagedSearchDTO pagedSearchDTO) {
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "name");

        Pageable pageable = pagedSearchDTO.getPageable(order1, null);

        return AuthorityDTO.valueOf(authorityService.findForPagedSearch(pagedSearchDTO.getSearchOperation(),
                pagedSearchDTO.getSearchValue(), pageable));
    }
}
