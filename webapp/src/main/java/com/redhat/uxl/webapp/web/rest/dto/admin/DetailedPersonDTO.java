package com.redhat.uxl.webapp.web.rest.dto.admin;

import com.redhat.uxl.dataobjects.domain.Authority;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.PersonActivationToken;
import com.redhat.uxl.webapp.web.rest.dto.PersonDTO;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * The type Detailed person dto.
 */
@Data
public class DetailedPersonDTO extends PersonDTO {
    private Long id;

    private String activationToken;

    private String createdBy;
    private DateTime createdDate;
    private DateTime lastLoginDate;
    private String lastModifiedBy;
    private DateTime lastModifiedDate;

    /**
     * To person person.
     *
     * @param id the id
     * @return the person
     */
    public Person toPerson(Long id) {
        Person person = new Person();

        person.setId(getId());
        person.setLogin(getLogin());
        person.setPassword(getPassword());
        person.setFirstName(getFirstName());
        person.setLastName(getLastName());
        person.setEmail(getEmail());
        person.setLangKey(getLangKey());
        person.setActivated(isActivated());
        person.setDisabled(isDisabled());

        return person;
    }

    /**
     * Value of detailed person dto.
     *
     * @param person                the person
     * @param personActivationToken the person activation token
     * @return the detailed person dto
     */
    public static DetailedPersonDTO valueOf(Person person, PersonActivationToken personActivationToken) {
        return valueOf(person, null, personActivationToken);
    }

    /**
     * Value of detailed person dto.
     *
     * @param person                the person
     * @param lastLoginDate         the last login date
     * @param personActivationToken the person activation token
     * @return the detailed person dto
     */
    public static DetailedPersonDTO valueOf(Person person, DateTime lastLoginDate,
            PersonActivationToken personActivationToken) {
        DetailedPersonDTO dto = new DetailedPersonDTO();

        if (person != null) {
            dto.setId(person.getId());
            dto.setLogin(person.getLogin());
            dto.setFirstName(person.getFirstName());
            dto.setLastName(person.getLastName());
            dto.setEmail(person.getEmail());
            dto.setLangKey(person.getLangKey());
            dto.setCreatedBy(person.getCreatedBy());
            dto.setCreatedDate(person.getCreatedDate());
            dto.setLastLoginDate(lastLoginDate);
            dto.setLastModifiedBy(person.getLastModifiedBy());
            dto.setLastModifiedDate(person.getLastModifiedDate());
            dto.setActivated(person.isActivated());
            dto.setDisabled(person.isDisabled());

            if (personActivationToken != null) {
                dto.setActivationToken(personActivationToken.getToken());
            }

            Set<String> authoritiesNames;
            Set<Authority> authorities = person.getAuthorities();

            if (authorities != null && authorities.size() > 0) {
                authoritiesNames = new HashSet<>(authorities.size());

                for (Authority authority : authorities) {
                    authoritiesNames.add(authority.getName());
                }
            } else {
                authoritiesNames = new HashSet<>(0);
            }

            dto.setAuthorities(authoritiesNames);
        }

        return dto;
    }
}
