package com.redhat.uxl.services.service.dto;

import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import com.redhat.uxl.dataobjects.domain.types.CalendarColorType;
import com.redhat.uxl.dataobjects.domain.types.CalendarType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * The type Calendar event dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventDTO implements Serializable {
    private Long id;
    private String title;
    private DateTime start;
    private DateTime end;
    private boolean allDay = false;
    private String country;
    private String city;
    private CalendarType type;
    private String backgroundColor;
    private String borderColor;
    private String categoryName;

    /**
     * Convert totara events list.
     *
     * @param events the events
     * @return the list
     */
    public static List<CalendarEventDTO> convertTotaraEvents(List<TotaraEventDTO> events) {
        List<CalendarEventDTO> calendarEvents = new ArrayList<>();
        for (TotaraEventDTO event : events) {
            // Must convert durations to individual days
            DateTime startDate = new DateTime(event.getEventTime());
            DateTime endDate = new DateTime(event.getEventEndTime());
            long p2 = Days.daysBetween(startDate.toLocalDate(), endDate.toLocalDate()).getDays();
            if (p2 > 0) {
                // Add current date
                CalendarEventDTO dto = createCalendarEventDTO(event, startDate);
                calendarEvents.add(dto);
                // Add others
                for (int i = 0; i < p2; i++) {
                    startDate = startDate.plusDays(1);
                    CalendarEventDTO dto2 = createCalendarEventDTO(event, startDate);
                    calendarEvents.add(dto2);
                }

            } else {
                // Single day event
                CalendarEventDTO dto = createCalendarEventDTO(event, new DateTime(event.getEventTime()));
                calendarEvents.add(dto);
            }
        }
        return calendarEvents;
    }

    private static CalendarEventDTO createCalendarEventDTO(TotaraEventDTO event, DateTime startDate) {
        CalendarEventDTO dto2 = new CalendarEventDTO();
        dto2.setId(event.getCourseId());
        dto2.setTitle(event.getName());
        dto2.setType(CalendarType.buildFrom(event.getCategoryname()));
        dto2.setBackgroundColor(CalendarColorType.buildFrom(dto2.getType()).getBackgroundColor());
        dto2.setBorderColor(CalendarColorType.buildFrom(dto2.getType()).getBorderColor());
        dto2.setCategoryName(event.getCategoryname());
        dto2.setCountry(event.getCountry());
        dto2.setCity(event.getCity());
        dto2.setStart(startDate);
        return dto2;
    }
}
