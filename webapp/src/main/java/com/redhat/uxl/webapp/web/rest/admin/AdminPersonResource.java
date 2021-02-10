package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.PersonActivationToken;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import com.redhat.uxl.webapp.web.rest.dto.PagedSearchDTO;
import com.redhat.uxl.webapp.web.rest.dto.PersonDTO;
import com.redhat.uxl.webapp.web.rest.dto.admin.DetailedPersonDTO;
import com.redhat.uxl.webapp.web.utils.RequestUtils;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Admin person resource.
 */
@RestController
@RequestMapping(value = "/api/admin/persons", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminPersonResource extends BaseResource {
    @Inject
    private PersonService personService;

    /**
     * Create person detailed person dto.
     *
     * @param personDTO           the person dto
     * @param sendActivationEmail the send activation email
     * @param request             the request
     * @return the detailed person dto
     */
    @ApiOperation(value = "TODO: SAC:", notes = "<p>TODO: SAC:</p>")
    @RequestMapping(method = RequestMethod.PUT)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public DetailedPersonDTO createPerson(@RequestBody PersonDTO personDTO,
            @RequestParam(value = "sendActivationEmail", defaultValue = "false") boolean sendActivationEmail,
            HttpServletRequest request) {
        Person createdPerson;
        Person person = personDTO.toPerson();

        person.setLangKey("en"); // TODO: SAC: how to deal with this?

        if (sendActivationEmail) {
            Person personInitiated = personService.getPersonByLogin(SecurityUtils.getCurrentLogin(), false);

            createdPerson = personService.createPerson(person, personDTO.getAuthorities(),
                    RequestUtils.getBaseUrl(request), personInitiated);
        } else {
            createdPerson = personService.createPerson(person, personDTO.getAuthorities());
        }

        return DetailedPersonDTO.valueOf(createdPerson, null); // TODO: SAC: add activation token
    }

    /**
     * Delete person.
     *
     * @param personId the person id
     */
    @ApiOperation(value = "Delete the given person.", notes = "<p>Delete the given person.</p>")
    @RequestMapping(value = "/{personId}", method = RequestMethod.DELETE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public void deletePerson(@PathVariable Long personId) {
        personService.deletePerson(personId);
    }

    /**
     * Delete persons.
     *
     * @param personIds the person ids
     */
    @ApiOperation(value = "Delete the given persons.", notes = "<p>Delete the given persons.</p>")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    void deletePersons(@RequestBody List<Long> personIds) {
        personService.deletePersons(personIds);
    }

    /**
     * Gets person.
     *
     * @param personId the person id
     * @return the person
     */
    @ApiOperation(value = "Get the person with given id value.", notes = "<p>Get the person with given id value.</p>")
    @RequestMapping(value = "/{personId}", method = RequestMethod.GET)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public DetailedPersonDTO getPerson(@PathVariable Long personId) {
        Person person = personService.getPersonWithAuthorities(personId);

        if (person == null) {
            throw new GeneralException(ErrorCodeGeneral.NOT_FOUND);
        }

        PersonActivationToken personActivationToken = personService.getActivationTokenByPersonId(personId);

        return DetailedPersonDTO.valueOf(person, personActivationToken);
    }

    /**
     * Update person detailed person dto.
     *
     * @param personId          the person id
     * @param detailedPersonDTO the detailed person dto
     * @return the detailed person dto
     */
    @ApiOperation(value = "TODO: SAC:", notes = "<p>TODO: SAC:</p>")
    @RequestMapping(value = "/{personId}", method = RequestMethod.PUT)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public DetailedPersonDTO updatePerson(@PathVariable Long personId,
            @RequestBody DetailedPersonDTO detailedPersonDTO) {
        if (detailedPersonDTO == null || detailedPersonDTO.getId() == null
                || !detailedPersonDTO.getId().equals(personId)) {
            throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST);
        }

        Person updatedPerson = personService.updatePerson(detailedPersonDTO.toPerson(personId),
                detailedPersonDTO.getAuthorities());

        return DetailedPersonDTO.valueOf(updatedPerson, null); // TODO: SAC: activationToken?
    }

    /**
     * Search persons page.
     *
     * @param pagedSearchDTO the paged search dto
     * @return the page
     */
    @ApiOperation(value = "Search for Persons.", notes = "<p>Search for Persons.</p>")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    Page<Person> searchPersons(@RequestBody PagedSearchDTO pagedSearchDTO) {
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "firstName");
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "lastName");

        Pageable pageable = pagedSearchDTO.getPageable(order1, order2);

        return personService.findForPagedSearch(pagedSearchDTO.getSearchOperation(), pagedSearchDTO.getSearchValue(),
                pageable);
    }
}
