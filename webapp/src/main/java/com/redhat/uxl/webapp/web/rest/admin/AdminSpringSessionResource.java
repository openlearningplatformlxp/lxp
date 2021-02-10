package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.SpringSession;
import com.redhat.uxl.services.service.SpringSessionService;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.web.rest.dto.PagedSearchDTO;
import io.swagger.annotations.ApiOperation;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Admin spring session resource.
 */
@RestController
@RequestMapping(value = "/api/admin/spring-sessions", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminSpringSessionResource {
    @Inject
    private SpringSessionService springSessionService;

    /**
     * Search spring sessions page.
     *
     * @param pagedSearchDTO the paged search dto
     * @return the page
     */
    @ApiOperation(value = "Search for Spring Sessions.", notes = "<p>Search for Spring Sessions.</p>")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    Page<SpringSession> searchSpringSessions(@RequestBody PagedSearchDTO pagedSearchDTO) {
        return springSessionService.findForPagedSearch(pagedSearchDTO.getSearchOperation(),
                pagedSearchDTO.getSearchValue(), pagedSearchDTO.getPageable());
    }
}
