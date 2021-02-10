package com.redhat.uxl.services.service.dto;

import com.redhat.uxl.dataobjects.domain.types.AppointmentItemType;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Appointment item dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentItemDTO implements Serializable {
    private Long courseId;
    private String title;
    private Integer durationMinutes;
    private AppointmentItemType itemType;

    private boolean isInProgress = false;
    private boolean isComplete = false;
    private boolean isEnrolled = false;

    /**
     * Instantiates a new Appointment item dto.
     *
     * @param courseId        the course id
     * @param title           the title
     * @param durationMinutes the duration minutes
     * @param itemType        the item type
     */
    public AppointmentItemDTO(Long courseId, String title, Integer durationMinutes, AppointmentItemType itemType) {
        this.courseId = courseId;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.itemType = itemType;
    }
}
