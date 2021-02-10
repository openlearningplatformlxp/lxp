package com.redhat.uxl.services.service.demo.impl;

import com.redhat.uxl.datalayer.dao.TotaraEventDAO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import com.redhat.uxl.services.service.demo.dto.ActiveEventDTO;
import com.redhat.uxl.services.service.demo.dto.UserEventDTO;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The type Demo totara event dao.
 */
@Slf4j
@Service
@ConditionalOnExpression("${demo.mode.enabled:true}")
public class DemoTotaraEventDAOImpl implements TotaraEventDAO {
    /**
     * The Demo utility service.
     */
    @Inject DemoUtilityService demoUtilityService;

  @Override
  public List<TotaraEventDTO> findEventsForCalendar(Long personId, DateTime startDateTime,
      DateTime endDateTime, String country, String city) {
    List<TotaraEventDTO> elements = demoUtilityService.getAllEvents();
    List<UserEventDTO> userEvents = demoUtilityService.getUserEvents();
    List<Long> ids = new ArrayList<>();
    List<TotaraEventDTO> result = new ArrayList<>();

    for (UserEventDTO pair : userEvents) {
      if (personId.equals(pair.getUserId())) {
        ids.add(pair.getEventId());
      }
    }

    for (TotaraEventDTO ev : elements) {
      for (Long id : ids) {
        if (ev.getEventId().equals(id) && startDateTime.isBefore(ev.getEventTime())
            && endDateTime.isAfter(ev.getEventEndTime()) && country.equals(ev.getCountry())
            && city.equals(ev.getCity())) {
          result.add(ev);
        }
      }
    }

    return result;
  }

  @Override
  public Page<TotaraEventDTO> findActiveEvents(Pageable pageable) {
    List<TotaraEventDTO> all = demoUtilityService.getAllEvents();
    List<ActiveEventDTO> actives = demoUtilityService.getActiveEvents();
    List<TotaraEventDTO> result = new ArrayList<>();
    for (ActiveEventDTO a : actives) {
      for (Long id : a.getEventIds()) {
        for (TotaraEventDTO event : all) {
          if (event.getEventId().equals(id)) {
            result.add(event);
          }
        }
      }
    }
    return new PageImpl<>(result, pageable, result.size());
  }

  @Override
  public Page<TotaraEventDTO> findUserEvents(Long userId, PageRequest pageRequest) {
    List<UserEventDTO> events = demoUtilityService.getUserEvents();
    List<TotaraEventDTO> all = demoUtilityService.getAllEvents();
    List<TotaraEventDTO> result = new ArrayList<>();
    for (UserEventDTO ue : events) {
      if (ue.getUserId().equals(userId)) {
        for (TotaraEventDTO te : all) {
          if (te.getEventId().equals(ue.getEventId())) {
            result.add(te);
          }
        }
      }

    }
    return new PageImpl<>(result, pageRequest, result.size());
  }

  @Override
  public List<TotaraEventDTO> findEventLocations() {
    return demoUtilityService.getAllEvents();
  }
}
