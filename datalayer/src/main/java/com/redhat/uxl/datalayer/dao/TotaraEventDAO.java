package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * The interface Totara event dao.
 */
public interface TotaraEventDAO {

    /**
     * Find events for calendar list.
     *
     * @param personId      the person id
     * @param startDateTime the start date time
     * @param endDateTime   the end date time
     * @param country       the country
     * @param city          the city
     * @return the list
     */
    List<TotaraEventDTO> findEventsForCalendar(Long personId, DateTime startDateTime, DateTime endDateTime, String country,
            String city);

    /**
     * Find active events page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<TotaraEventDTO> findActiveEvents(Pageable pageable);

    /**
     * Find user events page.
     *
     * @param userId      the user id
     * @param pageRequest the page request
     * @return the page
     */
    Page<TotaraEventDTO> findUserEvents(Long userId, PageRequest pageRequest);

    /**
     * Find event locations list.
     *
     * @return the list
     */
    List<TotaraEventDTO> findEventLocations();
}
