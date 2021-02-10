package com.redhat.uxl.webapp.web.rest.dto.admin;

import com.redhat.uxl.dataobjects.domain.Authority;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.PersonActivationToken;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * The type Add edit user page dto.
 */
@Data
public class AddEditUserPageDTO {
    private DetailedPersonDTO user;
    private List<String> availableAuthorities; // TODO: SAC: should this be renamed to
                                               // availableAuthoritiesNames
    private boolean appSecurityImpersonateEnabled;

    /**
     * Value of add edit user page dto.
     *
     * @param availableAuthorities          the available authorities
     * @param appSecurityImpersonateEnabled the app security impersonate enabled
     * @return the add edit user page dto
     */
    public static AddEditUserPageDTO valueOf(List<Authority> availableAuthorities,
            boolean appSecurityImpersonateEnabled) {
        return valueOf(null, null, availableAuthorities, null, appSecurityImpersonateEnabled);
    }

    /**
     * Value of add edit user page dto.
     *
     * @param person                        the person
     * @param personActivationToken         the person activation token
     * @param availableAuthorities          the available authorities
     * @param lastLoginDate                 the last login date
     * @param appSecurityImpersonateEnabled the app security impersonate enabled
     * @return the add edit user page dto
     */
    public static AddEditUserPageDTO valueOf(Person person, PersonActivationToken personActivationToken,
            List<Authority> availableAuthorities, DateTime lastLoginDate, boolean appSecurityImpersonateEnabled) {
        AddEditUserPageDTO dto = new AddEditUserPageDTO();

        dto.setUser(DetailedPersonDTO.valueOf(person, lastLoginDate, personActivationToken));
        dto.setAppSecurityImpersonateEnabled(appSecurityImpersonateEnabled);

        if (availableAuthorities != null && availableAuthorities.size() > 0) {
            List<String> availableAuthoritiesNames = new ArrayList<>(availableAuthorities.size());

            for (Authority authority : availableAuthorities) {
                availableAuthoritiesNames.add(authority.getName());
            }

            dto.setAvailableAuthorities(availableAuthoritiesNames);
        }

        return dto;
    }
}
