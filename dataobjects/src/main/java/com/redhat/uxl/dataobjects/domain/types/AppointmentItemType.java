package com.redhat.uxl.dataobjects.domain.types;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The enum Appointment item type.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AppointmentItemType {
    /**
     * Course appointment item type.
     */
    COURSE,
    /**
     * Reading appointment item type.
     */
    READING,
    /**
     * Scorm appointment item type.
     */
    SCORM,
    /**
     * Quiz appointment item type.
     */
    QUIZ,
    /**
     * Forum appointment item type.
     */
    FORUM,
    /**
     * Video appointment item type.
     */
    VIDEO;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
    return this.name();
  }
}
