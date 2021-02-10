package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.dataobjects.domain.Authority;
import com.redhat.uxl.dataobjects.domain.Email;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.PersonActivationToken;
import com.redhat.uxl.services.service.AuthorityService;
import com.redhat.uxl.services.service.EmailService;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import com.redhat.uxl.webapp.web.rest.dto.admin.AddEditUserPageDTO;
import com.redhat.uxl.webapp.web.rest.dto.admin.AdminStatusPageDTO;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Admin page resource.
 */
@RestController
@RequestMapping(value = "/api/admin/pages", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminPageResource extends BaseResource {
    @Value("${app.mail.sendEmailJob.maxAttempts}")
    private int appMailSendEmailJobMaxAttempts;

    @Value("${app.security.impersonate.enabled}")
    private boolean appSecurityImpersonateEnabled;

    @Inject
    private AuthorityService authorityService;

    @Inject
    private EmailService emailService;

    @Inject
    private PersonService personService;

    /**
     * Gets status page.
     *
     * @return the status page
     */
    @ApiOperation(value = "TODO: SAC:", notes = "<p>TODO: SAC:</p>")
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @Timed
    @RolesAllowed({ AuthoritiesConstants.ADMIN, AuthoritiesConstants.WIKI_EDITOR })
    public AdminStatusPageDTO getStatusPage() {
        List<Email> failedEmails = emailService.getFailedEmails();

        return AdminStatusPageDTO.valueOf(failedEmails, appMailSendEmailJobMaxAttempts);
    }

    /**
     * Gets user upsert page.
     *
     * @return the user upsert page
     */
    @ApiOperation(value = "TODO: SAC:", notes = "<p>TODO: SAC:</p>")
    @RequestMapping(value = "/user-upsert", method = RequestMethod.GET)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public AddEditUserPageDTO getUserUpsertPage() {
        List<Authority> availableAuthorities = authorityService.getAvailableAuthorities();

        return AddEditUserPageDTO.valueOf(availableAuthorities, appSecurityImpersonateEnabled);
    }

    /**
     * Gets user upsert page.
     *
     * @param personId the person id
     * @return the user upsert page
     */
    @ApiOperation(value = "TODO: SAC:", notes = "<p>TODO: SAC:</p>")
    @RequestMapping(value = "/user-upsert/{personId}", method = RequestMethod.GET)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public AddEditUserPageDTO getUserUpsertPage(@PathVariable Long personId) {
        Person person = personService.getPersonWithAuthorities(personId);

        if (person == null) {
            throw new GeneralException(ErrorCodeGeneral.NOT_FOUND);
        }

        PersonActivationToken personActivationToken = personService.getActivationTokenByPersonId(personId);
        List<Authority> availableAuthorities = authorityService.getAvailableAuthorities();
        DateTime lastLoginDate = personService.getLastLoginDate(personId);

        return AddEditUserPageDTO.valueOf(person, personActivationToken, availableAuthorities, lastLoginDate,
                appSecurityImpersonateEnabled);
    }
}
