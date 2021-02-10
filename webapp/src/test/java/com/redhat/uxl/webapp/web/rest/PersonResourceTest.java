package com.redhat.uxl.webapp.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.redhat.uxl.datalayer.repository.PersonRepository;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.webapp.Application;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * The type Person resource test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
// TODO: WJK: Determine what to replace this with
//@IntegrationTest
public class PersonResourceTest {

    @Inject
    private PersonRepository personRepository;

    @Inject
    private PersonService personService;

    private MockMvc restPersonMockMvc;

    /**
     * Sets .
     */
    @Before
    public void setup() {
        PersonResource personResource = new PersonResource();
        ReflectionTestUtils.setField(personResource, "personRepository", personRepository);
        ReflectionTestUtils.setField(personResource, "personService", personService);
        this.restPersonMockMvc = MockMvcBuilders.standaloneSetup(personResource).build();
    }

    /**
     * Test get unknown person.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetUnknownPerson() throws Exception {
        restPersonMockMvc.perform(get("/api/admin/persons/9999") // TODO: SAC: this is not a good test
                                                                 // -> hard-coded id
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }
}
