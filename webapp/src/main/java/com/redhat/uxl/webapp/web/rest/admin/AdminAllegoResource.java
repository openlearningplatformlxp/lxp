package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.solr.strategies.AllegoCourseSolrPagerServiceImpl;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import io.swagger.annotations.ApiOperation;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Admin allego resource.
 */
@RestController
@RequestMapping(value = "/api/admin/allego", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminAllegoResource extends BaseResource {

    /**
     * The Allego course solr pager service.
     */
    @Inject
    AllegoCourseSolrPagerServiceImpl allegoCourseSolrPagerService;

    /**
     * Index allego int.
     *
     * @return the int
     */
    @ApiOperation(value = "Search for Allego stuff.", notes = "<p>Search for Feedback.</p>")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    int indexAllego() {
        allegoCourseSolrPagerService.indexAll(100, 0);
        return -1;
    }
}
