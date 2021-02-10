package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.repository.AuthorityRepository;
import com.redhat.uxl.datalayer.repository.PersonRepository;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.web.rest.util.HeaderUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The type Person resource.
 */
@RestController
@RequestMapping(value = "/api/persons", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class PersonResource extends BaseResource {
    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private PersonRepository personRepository;

    @Inject
    private PersonService personService;

    /**
     * The constant currentUserId.
     */
    public static Long currentUserId = 0L;

    /**
     * Switch person.
     *
     * @param userId the user id
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Change who you are logged in as.", notes = "<p>Change who you are logged in as</p>")
    @RequestMapping(value = "/switch/{userId}", method = RequestMethod.GET)
    @Timed
    public void switchPerson(@PathVariable Long userId) throws URISyntaxException {
    }

    /**
     * Create person response entity.
     *
     * @param person the person
     * @return the response entity
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Create a new Person.", notes = "<p>Create a new Person.</p>")
    @RequestMapping(method = RequestMethod.POST)
    @Secured(AuthoritiesConstants.ADMIN)
    @Timed
    public ResponseEntity<Person> createPerson(@RequestBody Person person) throws URISyntaxException {
        log.debug("REST request to save Person: {}", person);
        if (person.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new person cannot already have an ID.").body(null);
        }

        Person result = personRepository.save(person);
        return ResponseEntity.created(new URI("/api/persons/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("person", result.getId().toString())).body(result);
    }
}
