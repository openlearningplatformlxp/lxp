package com.redhat.uxl.dataobjects.domain.dto;

import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

/**
 * The type Totara course dto.
 */
@Data
public class TotaraCourseDTO implements Searchable {

    /**
     * The Id.
     */
    Long id;
    /**
     * The Full name.
     */
    String fullName;
    /**
     * The Short name.
     */
    String shortName;
    /**
     * The Summary.
     */
    String summary;
    /**
     * The Enrollment id.
     */
    Long enrollmentId;
    /**
     * The Course id.
     */
    Long courseId;
    /**
     * The Status.
     */
    Long status;
    /**
     * The Course type.
     */
    Integer courseType;
    /**
     * The Due date.
     */
    Long dueDate;
    /**
     * The Completed date.
     */
    Long completedDate;
    /**
     * The Time created.
     */
    Long timeCreated;
    /**
     * The Duration.
     */
    String duration;
    /**
     * The Ce credits.
     */
    Integer ceCredits;
    /**
     * The Time enrolled.
     */
    Long timeEnrolled;

}
