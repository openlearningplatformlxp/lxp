package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.Email;
import com.redhat.uxl.services.service.EmailService;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import com.redhat.uxl.webapp.web.rest.dto.EmailDTO;
import com.redhat.uxl.webapp.web.rest.dto.PagedSearchDTO;
import io.swagger.annotations.ApiOperation;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Admin email resource.
 */
@RestController
@RequestMapping(value = "/api/admin/email", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminEmailResource extends BaseResource {
    @Inject
    private EmailService emailService;

    /**
     * Gets email.
     *
     * @param emailId the email id
     * @return the email
     */
    @ApiOperation(value = "Get Email.", notes = "<p>Get Email for given Id.</p>")
    @RequestMapping(value = "/{emailId}", method = RequestMethod.GET)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    EmailDTO getEmail(@PathVariable Long emailId) {
        return EmailDTO.valueOf(emailService.getEmail(emailId));
    }

    /**
     * Resend email email dto.
     *
     * @param emailId the email id
     * @return the email dto
     */
    @ApiOperation(value = "Resend Email.", notes = "<p>Resend Email for given Id.</p>")
    @RequestMapping(value = "/resend/{emailId}", method = RequestMethod.GET)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    EmailDTO resendEmail(@PathVariable Long emailId) {
        return EmailDTO.valueOf(emailService.resendEmail(emailId));
    }

    /**
     * Search emails page.
     *
     * @param pagedSearchDTO the paged search dto
     * @return the page
     */
    @ApiOperation(value = "Search for Emails.", notes = "<p>Search for Emails.</p>")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    Page<EmailDTO> searchEmails(@RequestBody PagedSearchDTO pagedSearchDTO) {
        Page<Email> emailsPage = emailService.findForPagedSearch(pagedSearchDTO.getSearchOperation(),
                pagedSearchDTO.getSearchValue(), pagedSearchDTO.getPageable());

        return EmailDTO.valueOf(emailsPage);
    }
}
