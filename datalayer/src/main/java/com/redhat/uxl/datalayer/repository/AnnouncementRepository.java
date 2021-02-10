package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.Announcement;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Announcement repository.
 */
public interface AnnouncementRepository
        extends BaseJpaRepository<Announcement, Long>, JpaSpecificationExecutor<Announcement> {

    /**
     * Find recent unread announcement.
     *
     * @param totaraPersonId the totara person id
     * @return the announcement
     */
    @Query(value = "select a.* from announcement a" + "           where a.id not in"
            + "                 ( SELECT pa.announcement_id from person_announcement pa where pa.person_totara_id = (?1) ) "
            + "           order by a.created_date limit 1", nativeQuery = true)
    Announcement findRecentUnread(Long totaraPersonId);
}
