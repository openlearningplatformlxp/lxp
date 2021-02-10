package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.datalayer.repository.AuthorityRepository;
import com.redhat.uxl.datalayer.repository.FeedbackRepository;
import com.redhat.uxl.datalayer.repository.PersistenceAuditEventRepository;
import com.redhat.uxl.datalayer.repository.PersistentTokenRepository;
import com.redhat.uxl.datalayer.repository.PersonActivationTokenRepository;
import com.redhat.uxl.datalayer.repository.PersonPasswordResetTokenRepository;
import com.redhat.uxl.datalayer.repository.PersonRepository;
import com.redhat.uxl.dataobjects.domain.Authority;
import com.redhat.uxl.dataobjects.domain.PersistentAuditEvent;
import com.redhat.uxl.dataobjects.domain.PersistentToken;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.PersonActivationToken;
import com.redhat.uxl.dataobjects.domain.PersonPasswordResetToken;
import com.redhat.uxl.services.errors.PersonServiceErrorCode;
import com.redhat.uxl.services.service.AuthorityService;
import com.redhat.uxl.services.service.EmailService;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.services.service.search.SearchBuilder;
import com.redhat.uxl.services.service.search.SearchSpec;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * The type Person service.
 */
@Service
@Transactional
@Slf4j
public class PersonServiceImpl implements PersonService {
  @Value("${app.security.activation.hoursTokenValid}")
  private int activationHoursTokenValid;

  @Value("${app.security.password.reset.hoursTokenValid}")
  private int passwordResetHoursTokenValid;

  @Inject
  private PersonActivationTokenRepository personActivationTokenRepository;

  @Inject
  private AuthorityService authorityService;

  @Inject
  private EmailService emailService;

  @Inject
  private PasswordEncoder passwordEncoder;

  @Inject
  private PersistenceAuditEventRepository persistenceAuditEventRepository;

  @Inject
  private PersonPasswordResetTokenRepository personPasswordResetTokenRepository;

  @Inject
  private PersonRepository personRepository;

  @Inject
  private PersistentTokenRepository persistentTokenRepository;

  @Inject
  private AuthorityRepository authorityRepository;

  @Inject
  private FeedbackRepository feedbackRepository;

  @Override
  @Timed
  @Transactional
  public Person activateRegistration(String key) {
    log.debug("Activating person for activation key {}", key);

    PersonActivationToken activationToken = personActivationTokenRepository.findByToken(key);

    if (activationToken == null) {
      return null;
    }

    Person person = personRepository.findOneDeletedIsFalse(activationToken.getPerson().getId());

    person.setActivated(true);
    person = personRepository.saveAndFlush(person);

    personActivationTokenRepository.delete(activationToken);

    log.debug("Activated person: {}", person);

    return person;
  }

  @Override
  @Timed
  @Transactional
  public Person completePasswordReset(String newPassword, String key) {
    log.debug("Reset user password for reset key {}", key);
    PersonPasswordResetToken resetToken = personPasswordResetTokenRepository.findByToken(key);

    if (resetToken == null) {
      return null;
    }

    if (DateTime.now().isAfter(resetToken.getExpiryDateTime())) {
      return null;
    }

    Person person = personRepository.findOneDeletedIsFalse(resetToken.getPerson().getId());

    if (person == null || !person.isActivated() || person.isDisabled()) {
      return null;
    }

    person.setPassword(passwordEncoder.encode(newPassword));

    person = personRepository.saveAndFlush(person);

    personPasswordResetTokenRepository.delete(resetToken);

    return person;
  }

  @Override
  @Timed
  @Transactional
  public void deletePerson(Long personId) {
    if (personId == null) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "Id for Person record to delete cannot be null.");
    }

    deletePersons(Arrays.asList(personId));
  }

  @Override
  @Timed
  @Transactional
  public void deletePersons(List<Long> personIds) {
    if (personIds == null || personIds.isEmpty()) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "Ids for Person records to delete cannot be empty.");
    }

    int numDeleted = personRepository.delete(personIds);

    if (numDeleted != personIds.size()) {
      throw new GeneralException(ErrorCodeGeneral.NOT_FOUND,
          "One or more Person records not found.");
    }
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public Person getPersonByActivationToken(String token) {
    if (StrUtils.isBlank(token)) {
      return null;
    }

    PersonActivationToken activationToken = personActivationTokenRepository.findByToken(token);

    if (activationToken == null) {
      return null;
    }

    return activationToken.getPerson();
  }

  @Override
  @Timed
  @Transactional
  public PersonPasswordResetToken requestPasswordReset(String mail) {
    Person person = personRepository.findOneByEmailIgnoreCaseAndDeletedIsFalse(mail);

    if (person == null) {
      return null;
    }

    if (!person.isActivated() || person.isDisabled() || StrUtils.isBlank(person.getLogin())) {
      return null;
    }

    PersonPasswordResetToken resetToken =
        personPasswordResetTokenRepository.findByPersonId(person.getId());

    if (resetToken != null) {
      personPasswordResetTokenRepository.delete(resetToken);
      personPasswordResetTokenRepository.flush();
    }

    resetToken = new PersonPasswordResetToken();

    resetToken.setPerson(person);
    resetToken.setToken(UUID.randomUUID().toString());
    resetToken.setExpiryDateTime(DateTime.now().plusHours(passwordResetHoursTokenValid));

    return personPasswordResetTokenRepository.saveAndFlush(resetToken);
  }

  @Override
  @Timed
  @Transactional
  public PersonActivationToken createPersonInformation(String login, String password,
      String firstName, String lastName, String email, String langKey) {
    Person newPerson = new Person();
    Authority authority = authorityRepository.findOne("ROLE_USER");
    Set<Authority> authorities = new HashSet<>();

    authorities.add(authority);

    newPerson.setLogin(login);
    newPerson.setPassword(passwordEncoder.encode(password));
    newPerson.setFirstName(firstName);
    newPerson.setLastName(lastName);
    newPerson.setEmail(email);
    newPerson.setLangKey(langKey);
    newPerson.setActivated(false); // new person is not active
    newPerson.setAuthorities(authorities);

    newPerson = personRepository.saveAndFlush(newPerson);

    PersonActivationToken activationToken = new PersonActivationToken();

    activationToken.setPerson(newPerson);
    activationToken.setToken(UUID.randomUUID().toString());
    activationToken.setExpiryDateTime(DateTime.now().plusHours(activationHoursTokenValid));

    log.debug("Created Information for Person: {}", newPerson);

    return personActivationTokenRepository.saveAndFlush(activationToken);
  }

  @Override
  @Timed
  @Transactional
  public Person createPerson(Person person, Set<String> authorities) {
    person.setLogin(person.getLogin());
    person.setAuthorities(authorityService.toAuthoritySet(authorities));

    if (StrUtils.isNotBlank(person.getPassword())) {
      person.setPassword(passwordEncoder.encode(person.getPassword()));
    }

    return personRepository.save(person);
  }

  @Override
  @Timed
  @Transactional
  public Person createPerson(Person person, Set<String> authorities, String baseUrl,
      Person personInitiated) {
    person.setLogin(person.getLogin());
    person.setAuthorities(authorityService.toAuthoritySet(authorities));

    if (StrUtils.isNotBlank(person.getPassword())) {
      person.setPassword(passwordEncoder.encode(person.getPassword()));
    }

    Person newPerson = personRepository.saveAndFlush(person);
    PersonActivationToken activationToken = new PersonActivationToken();

    activationToken.setPerson(newPerson);
    activationToken.setToken(UUID.randomUUID().toString());
    activationToken.setExpiryDateTime(DateTime.now().plusHours(activationHoursTokenValid));

    personActivationTokenRepository.saveAndFlush(activationToken);

    log.debug("Created Person: {}", newPerson);

    return newPerson;
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public PersonActivationToken getActivationTokenByPersonId(Long personId) {
    if (personId == null) {
      return null;
    }

    return personActivationTokenRepository.findByPersonId(personId);
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public DateTime getLastLoginDate(Long personId) {
    if (personId == null) {
      return null;
    }

    Person person = personRepository.findOneDeletedIsFalse(personId);

    if (person == null || StrUtils.isBlank(person.getLogin())) {
      return null;
    }

    PersistentAuditEvent persistentAuditEvent = persistenceAuditEventRepository
        .findTopByPrincipalAndAuditEventTypeOrderByAuditEventDateDesc(person.getLogin(),
            "AUTHENTICATION_SUCCESS");

    if (persistentAuditEvent == null) {
      return null;
    }

    return persistentAuditEvent.getAuditEventDate().toDateTime();
  }

  @Override
  @Timed
  @Transactional
  public void updatePersonInformation(String login, String firstName, String lastName, String email,
      String langKey) {
    Person currentPerson = personRepository.findOneByLoginIgnoreCaseAndDeletedIsFalse(login);

    currentPerson.setFirstName(firstName);
    currentPerson.setLastName(lastName);
    currentPerson.setEmail(email);
    currentPerson.setLangKey(langKey);

    personRepository.save(currentPerson);

    log.debug("Changed Information for Person: {}", currentPerson);
  }

  @Override
  @Timed
  @Transactional
  public Person updatePerson(Person person, Set<String> authoritiesNames) {
    if (person == null || person.getId() == null) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST);
    }

    Person currentPerson = personRepository.findOneDeletedIsFalse(person.getId());

    if (currentPerson == null) {
      throw new GeneralException(ErrorCodeGeneral.NOT_FOUND);
    }

    currentPerson.setLogin(person.getLogin());
    currentPerson.setFirstName(person.getFirstName());
    currentPerson.setLastName(person.getLastName());
    currentPerson.setEmail(person.getEmail());
    currentPerson.setActivated(person.isActivated());
    currentPerson.setDisabled(person.isDisabled());

    if (StrUtils.isNotBlank(person.getPassword())) {
      currentPerson.setPassword(passwordEncoder.encode(person.getPassword()));
    }

    if (authoritiesNames != null) {
      currentPerson.setAuthorities(authorityService.toAuthoritySet(authoritiesNames));
    }

    return personRepository.save(currentPerson);
  }

  @Override
  @Timed
  @Transactional
  public void changePassword(String login, String currentPassword, String newPassword) {
    Person currentPerson = personRepository.findOneByLoginIgnoreCaseAndDeletedIsFalse(login);

    if (!passwordEncoder.matches(currentPassword, currentPerson.getPassword())) {
      throw new GeneralException(PersonServiceErrorCode.BAD_PASSWORD);
    }

    String encryptedPassword = passwordEncoder.encode(newPassword);
    currentPerson.setPassword(encryptedPassword);
    personRepository.save(currentPerson);
    log.debug("Changed password for Person: {}", currentPerson);
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public Person getPersonByLogin(String login, boolean loadAuthorities) {
    Person person = personRepository.findOneByLoginIgnoreCaseAndDeletedIsFalse(login);

    if (person == null) {
      return null;
    }

    if (loadAuthorities) {
      person.getAuthorities().size();
    }

    return person;
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public Person getPersonByEmail(String login, boolean loadAuthorities) {
    Person person = personRepository.findByEmail(login);

    if (person == null) {
      return null;
    }

    if (loadAuthorities) {
      person.getAuthorities().size();
    }

    return person;
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public Person getPersonWithAuthorities(Long id) {
    Person person = personRepository.findOneDeletedIsFalse(id);

    if (person != null) {
      person.getAuthorities().size(); // eagerly load the association
    }

    return person;
  }

  @Override
  @Timed
  @Transactional
  public void removeOldPersistentToken(String series) {
    log.debug("Deleting token - series: {}", series);
    PersistentToken token = persistentTokenRepository.findOne(series);
    Person person = token.getPerson();
    person.getPersistentTokens().remove(token);
    persistentTokenRepository.delete(token);
  }

  @Override
  @Timed
  @Transactional
  public void removeNotActivatedPerson(Long personActivationTokenId) {
    PersonActivationToken activationToken =
        personActivationTokenRepository.findOne(personActivationTokenId);
    Person person = activationToken.getPerson();

    personActivationTokenRepository.delete(activationToken);

    if (!person.isActivated()) {
      person = personRepository.findOneDeletedIsFalse(person.getId());
      personRepository.delete(person);

      log.debug("Deleting not activated person {}", person.getLogin());
    }
  }

  @Override
  @Timed
  @Transactional
  public void removeExpiredPasswordResetTokens() {
    List<PersonPasswordResetToken> resetTokens =
        personPasswordResetTokenRepository.findAllByExpiryDateTimeBefore(DateTime.now());

    if (resetTokens.size() > 0) {
      personPasswordResetTokenRepository.delete(resetTokens);
    }
  }

  @Override
  @Timed
  @Transactional
  public Page<Person> findForPagedSearch(String searchOperation, String searchValue,
      Pageable pageable) {
    SearchBuilder<Person> searchBuilder = new SearchBuilder<>(personRepository);

    Page<Person> personsPage = searchBuilder
        .where("or", SearchSpec.valueOf("login", searchOperation, searchValue),
            SearchSpec.valueOf("firstName", searchOperation, searchValue),
            SearchSpec.valueOf("lastName", searchOperation, searchValue),
            SearchSpec.valueOf("email", searchOperation, searchValue))
        .and("or", SearchSpec.isNull("login"), SearchSpec.notIn("login", "anonymousUser", "system"))
        .and(SearchSpec.isFalse("deleted")).findForPagedSearch(pageable);

    return personsPage;
  }

}
