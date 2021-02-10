package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.dataobjects.domain.Person;
import java.util.Set;
import lombok.Data;

/**
 * The type Person dto.
 */
@Data
public class PersonDTO {

    /**
     * The constant PASSWORD_MIN_LENGTH.
     */
    public static final int PASSWORD_MIN_LENGTH = 5;
    /**
     * The constant PASSWORD_MAX_LENGTH.
     */
    public static final int PASSWORD_MAX_LENGTH = 100;

    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String langKey;
    private boolean activated;
    private boolean disabled;
    private boolean manager;

    private Set<String> authorities; // TODO: SAC: should this be renamed - authoritiesNames

    /**
     * Instantiates a new Person dto.
     */
    public PersonDTO() {
    }

    /**
     * Instantiates a new Person dto.
     *
     * @param login       the login
     * @param password    the password
     * @param firstName   the first name
     * @param lastName    the last name
     * @param email       the email
     * @param activated   the activated
     * @param disabled    the disabled
     * @param isManager   the is manager
     * @param langKey     the lang key
     * @param authorities the authorities
     */
    public PersonDTO(String login, String password, String firstName, String lastName, String email, boolean activated,
            boolean disabled, boolean isManager, String langKey, Set<String> authorities) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.disabled = disabled;
        this.manager = isManager;
        this.langKey = langKey;
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "PersonDTO{" + "login='" + login + '\'' + ", password='" + password + '\'' + ", firstName='" + firstName
                + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", activated=" + activated
                + ", disabled=" + disabled + ", manager=" + manager + ", langKey='" + langKey + '\'' + ", authorities="
                + authorities + '}';
    }

    /**
     * To person person.
     *
     * @return the person
     */
    public Person toPerson() {
        Person person = new Person();

        if (StrUtils.isNotBlank(getLogin())) {
            person.setLogin(getLogin());
        }

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
     * Value of person dto.
     *
     * @param person the person
     * @return the person dto
     */
    public static PersonDTO valueOf(Person person) {
        if (person == null) {
            return new PersonDTO();
        }

        PersonDTO dto = new PersonDTO();

        dto.setLogin(person.getLogin());
        dto.setPassword(StrUtils.isNotBlank(person.getPassword()) ? "*****" : null);
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setEmail(person.getEmail());
        dto.setLangKey(person.getLangKey());
        dto.setActivated(person.isActivated());
        dto.setDisabled(person.isDisabled());

        return dto;
    }
}
