package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.datalayer.dao.TotaraEventDAO;
import com.redhat.uxl.datalayer.dao.TotaraProfileDAO;
import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.services.service.CalendarService;
import com.redhat.uxl.services.service.dto.CalendarEventDTO;
import java.util.List;
import java.util.TimeZone;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * The type Calendar service.
 */
@Service
public class CalendarServiceImpl implements CalendarService {

    /**
     * The Totara profile dao.
     */
    @Inject
  TotaraProfileDAO totaraProfileDAO;
    /**
     * The Totara event dao.
     */
    @Inject
  TotaraEventDAO totaraEventDAO;

  @Override
  public Page<TotaraEventDTO> findCalendarEventsByPage(Long personId, PageRequest pageRequest) {
    TotaraUserDTO tUser = totaraProfileDAO.getUserProfile(personId);
    Page<TotaraEventDTO> events = totaraEventDAO.findUserEvents(personId, pageRequest);
    convertUserTimezones(tUser, events.getContent());
    return events;
  }

  @Override
  public List<CalendarEventDTO> findCalendarEvents(Long personId, DateTime startDate,
      DateTime endDate, String country, String city) {
    TotaraUserDTO tUser = totaraProfileDAO.getUserProfile(personId);
    List<TotaraEventDTO> events =
        totaraEventDAO.findEventsForCalendar(personId, startDate, endDate, country, city);
    convertUserTimezones(tUser, events);
    return CalendarEventDTO.convertTotaraEvents(events);
  }

  @Override
  public void fixUserTimezone(Long personId, CourseDocument courseDocument) {
    TotaraUserDTO tUser = totaraProfileDAO.getUserProfile(personId);
    courseDocument.setEventTimezone(tUser.getTimezone());
    courseDocument.setEventTime(new DateTime(courseDocument.getEventTime())
        .withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone(courseDocument.getEventTimezone())))
        .getMillis());
  }

  private void convertUserTimezones(TotaraUserDTO tUser, List<TotaraEventDTO> events) {
    events.stream().filter(TotaraEventDTO::requireUserTimezone).forEach(t -> {
      t.setTimezone(tUser.getTimezone());
      t.setTime(new DateTime(t.getTime())
          .withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone(t.getTimezone()))).getMillis());
    });
  }
}
