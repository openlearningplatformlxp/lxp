package com.redhat.uxl.webapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.redhat.uxl.datalayer.repository.PersistentTokenRepository;
import com.redhat.uxl.datalayer.repository.PersonPasswordResetTokenRepository;
import com.redhat.uxl.datalayer.repository.PersonRepository;
import com.redhat.uxl.dataobjects.domain.PersistentToken;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.PersonActivationToken;
import com.redhat.uxl.dataobjects.domain.PersonPasswordResetToken;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.webapp.Application;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * The type Person service test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
// TODO: WJK: Determine what to replace this with
// @IntegrationTest
@Transactional
public class PersonServiceTest {

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private PersonPasswordResetTokenRepository personPasswordResetTokenRepository;

    @Inject
    private PersonRepository personRepository;

    @Inject
    private PersonService personService;

    /**
     * Test remove old persistent token.
     */
    @Test
    public void testRemoveOldPersistentToken() {
        Person admin = personRepository.findOneByLoginIgnoreCaseAndDeletedIsFalse("admin");
        int existingCount = persistentTokenRepository.findByPerson(admin).size();
        generatePersonToken(admin, "1111-1111", new LocalDate());
        LocalDate now = new LocalDate();
        PersistentToken oldPersistentToken = generatePersonToken(admin, "2222-2222", now.minusDays(32));
        assertThat(persistentTokenRepository.findByPerson(admin)).hasSize(existingCount + 2);
        personService.removeOldPersistentToken(oldPersistentToken.getSeries());
        assertThat(persistentTokenRepository.findByPerson(admin)).hasSize(existingCount + 1);
    }

    /**
     * Assert that person must exist to reset password.
     */
    @Test
    public void assertThatPersonMustExistToResetPassword() {
        PersonPasswordResetToken maybeResetToken = personService.requestPasswordReset("john.doe@localhost");
        assertThat(maybeResetToken != null).isFalse();

        maybeResetToken = personService.requestPasswordReset("admin@localhost");
        assertThat(maybeResetToken != null).isTrue();

        assertThat(maybeResetToken.getPerson().getEmail()).isEqualTo("admin@localhost");
        assertThat(maybeResetToken.getExpiryDateTime()).isNotNull();
        assertThat(maybeResetToken.getToken()).isNotNull();
    }

    /**
     * Assert that only activated user can request password reset.
     */
    @Test
    public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
        PersonActivationToken activationToken = personService.createPersonInformation("johndoe", "johndoe", "John",
                "Doe", "john.doe@localhost", "en-US");
        Person person = activationToken.getPerson();
        PersonPasswordResetToken maybeToken = personService.requestPasswordReset("john.doe@localhost");
        assertThat(maybeToken).isNull();
        personRepository.delete(person);
    }

    /**
     * Assert that reset key must not be older than 24 hours.
     */
    @Test
    public void assertThatResetKeyMustNotBeOlderThan24Hours() {
        PersonActivationToken activationToken = personService.createPersonInformation("johndoe", "johndoe", "John",
                "Doe", "john.doe@localhost", "en-US");
        Person person = activationToken.getPerson();

        person.setActivated(true);

        personRepository.save(person);

        PersonPasswordResetToken resetToken = personService.requestPasswordReset("john.doe@localhost");

        resetToken.setExpiryDateTime(DateTime.now().minusHours(25));

        personPasswordResetTokenRepository.save(resetToken);

        Person maybePerson = personService.completePasswordReset("newPassword", resetToken.getToken());

        assertThat(maybePerson != null).isFalse();

        personRepository.delete(person);
    }

    /**
     * Assert that reset key must be valid.
     */
    @Test
    public void assertThatResetKeyMustBeValid() {
        PersonActivationToken activationToken = personService.createPersonInformation("johndoe", "johndoe", "John",
                "Doe", "john.doe@localhost", "en-US");
        Person person = activationToken.getPerson();

        person.setActivated(true);

        personRepository.save(person);

        Person maybePerson = personService.completePasswordReset("johndoe2", "x" + activationToken.getToken());

        assertThat(maybePerson != null).isFalse();

        personRepository.delete(person);
    }

    /**
     * Assert that person can reset password.
     */
    @Test
    public void assertThatPersonCanResetPassword() {
        PersonActivationToken activationToken = personService.createPersonInformation("johndoe", "johndoe", "John",
                "Doe", "john.doe@localhost", "en-US");
        Person person = activationToken.getPerson();

        String oldPassword = person.getPassword();

        person.setActivated(true);

        personRepository.save(person);

        PersonPasswordResetToken resetToken = personService.requestPasswordReset("john.doe@localhost");

        Person maybePerson = personService.completePasswordReset("newPassword", resetToken.getToken());

        assertThat(maybePerson != null).isTrue();
        assertThat(maybePerson.getPassword()).isNotEqualTo(oldPassword);

        personRepository.delete(person);
    }

    private PersistentToken generatePersonToken(Person person, String tokenSeries, LocalDate localDate) {
        PersistentToken token = new PersistentToken();
        token.setSeries(tokenSeries);
        token.setPerson(person);
        token.setTokenValue(tokenSeries + "-data");
        token.setTokenDate(localDate);
        token.setIpAddress("127.0.0.1");
        token.setUserAgent("Test agent");
        return persistentTokenRepository.saveAndFlush(token);
    }
}
