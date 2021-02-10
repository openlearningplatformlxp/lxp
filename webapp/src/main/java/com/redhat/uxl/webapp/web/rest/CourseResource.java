package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.dao.TotaraTagDAO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import com.redhat.uxl.services.service.CourseService;
import com.redhat.uxl.services.service.ProgramItemService;
import com.redhat.uxl.services.service.ProgramStatisticsService;
import com.redhat.uxl.services.service.TeamService;
import com.redhat.uxl.services.service.TotaraCourseService;
import com.redhat.uxl.services.service.TotaraProgramService;
import com.redhat.uxl.services.service.dto.AppointmentItemDTO;
import com.redhat.uxl.services.service.dto.CourseDTO;
import com.redhat.uxl.services.service.dto.CourseOverviewDTO;
import com.redhat.uxl.services.service.dto.CourseProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.CourseSectionsDTO;
import com.redhat.uxl.services.service.dto.CourseSetProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.CourseTagDTO;
import com.redhat.uxl.services.service.dto.LearningPathDTO;
import com.redhat.uxl.services.service.dto.LearningPathProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.ProgramItemDTO;
import com.redhat.uxl.services.service.dto.TotaraCourseDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.dto.CourseTextEntryDTO;
import com.redhat.uxl.webapp.web.rest.dto.CourseUpcomingDTO;
import com.redhat.uxl.webapp.web.rest.dto.LearningPathOverviewDTO;
import com.redhat.uxl.webapp.web.rest.dto.SuccessDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Course resource.
 */
@RestController
@RequestMapping(value = "/api/course", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CourseResource {

    /**
     * The Course service.
     */
    @Inject
    CourseService courseService;

    /**
     * The Totara course service.
     */
    @Inject
    TotaraCourseService totaraCourseService;

    /**
     * The Totara tag dao.
     */
    @Inject
    TotaraTagDAO totaraTagDAO;

    /**
     * The Program item service.
     */
    @Inject
    ProgramItemService programItemService;

    /**
     * The Program statistics service.
     */
    @Inject
    ProgramStatisticsService programStatisticsService;

    /**
     * The Totara program service.
     */
    @Inject
    TotaraProgramService totaraProgramService;

    /**
     * The Team service.
     */
    @Inject
    TeamService teamService;

    /**
     * Retrieve courses list.
     *
     * @return the list
     */
    @ApiOperation(value = "Retrieve All Visible Courses", notes = "<p>Retrieve All Visible Courses.</p>")
    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public List<TotaraCourseDTO> retrieveCourses() {

        return totaraCourseService.findActiveCourses();
    }

    /**
     * Retrieve program learning path overview dto.
     *
     * @param programId the program id
     * @return the learning path overview dto
     */
    @ApiOperation(value = "Retrieve Program Overview", notes = "<p>Retrieve Learning Path Overview</p>")
    @RequestMapping(value = "/program/{programId}", method = RequestMethod.GET)
    @Timed
    public LearningPathOverviewDTO retrieveProgram(@PathVariable Long programId) {
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        LearningPathOverviewDTO overviewDTO = new LearningPathOverviewDTO();

        ProgramItemDTO prog = programItemService.findByProgramId(personTotaraId, programId);

        prog.setEnrolled(totaraProgramService.isUserEnrolledInProgram(personTotaraId, programId));

        if (!prog.isEnrolled()) {
            // If we are not enrolled, check to see if we should show enrolled button
            if (prog.getAudienceVisible() != 2) {
                // This is a restricted course
                if (prog.getAudienceVisible() == 1) {
                    // Find if user is allowed
                    Long userAllowedId = programItemService.findAllowedUserAudienceByUserId(personTotaraId, programId);
                    prog.setEnrolSelfEnabled(userAllowedId > 0);
                } else {
                    prog.setEnrolSelfEnabled(false);
                }
            } else {
                // prog.setEnrolSelfEnabled(true);
            }
            // Need to handle audience self enrollment
        }

        overviewDTO.setProgram(prog);
        if (prog.getHidesets() == null || !prog.getHidesets().equalsIgnoreCase("1")) {
            overviewDTO.setCourseSets(totaraCourseService.getProgramCourseSets(personTotaraId, programId));

            overviewDTO.setNextAppointments(programItemService.determineAppointments(overviewDTO.getCourseSets()));
        }

        overviewDTO.setHtmlBlock(programItemService.findHtmlBlockByProgramId(programId));

        // Lets do Tags
        List<TotaraTagDTO> tags = totaraTagDAO.findTagsForProgramWithParent(programId);
        if (tags != null) {

            overviewDTO.setTagsTypes(new ArrayList<>());
            // Create array
            String tagname = "";
            CourseTagDTO cdt = null;
            for (TotaraTagDTO tag : tags) {
                if (tag.getName().equalsIgnoreCase(tagname)) {
                    cdt.getTags().add(tag.getRawname());
                } else {
                    if (cdt != null)
                        overviewDTO.getTagsTypes().add(cdt);
                    tagname = tag.getName();
                    cdt = new CourseTagDTO();
                    cdt.setName(tag.getName());
                    cdt.getTags().add(tag.getRawname());

                }
            }
            // Add final
            if (cdt != null)
                overviewDTO.getTagsTypes().add(cdt);
        }

        LearningPathProgressionOverviewDTO myOverviewDTO = programStatisticsService.getProgramStatistics(programId,
                personTotaraId);
        log.debug("Percent complete: " + myOverviewDTO.getPercentComplete());

        overviewDTO.setPercentComplete(myOverviewDTO.getPercentComplete().multiply(new BigDecimal(100)).intValue());
        if (overviewDTO.getNextAppointments() != null) {
            for (AppointmentItemDTO aDto : overviewDTO.getNextAppointments()) {
                BigDecimal duration = myOverviewDTO.getDurationMap().get(aDto.getCourseId());
                aDto.setDurationMinutes((duration != null) ? duration.multiply(new BigDecimal(60)).intValue() : 0);
            }
        }

        overviewDTO.setTotalMinutes((myOverviewDTO.getTotalCourseDuration() != null)
                ? myOverviewDTO.getTotalCourseDuration().multiply(new BigDecimal(60)).intValue() : 0);
        if (overviewDTO.getCourseSets() != null) {
            overviewDTO.setTotalNumOfCourses(0);
            overviewDTO.getCourseSets().forEach(cs -> {
                if (cs.getCourses() != null) {
                    overviewDTO.increaseCourses(cs.getCourses().size());
                }
            });
        }

        overviewDTO.setTotalNumOfCourses(myOverviewDTO.getCourseCount().intValue());
        return overviewDTO;
    }

    /**
     * Update text extry success dto.
     *
     * @param courseId           the course id
     * @param courseTextEntryDTO the course text entry dto
     * @return the success dto
     * @throws Exception the exception
     */
    @ApiOperation(value = "Save text entry from program activity", notes = "<p>Save text entry from program activty</p>")
    @RequestMapping(value = "/programs/{courseId}/textentry", method = RequestMethod.POST)
    @Timed
    public SuccessDTO updateTextExtry(@PathVariable Long courseId, @RequestBody CourseTextEntryDTO courseTextEntryDTO)
            throws Exception {
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        totaraCourseService.updateTextEntryResult(personTotaraId, courseTextEntryDTO.getActivityId(),
                courseTextEntryDTO.getTextEntryId(), courseTextEntryDTO.getMessage());

        return new SuccessDTO();
    }

    /**
     * Retrieve programs learning path dto.
     *
     * @param userId    the user id
     * @param programId the program id
     * @return the learning path dto
     */
    @ApiOperation(value = "Retrieve All Program Trackings", notes = "<p>Retrieve All Program Trackings</p>")
    @RequestMapping(value = "/programs/{userId}/{programId}", method = RequestMethod.GET)
    @Timed
    public LearningPathDTO retrievePrograms(@PathVariable Long userId, @PathVariable Long programId) {
        return totaraCourseService.findByProgramIdAndUserId(programId, userId);
    }

    /**
     * Retrieve course data course dto.
     *
     * @param courseId the course id
     * @return the course dto
     */
    @ApiOperation(value = "Retrieve Course Data", notes = "<p>Retrieve Course Data.</p>")
    @RequestMapping(value = "/{courseId}", method = RequestMethod.GET)
    @Timed
    public CourseDTO retrieveCourseData(@PathVariable Long courseId) {
        return courseService.getCourse(SecurityUtils.getCurrentLoginAsLong(), courseId);
    }

    /**
     * Retrieve course overview course overview dto.
     *
     * @param courseId the course id
     * @return the course overview dto
     */
    @ApiOperation(value = "Retrieve Course Overview", notes = "<p>Retrieve Course Overview.</p>")
    @RequestMapping(value = "/{courseId}/overview", method = RequestMethod.GET)
    @Timed
    public CourseOverviewDTO retrieveCourseOverview(@PathVariable Long courseId) {
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();
        return courseService.getCourseOverview(personTotaraId, courseId);
    }

    /**
     * Retrieve course upcoming course upcoming dto.
     *
     * @param courseId the course id
     * @return the course upcoming dto
     */
    @ApiOperation(value = "Retrieve Course Upcoming", notes = "<p>Retrieve Course Upcoming.</p>")
    @RequestMapping(value = "/{courseId}/upcoming", method = RequestMethod.GET)
    @Timed
    public CourseUpcomingDTO retrieveCourseUpcoming(@PathVariable Long courseId) {
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();
        CourseUpcomingDTO upcomingDTO = new CourseUpcomingDTO();

        upcomingDTO.setUpcomingClassList(totaraCourseService.findUpcomingClasses(courseId, personTotaraId));

        return upcomingDTO;
    }

    /**
     * Retrieve course sections course sections dto.
     *
     * @param courseId the course id
     * @return the course sections dto
     */
    @ApiOperation(value = "Retrieve Course Sections", notes = "<p>Retrieve Course Sections.</p>")
    @RequestMapping(value = "/{courseId}/sections", method = RequestMethod.GET)
    @Timed
    public CourseSectionsDTO retrieveCourseSections(@PathVariable Long courseId) {
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();
        return courseService.getGeneralCourseSections(courseId, personTotaraId);
    }

    /**
     * Retrieve course prerequisites course sections dto.
     *
     * @param courseId the course id
     * @return the course sections dto
     */
    @ApiOperation(value = "Retrieve Course Prerequisites", notes = "<p>Retrieve Course Sections.</p>")
    @RequestMapping(value = "/{courseId}/prerequisites", method = RequestMethod.GET)
    @Timed
    public CourseSectionsDTO retrieveCoursePrerequisites(@PathVariable Long courseId) {
        return courseService.getPrerequisitesCourseSections(courseId);
    }

    /**
     * Retrieve course resources course sections dto.
     *
     * @param courseId the course id
     * @return the course sections dto
     */
    @ApiOperation(value = "Retrieve Course Resources", notes = "<p>Retrieve Course Sections.</p>")
    @RequestMapping(value = "/{courseId}/resources", method = RequestMethod.GET)
    @Timed
    public CourseSectionsDTO retrieveCourseResources(@PathVariable Long courseId) {
        return courseService.getResourcesCourseSections(courseId);
    }

    /**
     * Retrieve course progression overview course progression overview dto.
     *
     * @param courseId the course id
     * @param userId   the user id
     * @return the course progression overview dto
     */
    @ApiOperation(value = "Retrieve Course Progression Overview", notes = "<p>Retrieve Course Progression Overview.</p>")
    @RequestMapping(value = "/{courseId}/{userId}/progression", method = RequestMethod.GET)
    @Timed
    public CourseProgressionOverviewDTO retrieveCourseProgressionOverview(@PathVariable Long courseId,
            @PathVariable Long userId) {
        return courseService.getCourseProgressionOverview(courseId, userId);
    }

    /**
     * Retrieve program progression overview list.
     *
     * @param programId the program id
     * @param userId    the user id
     * @return the list
     */
    @ApiOperation(value = "Retrieve Program Progression Overview", notes = "<p>Retrieve Program Progression Overview.</p>")
    @RequestMapping(value = "/program/{programId}/{userId}/progression", method = RequestMethod.GET)
    @Timed
    public List<CourseSetProgressionOverviewDTO> retrieveProgramProgressionOverview(@PathVariable Long programId,
            @PathVariable Long userId) {
        return programItemService.getProgramProgressionOverview(programId, userId);
    }

    /**
     * Retrieve shared program progression overview list.
     *
     * @param programId the program id
     * @param userId    the user id
     * @return the list
     */
    @ApiOperation(value = "Retrieve Shared Program Progression Overview", notes = "<p>Retrieve Program Progression Overview.</p>")
    @RequestMapping(value = "/program/{programId}/{userId}/shared/progression", method = RequestMethod.GET)
    @Timed
    public List<CourseSetProgressionOverviewDTO> retrieveSharedProgramProgressionOverview(@PathVariable Long programId,
            @PathVariable Long userId) {
        return teamService.findSharedPersonalProgramsCompletionByUser(userId, programId, userId);
    }

}
