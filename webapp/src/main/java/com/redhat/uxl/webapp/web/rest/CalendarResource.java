package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.CalendarService;
import com.redhat.uxl.services.service.dto.CalendarEventDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Calendar resource.
 */
@RestController
@RequestMapping(value = "/api/calendar", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CalendarResource extends BaseResource {

    /**
     * The Calendar service.
     */
    @Inject
    CalendarService calendarService;

    /**
     * Gets calendar events.
     *
     * @param start   the start
     * @param end     the end
     * @param country the country
     * @param city    the city
     * @return the calendar events
     */
    @ApiOperation(value = "Get calendar events", notes = "<p>Get calendar events.</p>")
    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public List<CalendarEventDTO> getCalendarEvents(@RequestParam("start") String start,
            @RequestParam("end") String end, @RequestParam(value = "country", defaultValue = "") String country,
            @RequestParam(value = "city", defaultValue = "") String city) {
        log.debug("REST request to get calendar events between: " + start + " and " + end);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime startTime = formatter.parseDateTime(start);
        DateTime endTime = formatter.parseDateTime(end);

        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        return calendarService.findCalendarEvents(currentUserId, startTime, endTime, country, city);
    }

}
