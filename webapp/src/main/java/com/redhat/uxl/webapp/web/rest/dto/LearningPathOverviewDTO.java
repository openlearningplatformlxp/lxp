package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseSetDTO;
import com.redhat.uxl.services.service.dto.*;
import java.util.List;
import lombok.Data;

/**
 * The type Learning path overview dto.
 */
@Data
public class LearningPathOverviewDTO {
    private ProgramItemDTO program;
    private List<ProgramCourseSetDTO> courseSets = null;
    private List<AppointmentItemDTO> nextAppointments = null;
    private List<CourseTagDTO> tagsTypes = null;
    private List<TeamMemberDTO> teamMembers;
    private HtmlBlockDTO htmlBlock;
    private int percentComplete = 0;
    private int totalMinutes = 0;
    private int totalNumOfCourses;

    /**
     * Increase courses.
     *
     * @param size the size
     */
    public void increaseCourses(int size) {
        totalNumOfCourses += size;
    }
}
