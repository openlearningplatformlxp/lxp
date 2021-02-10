package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.Announcement;
import com.redhat.uxl.services.service.AnnouncementService;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
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
 * The type Admin announcement resource.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/admin/announcements", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminAnnouncementResource extends BaseResource {
    @Inject
    private AnnouncementService announcementService;

    /**
     * Search announcements page.
     *
     * @param pagedSearchDTO the paged search dto
     * @return the page
     */
    @ApiOperation(value = "Search for Announcements.", notes = "<p>Search for Announcements.</p>")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    Page<Announcement> searchAnnouncements(@RequestBody PagedSearchDTO pagedSearchDTO) {
        return announcementService.findForPagedSearch(pagedSearchDTO.getSearchOperation(),
                pagedSearchDTO.getSearchValue(), pagedSearchDTO.getPageable());
    }

    /**
     * Gets page.
     *
     * @param id the id
     * @return the page
     */
    @ApiOperation(value = "Get announcement", notes = "<p>Get announcement</p>")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    public Announcement getPage(@PathVariable("id") Long id) {
        log.debug("REST request to display a announcement");
        return announcementService.findPageById(id);
    }

    /**
     * Save announcement announcement.
     *
     * @param announcement the announcement
     * @return the announcement
     */
    @ApiOperation(value = "Create a announcement on the system", notes = "<p>Create a announcement on the system</p>")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    public Announcement saveAnnouncement(@RequestBody Announcement announcement) {
        return announcementService.createAnnouncement(announcement);
    }

    /**
     * Update announcement announcement.
     *
     * @param announcement the announcement
     * @return the announcement
     */
    @ApiOperation(value = "Update a announcement on the system", notes = "<p>Update a announcement on the system</p>")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    public Announcement updateAnnouncement(@RequestBody Announcement announcement) {
        Announcement storedAnnouncement = announcementService.findPageById(announcement.getId());
        storedAnnouncement.setMessage(announcement.getMessage());
        storedAnnouncement.setLinkUrl(announcement.getLinkUrl());
        storedAnnouncement.setLinkText(announcement.getLinkText());
        return announcementService.updateAnnouncement(storedAnnouncement);
    }

    /**
     * Delete announcement.
     *
     * @param id the id
     */
    @ApiOperation(value = "Delete announcement", notes = "<p>Delete announcement</p>")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    public void deleteAnnouncement(@PathVariable("id") Long id) {
        log.debug("REST request to delete a announcement");
        announcementService.deleteAnnouncement(id);
    }
}
