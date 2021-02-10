package com.redhat.uxl.services.service;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Announcement service.
 */
public interface AnnouncementService {

    /**
     * Pull announcement announcement.
     *
     * @param personTotaraId the person totara id
     * @return the announcement
     */
    @Timed
    @Transactional(readOnly = true)
    Announcement pullAnnouncement(Long personTotaraId);

    /**
     * Mark as read.
     *
     * @param personTotaraId the person totara id
     * @param announcementId the announcement id
     */
    @Timed
    @Transactional
    void markAsRead(Long personTotaraId, Long announcementId);

    /**
     * Find page by id announcement.
     *
     * @param id the id
     * @return the announcement
     */
    @Timed
    @Transactional(readOnly = true)
    Announcement findPageById(Long id);

    /**
     * Find for paged search page.
     *
     * @param searchOperation the search operation
     * @param searchValue     the search value
     * @param pageable        the pageable
     * @return the page
     */
    @Timed
    @Transactional(readOnly = true)
    Page<Announcement> findForPagedSearch(String searchOperation, String searchValue, Pageable pageable);

    /**
     * Create announcement announcement.
     *
     * @param announcement the announcement
     * @return the announcement
     */
    @Timed
    @Transactional
    Announcement createAnnouncement(Announcement announcement);

    /**
     * Update announcement announcement.
     *
     * @param announcement the announcement
     * @return the announcement
     */
    @Timed
    @Transactional
    Announcement updateAnnouncement(Announcement announcement);

    /**
     * Delete announcement.
     *
     * @param id the id
     */
    @Timed
    @Transactional
    void deleteAnnouncement(Long id);
}
