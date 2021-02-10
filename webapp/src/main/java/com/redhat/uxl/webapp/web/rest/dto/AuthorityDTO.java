package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.dataobjects.domain.Authority;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * The type Authority dto.
 */
@Data
public class AuthorityDTO extends AuditingDTO {
    private String name;
    private boolean deletable = false;
    private List<PermissionDTO> permissions;

    /**
     * Value of authority dto.
     *
     * @param authority          the authority
     * @param includePermissions the include permissions
     * @return the authority dto
     */
    public static AuthorityDTO valueOf(Authority authority, boolean includePermissions) {
        if (authority == null) {
            return new AuthorityDTO();
        }

        AuthorityDTO dto = new AuthorityDTO();

        dto.setName(authority.getName());
        dto.setDeletable(!ArrayUtils.contains(AuthoritiesConstants.SYSTEM_ROLES, authority.getName()));
        dto.setAuditing(authority);

        if (includePermissions) {
            dto.setPermissions(PermissionDTO.valueOf(authority.getPermissions()));
        }

        return dto;
    }

    /**
     * Value of page.
     *
     * @param authorityPage the authority page
     * @return the page
     */
    public static Page<AuthorityDTO> valueOf(Page<Authority> authorityPage) {
        if (authorityPage == null || !authorityPage.hasContent()) {
            return new PageImpl<>(new ArrayList<>(0));
        }

        List<AuthorityDTO> dtos = new ArrayList<>(authorityPage.getNumberOfElements());

        for (Authority authority : authorityPage.getContent()) {
            dtos.add(AuthorityDTO.valueOf(authority, false));
        }

        Pageable pageable = PageRequest.of(authorityPage.getNumber(), authorityPage.getSize(), authorityPage.getSort());

        return new PageImpl<>(dtos, pageable, authorityPage.getTotalElements());
    }

    // Private Helper Methods
}
