package com.redhat.uxl.dataobjects.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.redhat.uxl.dataobjects.domain.ProgramSectionCourse;
import com.redhat.uxl.dataobjects.domain.types.ContentSourceType;
import com.redhat.uxl.dataobjects.domain.types.ProgramCourseType;
import java.util.Date;
import lombok.Data;

/**
 * The type Program course dto.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramCourseDTO {

    private Long id;
    private ProgramCourseType type;
    private ContentSourceType cms;
    private Long sectionId;
    private String fullName;
    private String description;
    private String contentType;
    private String contentCaption;
    private Long activityId;
    private String activityMessage;
    private String activityValue;
    private Long itemId;
    private Integer sortOrder;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private Date dueDate;

    private ProgramCourseStatusDTO status;

    /**
     * Instantiates a new Program course dto.
     */
    public ProgramCourseDTO() {

    }

    /**
     * Instantiates a new Program course dto.
     *
     * @param c the c
     */
    public ProgramCourseDTO(ProgramSectionCourse c) {
        this.id = c.getId();
        this.sectionId = c.getSectionId();
        this.cms = ContentSourceType.valueOf(c.getCms());
        this.type = ProgramCourseType.valueOf(c.getType());
        this.contentCaption = this.type.getDescription();
        this.itemId = c.getItemId();
        this.fullName = c.getTitle();
        this.description = c.getDescription();
        this.dueDate = c.getDueDate();
        this.activityValue = c.getUrl();
    }

    /**
     * Gets external id.
     *
     * @return the external id
     */
    public String getExternalId() {
        if (this.cms != null) {
            return this.cms.name() + "{}" + this.id;
        }
        return null;
    }

    /**
     * Is manual completable boolean.
     *
     * @return the boolean
     */
    public boolean isManualCompletable() {
        return !ContentSourceType.LMS.equals(cms) || ProgramCourseType.OFFLINE_TASK.equals(type)
                || ProgramCourseType.WIKIPAGE.equals(type) || ProgramCourseType.WEB_TASK.equals(type);
    }

    /**
     * Gets course name.
     *
     * @return the course name
     */
    public String getCourseName() {
        switch (type) {
        case OFFLINE_TASK:
            return "Offline Task: " + getFullName();
        case WEB_TASK:
            return "Web Activity: " + getFullName();
        case COURSE:
            return getFullName();
        }
        return null;
    }
}
