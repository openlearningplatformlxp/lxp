package com.redhat.uxl.dataobjects.domain.dto;

import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;
import org.apache.commons.text.WordUtils;
import org.joda.time.DateTime;

/**
 * The type Totara event dto.
 */
@Data
public class TotaraEventDTO implements Searchable {
    /**
     * The constant AMERICA_NEW_YORK.
     */
    public static final String AMERICA_NEW_YORK = "America/New_York";
    /**
     * The constant USER_TIMEZONE.
     */
    public static final String USER_TIMEZONE = "99";
    private Long eventId;
    private Long courseId;
    private Long sessionId;
    private String name;
    private Long time;
    private Long duration;
    private String timezone;
    private String city;
    private String country;
    private String categoryname;

    /**
     * Gets event time.
     *
     * @return the event time
     */
    public Long getEventTime() {
        return new DateTime(time * 1000).getMillis();
        // .withZoneRetainFields(DateTimeZone.forTimeZone(TimeZone.getTimeZone(timezone)))
        // .withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone(AMERICA_NEW_YORK))).getMillis();
    }

    /**
     * Gets event end time.
     *
     * @return the event end time
     */
    public Long getEventEndTime() {
        return getEventTime() + duration * 1000;
    }

    /**
     * Require user timezone boolean.
     *
     * @return the boolean
     */
    public boolean requireUserTimezone() {
        return USER_TIMEZONE.equals(timezone);
    }
}
