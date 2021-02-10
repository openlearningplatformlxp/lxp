package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraEventDAO;
import com.redhat.uxl.datalayer.sql.totara.TotaraEventSQL;
import com.redhat.uxl.datalayer.utils.JodaBeanPropertyRowMapper;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Totara event dao.
 */
@Service
@ConditionalOnExpression("!${demo.mode.enabled:false}")
public class TotaraEventDAOImpl implements TotaraEventDAO {

  @Inject
  private JdbcTemplate totaraJdbcTemplate;

  @Override
  public List<TotaraEventDTO> findEventsForCalendar(Long personId, DateTime startDateTime,
      DateTime endDateTime, String country, String city) {
    if (StringUtils.isEmpty(country)) {
      country = "%";
    }
    if (StringUtils.isEmpty(city)) {
      city = "%";
    }
    return totaraJdbcTemplate.query(TotaraEventSQL.S_SELECT_EVENTS,
        new Object[] {startDateTime.toDate().getTime() / 1000,
            endDateTime.toDate().getTime() / 1000, startDateTime.toDate().getTime() / 1000,
            endDateTime.toDate().getTime() / 1000, country, city},
        new JodaBeanPropertyRowMapper<>(TotaraEventDTO.class));
  }

  @Override
  public Page<TotaraEventDTO> findActiveEvents(Pageable pageable) {
    int total = totaraJdbcTemplate.queryForObject(TotaraEventSQL.SQL_SELECT_COUNT_ACTIVE_EVENTS, new Object[] {},
        Integer.class);
    List<TotaraEventDTO> elements = new ArrayList<>();
    if (total > 0) {
      elements = totaraJdbcTemplate.query(TotaraEventSQL.SQL_SELECT_ALL_ACTIVE_EVENTS_PAGED,
          new Object[] {pageable.getPageSize(), pageable.getOffset()},
          new JodaBeanPropertyRowMapper<>(TotaraEventDTO.class));
    }
    return new PageImpl<>(elements, pageable, total);
  }

  @Override
  public Page<TotaraEventDTO> findUserEvents(Long userId, PageRequest pageRequest) {
    Long currentTime = new DateTime().getMillis() / 1000;
    int total = totaraJdbcTemplate.queryForObject(TotaraEventSQL.SQL_SELECT_COUNT_MY_UPCOMING_EVENTS,
        new Object[] {userId}, Integer.class);
    List<TotaraEventDTO> elements = new ArrayList<>();
    if (total > 0) {
      elements = totaraJdbcTemplate.query(TotaraEventSQL.SQL_SELECT_MY_UPCOMING_EVENTS,
          new Object[] {userId, pageRequest.getPageSize(), pageRequest.getOffset()},
          new JodaBeanPropertyRowMapper<>(TotaraEventDTO.class));
    }
    return new PageImpl<>(elements, pageRequest, total);
  }

  @Override
  public List<TotaraEventDTO> findEventLocations() {
    return totaraJdbcTemplate.query(TotaraEventSQL.SQL_SELECT_ALL_EVENT_LOCATIONS, new Object[] {},
        new JodaBeanPropertyRowMapper<>(TotaraEventDTO.class));
  }
}
