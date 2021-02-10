package com.redhat.uxl.datalayer.dto;

import java.util.Map;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * The type Enrolled totara course dto.
 */
@Data
public class EnrolledTotaraCourseDTO {

    private final int COMPLETE = 50;

    private final int IN_PROGRESS = 25;

    private final int NOT_STARTED = 10;

    private final int RPL = 75;

    private Long courseID;

    private String courseFullName;

    private String description;

    private DateTime timeCompleted;

    private DateTime timeStarted;

    private DateTime timeEnrolled;

    private String status;

    private Long requiredActivitiesCount;

    private Long completedActivitiesCount;

    private Double completionPercentage;

    private DateTime dueDate;

    /**
     * Instantiates a new Enrolled totara course dto.
     *
     * @param course the course
     */
    public EnrolledTotaraCourseDTO(Map<String, Object> course) {
        this.setCourseID((Long) course.get("id"));
        this.setCourseFullName(course.get("fullname").toString());
        this.setDescription(course.get("description").toString());
        if (course.get("timecompleted") != null) {
            Long completedDate = (Long) course.get("timecompleted");
            if (completedDate > 0) {
                this.setTimeCompleted(new DateTime(completedDate * 1000));
            }
        }
        if (course.get("timestarted") != null) {
            Long timestarted = (Long) course.get("timestarted");
            if (timestarted > 0) {
                this.setTimeStarted(new DateTime(timestarted * 1000));
            }
        }
        if (course.get("timeenrolled") != null) {
            Long timeenrolled = (Long) course.get("timeenrolled");
            if (timeenrolled > 0) {
                this.setTimeEnrolled(new DateTime(timeenrolled * 1000));
            }
        }
        if (course.get("duedate") != null) {
            Long endDate = (Long) course.get("duedate");
            if (endDate > 0) {
                this.setDueDate(new DateTime(endDate * 1000));
            }
        }
        Integer status = (Integer) course.get("status");
        switch (status) {
        case COMPLETE:
        case RPL:
            this.setStatus("COMPLETE");
            break;
        case IN_PROGRESS:
            this.setStatus("IN_PROGRESS");
            break;
        case NOT_STARTED:
        default:
            this.setStatus("NOT_STARTED");
            break;
        }

        this.setRequiredActivitiesCount((Long) course.get("required_activities_count"));
        this.setCompletedActivitiesCount((Long) course.get("completed_activities_count"));
        this.setCompletionPercentage((Double) course.get("completion_percentage"));
    }
}
