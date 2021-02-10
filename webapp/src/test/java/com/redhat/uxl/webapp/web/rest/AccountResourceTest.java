package com.redhat.uxl.webapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.redhat.uxl.datalayer.repository.AuthorityRepository;
import com.redhat.uxl.datalayer.repository.PersonRepository;
import com.redhat.uxl.dataobjects.domain.Authority;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.services.service.EmailService;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.webapp.Application;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.web.rest.dto.PersonDTO;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * The type Account resource test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
// TODO: WJK: Determine what to replace this with
//@IntegrationTest
public class AccountResourceTest {

    @Inject
    private PersonRepository personRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private PersonService personService;

    @Mock
    private PersonService mockPersonService;

    @Mock
    private EmailService mockEmailService;

    private MockMvc restPersonMockMvc;

    private MockMvc restMvc;

    /**
     * Sets .
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        AccountResource accountResource = new AccountResource();
        ReflectionTestUtils.setField(accountResource, "personRepository", personRepository);
        ReflectionTestUtils.setField(accountResource, "personService", personService);
        ReflectionTestUtils.setField(accountResource, "emailService", mockEmailService);

        AccountResource accountPersonMockResource = new AccountResource();
        ReflectionTestUtils.setField(accountPersonMockResource, "personRepository", personRepository);
        ReflectionTestUtils.setField(accountPersonMockResource, "personService", mockPersonService);
        ReflectionTestUtils.setField(accountPersonMockResource, "emailService", mockEmailService);

        this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).build();
        this.restPersonMockMvc = MockMvcBuilders.standaloneSetup(accountPersonMockResource).build();
    }

    /**
     * Test non authenticated person.
     *
     * @throws Exception the exception
     */
    @Test
    public void testNonAuthenticatedPerson() throws Exception {
        restPersonMockMvc.perform(get("/api/account/authenticate").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string(""));
    }

    /**
     * Test authenticated person.
     *
     * @throws Exception the exception
     */
    @Test
    public void testAuthenticatedPerson() throws Exception {
        restPersonMockMvc.perform(get("/api/account/authenticate").with(request -> {
            request.setRemoteUser("test");
            return request;
        }).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().string("test"));
    }

    /**
     * Test get existing account.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetExistingAccount() throws Exception {
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        authorities.add(authority);

        Person person = new Person();
        person.setLogin("test");
        person.setFirstName("john");
        person.setLastName("doe");
        person.setEmail("john.doe@nowhere.com");
        person.setAuthorities(authorities);
        when(mockPersonService.getPersonByLogin(anyString(), true)).thenReturn(person);

        restPersonMockMvc.perform(get("/api/account").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.login").value("test")).andExpect(jsonPath("$.firstName").value("john"))
                .andExpect(jsonPath("$.lastName").value("doe"))
                .andExpect(jsonPath("$.email").value("john.doe@nowhere.com"))
                .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN));
    }

    /**
     * Test get unknown account.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetUnknownAccount() throws Exception {
        when(mockPersonService.getPersonByLogin("unknown", true)).thenReturn(null);

        restPersonMockMvc.perform(get("/api/account").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    /**
     * Test register valid.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void testRegisterValid() throws Exception {
        PersonDTO u = new PersonDTO("joe", // login
                "password", // password
                "Joe", // firstName
                "Shmoe", // lastName
                "joe@nowhere.com", // email
                true, // activated
                false, // disabled
                false, // manager
                "en", // langKey
                new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

        restMvc.perform(post("/api/account/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isCreated());

        Person person = personRepository.findOneByLoginIgnoreCaseAndDeletedIsFalse("joe");
        assertThat(person).isNotNull();
    }

    /**
     * Test register invalid login.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void testRegisterInvalidLogin() throws Exception {
        PersonDTO u = new PersonDTO("funky-log!n", // login <-- invalid
                "password", // password
                "Funky", // firstName
                "One", // lastName
                "funky@nowhere.com", // email
                true, // activated
                false, // disabled
                false, // manager
                "en", // langKey
                new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

        restPersonMockMvc.perform(post("/api/account/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(u)));

        /*
         * the DTO no longer enforces login, so this will not fail... the entity will fail, but this does not test that!
         * .andExpect(status().isBadRequest());
         */

        Person person = personRepository.findOneByEmailIgnoreCaseAndDeletedIsFalse("funky@nowhere.com");
        assertThat(person).isNull();
    }

    /**
     * Test register invalid email.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void testRegisterInvalidEmail() throws Exception {
        PersonDTO u = new PersonDTO("bob", // login
                "password", // password
                "Bob", // firstName
                "Green", // lastName
                "invalid", // email <-- invalid
                true, // activated
                false, // disabled
                false, // manager
                "en", // langKey
                new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

        restPersonMockMvc.perform(post("/api/account/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(u)));

        /*
         * the DTO no longer enforces email, so this will not fail... the entity will fail, but this does not test that!
         * .andExpect(status().isBadRequest());
         */

        Person person = personRepository.findOneByLoginIgnoreCaseAndDeletedIsFalse("bob");
        assertThat(person).isNull();
    }

    /**
     * Test register duplicate login.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void testRegisterDuplicateLogin() throws Exception {
        // Good
        PersonDTO u = new PersonDTO("alice", // login
                "password", // password
                "Alice", // firstName
                "Something", // lastName
                "alice@nowhere.com", // email
                true, // activated
                false, // disabled
                false, // manager
                "en", // langKey
                new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

        // Duplicate login, different email
        PersonDTO dup = new PersonDTO(u.getLogin(), u.getPassword(), u.getLogin(), u.getLastName(),
                "alicejr@nowhere.com", true, false, false, u.getLangKey(), u.getAuthorities());

        // Good user
        restMvc.perform(post("/api/account/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isCreated());

        // Duplicate login
        restMvc.perform(post("/api/account/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dup))).andExpect(status().is4xxClientError());

        Person personDup = personRepository.findOneByEmailIgnoreCaseAndDeletedIsFalse("alicejr@nowhere.com");
        assertThat(personDup).isNull();
    }

    /**
     * Test register duplicate email.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void testRegisterDuplicateEmail() throws Exception {
        // Good
        PersonDTO u = new PersonDTO("john", // login
                "password", // password
                "John", // firstName
                "Doe", // lastName
                "john@nowhere.com", // email
                true, // activated
                false, // disabled
                false, // manager
                "en", // langKey
                new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

        // Duplicate email, different login
        PersonDTO dup = new PersonDTO("johnjr", u.getPassword(), u.getLogin(), u.getLastName(), u.getEmail(), true,
                false, false, u.getLangKey(), u.getAuthorities());

        // Good user
        restMvc.perform(post("/api/account/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isCreated());

        // Duplicate email
        restMvc.perform(post("/api/account/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dup))).andExpect(status().is4xxClientError());

        Person personDup = personRepository.findOneByLoginIgnoreCaseAndDeletedIsFalse("johnjr");
        assertThat(personDup).isNull();
    }

    /**
     * Test register admin is ignored.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void testRegisterAdminIsIgnored() throws Exception {
        PersonDTO u = new PersonDTO("badguy", // login
                "password", // password
                "Bad", // firstName
                "Guy", // lastName
                "badguy@nowhere.com", // email
                true, // activated
                false, // disabled
                false, // manager
                "en", // langKey
                new HashSet<>(Arrays.asList(AuthoritiesConstants.ADMIN)) // <-- only admin should be able to
                                                                         // do that
        );

        restMvc.perform(post("/api/account/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(u))).andExpect(status().isCreated());

        Person personDup = personRepository.findOneByLoginIgnoreCaseAndDeletedIsFalse("badguy");
        assertThat(personDup).isNotNull();
        assertThat(personDup.getAuthorities()).hasSize(1)
                .containsExactly(authorityRepository.findOne(AuthoritiesConstants.USER));
    }
}
