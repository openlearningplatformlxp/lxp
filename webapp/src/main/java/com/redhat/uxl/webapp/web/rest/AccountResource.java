package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.datalayer.repository.PersistentTokenRepository;
import com.redhat.uxl.datalayer.repository.PersonRepository;
import com.redhat.uxl.dataobjects.domain.Authority;
import com.redhat.uxl.dataobjects.domain.PersistentToken;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.PersonActivationToken;
import com.redhat.uxl.dataobjects.domain.PersonPasswordResetToken;
import com.redhat.uxl.services.service.EmailService;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.services.service.TeamService;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.dto.KeyAndPasswordDTO;
import com.redhat.uxl.webapp.web.rest.dto.PersonDTO;
import com.redhat.uxl.webapp.web.rest.dto.account.ChangePasswordDTO;
import com.redhat.uxl.webapp.web.utils.RequestUtils;
import io.swagger.annotations.ApiOperation;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Account resource.
 */
@RestController
@RequestMapping(value = "/api/account", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AccountResource extends BaseResource {
    @Inject
    private PersonRepository personRepository;

    @Inject
    private PersonService personService;
    @Inject
    private TeamService teamService;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private EmailService emailService;

    /**
     * Register account response entity.
     *
     * @param personDTO the person dto
     * @param key       the key
     * @param request   the request
     * @return the response entity
     */
    @ApiOperation(value = "Register the person.", notes = "<p>Register the person.</p>")
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> registerAccount(@Valid @RequestBody PersonDTO personDTO,
            @RequestParam(value = "key", required = false) String key, HttpServletRequest request) {
        boolean activate = StrUtils.isNotBlank(key);
        Person personRegistering = (activate ? personService.getPersonByActivationToken(key) : null);

        if (activate && personRegistering == null) {
            throw new GeneralException(ErrorCodeGeneral.FORBIDDEN);
        }

        Person person = personService.getPersonByLogin(personDTO.getLogin(), false);

        if (person != null) {
            if (!activate || (activate && !person.getLogin().equalsIgnoreCase(personRegistering.getLogin()))) {
                return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("login already in use");
            }
        }

        person = personRepository.findOneByEmailIgnoreCaseAndDeletedIsFalse(personDTO.getEmail());

        if (person != null) {
            if (!activate || (activate && !person.getEmail().equalsIgnoreCase(personRegistering.getEmail()))) {
                return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN)
                        .body("email address already in use");
            }
        }

        if (activate) {
            person = personService.activateRegistration(key);

            person.setLogin(personDTO.getLogin());
            person.setEmail(personDTO.getEmail());
            person.setFirstName(personDTO.getFirstName());
            person.setLastName(personDTO.getLastName());
            person.setPassword(personDTO.getPassword());

            person = personService.updatePerson(person, null);

            if (person == null) {
                throw new GeneralException(ErrorCodeGeneral.FORBIDDEN);
            }
        } else {
            PersonActivationToken activationToken = personService.createPersonInformation(personDTO.getLogin(),
                    personDTO.getPassword(), personDTO.getFirstName(), personDTO.getLastName(),
                    personDTO.getEmail().toLowerCase(), personDTO.getLangKey());
            Person personInitiated = personService.getPersonByLogin(SecurityUtils.getCurrentLogin(), false);

            emailService.sendActivationEmail(activationToken, RequestUtils.getBaseUrl(request),
                    personInitiated.getId());
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Activate account map.
     *
     * @param key the key
     * @return the map
     */
    @ApiOperation(value = "Activate the registered person.", notes = "<p>Activate the registered person.</p>")
    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    @Timed
    public Map<String, Object> activateAccount(@RequestParam(value = "key") String key) {
        Person person = personService.getPersonByActivationToken(key);

        if (person == null) {
            throw new GeneralException(ErrorCodeGeneral.FORBIDDEN);
        }

        boolean activate = (StrUtils.isAllNotBlank(person.getLogin(), person.getPassword(), person.getFirstName(),
                person.getLastName(), person.getEmail()));

        if (activate) {
            person = personService.activateRegistration(key);

            if (person == null) {
                throw new GeneralException(ErrorCodeGeneral.FORBIDDEN);
            }
        }

        Map<String, Object> retVal = new HashMap<>(1);

        retVal.put("activated", activate);

        return retVal;
    }

    /**
     * Is authenticated string.
     *
     * @param request the request
     * @return the string
     */
    // TODO: SAC: Is this used for anything... (not directly in this code - when logged in returns a
    // value for
    // cookie-based auth but not xauth)
    @ApiOperation(value = "Check if the person is authenticated and return its login.", notes = "<p>Check if the person is authenticated and return its login.</p>")
    @RequestMapping(value = "/authenticate", method = RequestMethod.GET)
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current person is authenticated");
        return request.getRemoteUser();
    }

    /**
     * Gets account.
     *
     * @param request the request
     * @return the account
     */
    @ApiOperation(value = "Get the current person.", notes = "<p>Get the current person.</p>")
    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public ResponseEntity<PersonDTO> getAccount(HttpServletRequest request) {
        Person person = personService.getPersonByLogin(SecurityUtils.getCurrentLogin(), true);

        if (person == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Boolean isManager = teamService.isManager(SecurityUtils.getCurrentLoginAsLong());

        Set<String> authorities = new HashSet<>();
        Principal principal = request.getUserPrincipal();

        if (principal != null && principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;

            for (GrantedAuthority authority : authentication.getAuthorities()) {
                authorities.add(authority.getAuthority());
            }
        } else {
            for (Authority authority : person.getAuthorities()) {
                authorities.add(authority.getName());
            }

            if (SecurityUtils.isImpersonatedUser()) {
                authorities.add(AuthoritiesConstants.GRANTED_PREVIOUS_ADMINISTRATOR);
            }
        }

        return new ResponseEntity<>(
                new PersonDTO(person.getLogin(), null, person.getFirstName(), person.getLastName(), person.getEmail(),
                        person.isActivated(), person.isDisabled(), isManager, person.getLangKey(), authorities),
                HttpStatus.OK);
    }

    /**
     * Save account response entity.
     *
     * @param personDTO the person dto
     * @return the response entity
     */
    @ApiOperation(value = "Update the current person information.", notes = "<p>Update the current person information.</p>")
    @RequestMapping(method = RequestMethod.POST)
    @Timed
    public ResponseEntity<String> saveAccount(@RequestBody PersonDTO personDTO) {
        Person personHavingThisLogin = personService.getPersonByLogin(personDTO.getLogin(), false);
        if (personHavingThisLogin != null
                && !personHavingThisLogin.getLogin().equals(SecurityUtils.getCurrentLogin())) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        personService.updatePersonInformation(SecurityUtils.getCurrentLogin(), personDTO.getFirstName(),
                personDTO.getLastName(), personDTO.getEmail(), personDTO.getLangKey());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Change password response entity.
     *
     * @param changePasswordDTO the change password dto
     * @return the response entity
     */
    @ApiOperation(value = "Change the current person's password.", notes = "<p>Change the current person's password.</p>")
    @RequestMapping(value = "/change_password", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        if (!checkPasswordLength(changePasswordDTO.getNewPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        personService.changePassword(SecurityUtils.getCurrentLogin(), changePasswordDTO.getCurrentPassword(),
                changePasswordDTO.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Gets current sessions.
     *
     * @return the current sessions
     */
    @ApiOperation(value = "Get the current open sessions.", notes = "<p>Get the current open sessions.</p>")
    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<List<PersistentToken>> getCurrentSessions() {
        Person person = personService.getPersonByLogin(SecurityUtils.getCurrentLogin(), false);
        if (person == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(persistentTokenRepository.findByPerson(person), HttpStatus.OK);
    }

    /**
     * Invalidate session.
     *
     * @param series the series
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    @ApiOperation(value = "Invalidate an existing session.", notes = "<p>Get the current open sessions.</p>"
            + "<p>You can only delete your own sessions, not any other person's session</p>" + "<p>"
            + "If you delete one of your existing sessions, and that you are currently logged in on that session, you will "
            + "still be able to use that session, until you quit your browser: it does not work in real time (there is "
            + "no API for that), it only removes the \"remember me\" cookie" + "</p>" + "<p>"
            + "This is also true if you invalidate your current session: you will still be able to use it until you close"
            + "your browser or that the session times out. But automatic login (the \"remember me\" cookie) will not work"
            + "anymore."
            + "There is an API to invalidate the current session, but there is no API to check which session uses which"
            + "cookie." + "</p>")
    @RequestMapping(value = "/sessions/{series}", method = RequestMethod.DELETE)
    @Timed
    public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
        String decodedSeries = URLDecoder.decode(series, "UTF-8");
        Person person = personService.getPersonByLogin(SecurityUtils.getCurrentLogin(), false);
        List<PersistentToken> persistentTokens = persistentTokenRepository.findByPerson(person);
        for (PersistentToken persistentToken : persistentTokens) {
            if (StringUtils.equals(persistentToken.getSeries(), decodedSeries)) {
                persistentTokenRepository.deleteById(decodedSeries);
            }
        }
    }

    /**
     * Request password reset response entity.
     *
     * @param mail    the mail
     * @param request the request
     * @return the response entity
     */
    @ApiOperation(value = "Start Reset Password Process.", notes = "<p>Start Reset Password Process. (i.e. create reset token and send email)</p>")
    @RequestMapping(value = "/reset_password/init", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {
        PersonPasswordResetToken resetToken = personService.requestPasswordReset(mail);

        if (resetToken != null) {
            Person personInitiated = personService.getPersonByLogin(SecurityUtils.getCurrentLogin(), false);
            emailService.sendPasswordResetMail(resetToken, RequestUtils.getBaseUrl(request), personInitiated.getId());
            return new ResponseEntity<>("email was sent", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("email address not registered", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Finish password reset response entity.
     *
     * @param keyAndPassword the key and password
     * @return the response entity
     */
    @ApiOperation(value = "Finish Reset Password Process.", notes = "<p>Finish Reset Password Process. (i.e. actually reset the password)</p>")
    @RequestMapping(value = "/reset_password/finish", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordDTO keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }

        Person person = personService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (person != null) {
            return new ResponseEntity<String>(HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets registration page data.
     *
     * @param key the key
     * @return the registration page data
     */
    @ApiOperation(value = "Get the Registration page data.", notes = "<p>Get the Registration page data.</p>")
    @RequestMapping(value = "/page/register", method = RequestMethod.GET)
    @Timed
    public Map<String, Object> getRegistrationPageData(@RequestParam(value = "key", required = false) String key) {
        Map<String, Object> retVal = new HashMap<>(1);

        if (StrUtils.isNotBlank(key)) {
            Person person = personService.getPersonByActivationToken(key);

            retVal.put("person", person != null ? PersonDTO.valueOf(person) : null);
        }

        return retVal;
    }

    // Private helper methods

    private boolean checkPasswordLength(String password) {
        return (StrUtils.isNotBlank(password) && password.length() >= PersonDTO.PASSWORD_MIN_LENGTH
                && password.length() <= PersonDTO.PASSWORD_MAX_LENGTH);
    }
}
