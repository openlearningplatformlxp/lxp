package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.FeaturedSearchService;
import com.redhat.uxl.services.service.dto.FeaturedSearchDTO;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import com.redhat.uxl.webapp.web.rest.dto.PagedSearchDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

/**
 * The type Admin featured search resource.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/admin/featured-searches", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminFeaturedSearchResource extends BaseResource {

    @Inject
    private FeaturedSearchService featuredSearchService;

    /**
     * Search featured searches page.
     *
     * @param pagedSearchDTO the paged search dto
     * @return the page
     */
    @ApiOperation(value = "Search for Featured Searches.", notes = "<p>Search for Featured Searches.</p>")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    Page<FeaturedSearchDTO> searchFeaturedSearches(@RequestBody PagedSearchDTO pagedSearchDTO) {

        return featuredSearchService.findForPagedSearch(pagedSearchDTO.getSearchOperation(),
                pagedSearchDTO.getSearchValue(), pagedSearchDTO.getPageable());
    }

    /**
     * Gets page.
     *
     * @param id the id
     * @return the page
     */
    @ApiOperation(value = "Get featured search", notes = "<p>Get featured search</p>")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    public FeaturedSearchDTO getPage(@PathVariable("id") Long id) {
        log.debug("REST request to display a featured search");
        return featuredSearchService.findFeaturedSearchById(id);
    }

    /**
     * Save featured search featured search dto.
     *
     * @param dto the dto
     * @return the featured search dto
     */
    @ApiOperation(value = "Create a featured search on the system", notes = "<p>Create a featured search on the system</p>")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    public FeaturedSearchDTO saveFeaturedSearch(@RequestBody FeaturedSearchDTO dto) {
        Long userId = Long.valueOf(SecurityUtils.getCurrentLogin());
        return featuredSearchService.createFeaturedSearch(userId, dto);
    }

    /**
     * Update featured search featured search dto.
     *
     * @param dto the dto
     * @return the featured search dto
     */
    @ApiOperation(value = "Update a featured search on the system", notes = "<p>Update a featured search on the system</p>")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    public FeaturedSearchDTO updateFeaturedSearch(@RequestBody FeaturedSearchDTO dto) {
        Long userId = Long.valueOf(SecurityUtils.getCurrentLogin());
        return featuredSearchService.updateFeaturedSearch(userId, dto);
    }

    /**
     * Delete search.
     *
     * @param id the id
     */
    @ApiOperation(value = "Delete featured search", notes = "<p>Delete featured search</p>")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    public void deleteSearch(@PathVariable("id") Long id) {
        log.debug("REST request to delete a featured search");
        featuredSearchService.deleteFeatureSearch(id);
    }
}
