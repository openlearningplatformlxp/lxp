package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.PersonActivationToken;
import com.redhat.uxl.dataobjects.domain.PersonPasswordResetToken;
import java.util.List;
import java.util.Set;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Person service.
 */
public interface PersonService {

    /**
     * Activate registration person.
     *
     * @param key the key
     * @return the person
     */
    Person activateRegistration(String key);

    /**
     * Complete password reset person.
     *
     * @param newPassword the new password
     * @param key         the key
     * @return the person
     */
    Person completePasswordReset(String newPassword, String key);

    /**
     * Gets person by activation token.
     *
     * @param token the token
     * @return the person by activation token
     */
    Person getPersonByActivationToken(String token);

    /**
     * Create person information person activation token.
     *
     * @param login     the login
     * @param password  the password
     * @param firstName the first name
     * @param lastName  the last name
     * @param email     the email
     * @param langKey   the lang key
     * @return the person activation token
     */
    PersonActivationToken createPersonInformation(String login, String password, String firstName, String lastName,
            String email, String langKey);

    /**
     * Delete person.
     *
     * @param personId the person id
     */
    void deletePerson(Long personId);

    /**
     * Delete persons.
     *
     * @param personIds the person ids
     */
    void deletePersons(List<Long> personIds);

    /**
     * Update person information.
     *
     * @param login     the login
     * @param firstName the first name
     * @param lastName  the last name
     * @param email     the email
     * @param langKey   the lang key
     */
    void updatePersonInformation(String login, String firstName, String lastName, String email, String langKey);

    /**
     * Update person person.
     *
     * @param person           the person
     * @param authoritiesNames the authorities names
     * @return the person
     */
    Person updatePerson(Person person, Set<String> authoritiesNames);

    /**
     * Create person person.
     *
     * @param person      the person
     * @param authorities the authorities
     * @return the person
     */
    Person createPerson(Person person, Set<String> authorities);

    /**
     * Create person person.
     *
     * @param person          the person
     * @param authorities     the authorities
     * @param baseUrl         the base url
     * @param personInitiated the person initiated
     * @return the person
     */
    Person createPerson(Person person, Set<String> authorities, String baseUrl, Person personInitiated);

    /**
     * Change password.
     *
     * @param login           the login
     * @param currentPassword the current password
     * @param newPassword     the new password
     */
    void changePassword(String login, String currentPassword, String newPassword);

    /**
     * Gets activation token by person id.
     *
     * @param personId the person id
     * @return the activation token by person id
     */
    PersonActivationToken getActivationTokenByPersonId(Long personId);

    /**
     * Gets last login date.
     *
     * @param personId the person id
     * @return the last login date
     */
    DateTime getLastLoginDate(Long personId);

    /**
     * Gets person with authorities.
     *
     * @param id the id
     * @return the person with authorities
     */
    Person getPersonWithAuthorities(Long id);

    /**
     * Gets person by login.
     *
     * @param login           the login
     * @param loadAuthorities the load authorities
     * @return the person by login
     */
    Person getPersonByLogin(String login, boolean loadAuthorities);

    /**
     * Gets person by email.
     *
     * @param login           the login
     * @param loadAuthorities the load authorities
     * @return the person by email
     */
    Person getPersonByEmail(String login, boolean loadAuthorities);

    /**
     * Remove expired password reset tokens.
     */
    void removeExpiredPasswordResetTokens();

    /**
     * Remove old persistent token.
     *
     * @param series the series
     */
    void removeOldPersistentToken(String series);

    /**
     * Remove not activated person.
     *
     * @param personActivationTokenId the person activation token id
     */
    void removeNotActivatedPerson(Long personActivationTokenId);

    /**
     * Request password reset person password reset token.
     *
     * @param mail the mail
     * @return the person password reset token
     */
    PersonPasswordResetToken requestPasswordReset(String mail);

    /**
     * Find for paged search page.
     *
     * @param searchOperation the search operation
     * @param searchValue     the search value
     * @param pageable        the pageable
     * @return the page
     */
    Page<Person> findForPagedSearch(String searchOperation, String searchValue, Pageable pageable);

}
