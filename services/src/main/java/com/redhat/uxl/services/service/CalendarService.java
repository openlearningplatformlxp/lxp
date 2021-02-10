package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import com.redhat.uxl.services.service.dto.CalendarEventDTO;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * The interface Calendar service.
 */
public interface CalendarService {

    /**
     * Find calendar events by page page.
     *
     * @param personId    the person id
     * @param pageRequest the page request
     * @return the page
     */
    Page<TotaraEventDTO> findCalendarEventsByPage(Long personId, PageRequest pageRequest);

    /**
     * Find calendar events list.
     *
     * @param personId  the person id
     * @param startDate the start date
     * @param endDate   the end date
     * @param country   the country
     * @param city      the city
     * @return the list
     */
    List<CalendarEventDTO> findCalendarEvents(Long personId, DateTime startDate, DateTime endDate, String country,
            String city);

    /**
     * Fix user timezone.
     *
     * @param userId         the user id
     * @param courseDocument the course document
     */
    void fixUserTimezone(Long userId, CourseDocument courseDocument);
}
