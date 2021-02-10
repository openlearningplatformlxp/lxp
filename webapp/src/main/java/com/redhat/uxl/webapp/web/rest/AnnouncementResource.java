package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.Announcement;
import com.redhat.uxl.services.service.AnnouncementService;
import com.redhat.uxl.webapp.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * The type Announcement resource.
 */
@RestController
@RequestMapping(value = "/api/announcement", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AnnouncementResource extends BaseResource {

    /**
     * The Announcement service.
     */
    @Inject
    AnnouncementService announcementService;

    /**
     * Gets announcement.
     *
     * @return the announcement
     */
    @ApiOperation(value = "Get announcement", notes = "<p>Get announcement</p>")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<Announcement> getAnnouncement() {
        log.debug("REST request to display a announcement");
        Long userId = Long.valueOf(SecurityUtils.getCurrentLoginAsLong());
        Announcement announcement = announcementService.pullAnnouncement(userId);
        if (announcement == null) {
            return new ResponseEntity<Announcement>(HttpStatus.OK);
        }
        return new ResponseEntity<Announcement>(announcement, HttpStatus.OK);
    }

    /**
     * Dismiss response entity.
     *
     * @param announcementId the announcement id
     * @return the response entity
     */
    @ApiOperation(value = "Dismiss announcement", notes = "<p>Dismiss announcement</p>")
    @RequestMapping(value = "/dismiss/{id}", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Announcement> dismiss(@PathVariable("id") Long announcementId) {
        log.debug("REST request to display a announcement");
        Long userId = Long.valueOf(SecurityUtils.getCurrentLogin());
        announcementService.markAsRead(userId, announcementId);
        // read a new announcement
        return getAnnouncement();
    }

}
