package com.redhat.uxl.webapp.web.rest.courseplayer;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.services.service.CoursePlayerService;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.services.service.ScormValueService;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityChoiceDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityQuizSubmitDTO;
import com.redhat.uxl.datalayer.dto.TotaraActivityCompletionDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import com.redhat.uxl.webapp.web.rest.courseplayer.dto.*;
import io.swagger.annotations.ApiOperation;
import java.net.URISyntaxException;
import java.util.Map;
import javax.inject.Inject;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * The type Course player resource.
 */
@RestController
@RequestMapping(value = "/api/courseplayer", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CoursePlayerResource extends BaseResource {

    /**
     * The Scorm value service.
     */
    @Inject
    ScormValueService scormValueService;

    /**
     * The Person service.
     */
    @Inject
    PersonService personService;

    /**
     * The Course player service.
     */
    @Inject
    CoursePlayerService coursePlayerService;

    @Value("${totara.activity.baseurl}")
    private String activtyBaseURL;

    /**
     * Gets course player data.
     *
     * @param courseId          the course id
     * @param includeActivities the include activities
     * @return the course player data
     */
    @ApiOperation(value = "Get Course Content for Player", notes = "<p>Get Course Content for Player</p>")
    @RequestMapping(value = "/course/{courseId}", method = RequestMethod.GET)
    @Timed
    public CoursePlayerCourseOverviewDTO getCoursePlayerData(@PathVariable Long courseId,
            @RequestParam(value = "includeActivities", defaultValue = "true") boolean includeActivities) {
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        CoursePlayerCourseOverviewDTO dto = new CoursePlayerCourseOverviewDTO();

        dto.setCourseData(CoursePlayerCourseDTO.valueOf(coursePlayerService.getCourseAndSectionsHaveAcitivitesForUser(
                courseId, SecurityUtils.getCurrentLoginAsLong(), includeActivities)));
        dto.setActivityStatuses(CoursePlayerActivityStatusDTO
                .valueOfAvailable(coursePlayerService.getStatusForActivitiesInCourse(courseId, personTotaraId)));

        return dto;
    }

    /**
     * Gets section activities.
     *
     * @param courseId  the course id
     * @param sectionId the section id
     * @return the section activities
     */
    @ApiOperation(value = "Get Course Section Activities", notes = "<p>Get Course Section Activities</p>")
    @RequestMapping(value = "/sectionActivities/{courseId}/{sectionId}", method = RequestMethod.GET)
    @Timed
    public CoursePlayerSectionDTO getSectionActivities(@PathVariable Long courseId, @PathVariable Long sectionId) {
        return CoursePlayerSectionDTO.valueOf(coursePlayerService.getActivitiesForSectionForCourse(courseId, sectionId,
                SecurityUtils.getCurrentLoginAsLong()));
    }

    /**
     * Gets activity content.
     *
     * @param courseId   the course id
     * @param activityId the activity id
     * @return the activity content
     */
    @ApiOperation(value = "Get Activity Content", notes = "<p>Get Activity Content</p>")
    @RequestMapping(value = "/activity/{courseId}/{activityId}", method = RequestMethod.GET)
    @Timed
    public com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerActivityDTO getActivityContent(
            @PathVariable Long courseId, @PathVariable Long activityId) {
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        Person person = personService.getPersonByLogin(SecurityUtils.getCurrentLogin(), false);
        CoursePlayerActivityDTO activityContent = coursePlayerService.getActivityContent(courseId, activityId,
                person.getId(), personTotaraId);

        return generateCoursePlayerActivityDTO(courseId, activityId, personTotaraId, activityContent);
    }

    private com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerActivityDTO generateCoursePlayerActivityDTO(
            Long courseId, Long activityId, Long personTotaraId, CoursePlayerActivityDTO activityContent) {
        com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerActivityDTO dto = com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerActivityDTO
                .valueOfWithContent(activityContent);
        dto.setCourseId(courseId);

        Map<Long, CoursePlayerActivityStatusDTO> activityStatuses = CoursePlayerActivityStatusDTO
                .valueOfAvailable(coursePlayerService.getStatusForActivitiesInSameCourse(activityId, personTotaraId));

        // TODO: (WJK) Move this into the call for the activity-specific data itself?
        dto.setActivityStatus(activityStatuses.get(activityId));

        // get status of other activites in same section
        dto.setActivityStatuses(activityStatuses);
        dto.setExternalURL(activtyBaseURL + dto.getOriginalActivityType() + "/view.php?id=" + activityId);

        return dto;
    }

    /**
     * Mark activity complete com . redhat . uxl . webapp . web . rest . courseplayer . dto . course player activity dto.
     *
     * @param courseId   the course id
     * @param activityId the activity id
     * @return the com . redhat . uxl . webapp . web . rest . courseplayer . dto . course player activity dto
     */
    @ApiOperation(value = "Mark activity complete", notes = "<p>Mark activity complete</p>")
    @RequestMapping(value = "/activity/complete/{courseId}/{activityId}", method = RequestMethod.POST)
    @Timed
    public com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerActivityDTO markActivityComplete(
            @PathVariable Long courseId, @PathVariable Long activityId) {
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        Person person = personService.getPersonByLogin(SecurityUtils.getCurrentLogin(), false);
        CoursePlayerActivityDTO activityContent = coursePlayerService.markActivityComplete(courseId, activityId,
                person.getId(), personTotaraId);

        return generateCoursePlayerActivityDTO(courseId, activityId, personTotaraId, activityContent);
    }

    /**
     * Mark activity incomplete com . redhat . uxl . webapp . web . rest . courseplayer . dto . course player activity dto.
     *
     * @param courseId   the course id
     * @param activityId the activity id
     * @return the com . redhat . uxl . webapp . web . rest . courseplayer . dto . course player activity dto
     */
    @ApiOperation(value = "Mark activity incomplete", notes = "<p>Mark activity incomplete</p>")
    @RequestMapping(value = "/activity/incomplete/{courseId}/{activityId}", method = RequestMethod.POST)
    @Timed
    public com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerActivityDTO markActivityIncomplete(
            @PathVariable Long courseId, @PathVariable Long activityId) {
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        Person person = personService.getPersonByLogin(SecurityUtils.getCurrentLogin(), false);
        CoursePlayerActivityDTO activityContent = coursePlayerService.markActivityIncomplete(courseId, activityId,
                person.getId(), personTotaraId);

        return generateCoursePlayerActivityDTO(courseId, activityId, personTotaraId, activityContent);
    }

    /**
     * Submit feedback activity totara activity completion dto.
     *
     * @param dto      the dto
     * @param courseId the course id
     * @param moduleId the module id
     * @return the totara activity completion dto
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Submit Feedback Activity", notes = "<p>Submit Feedback Activity</p>")
    @RequestMapping(value = "/activity/feedback/submit/{courseId}/{moduleId}", method = RequestMethod.POST)
    @Timed
    public TotaraActivityCompletionDTO submitFeedbackActivity(@RequestBody CoursePlayerActivityFeedbackSubmitDTO dto,
            @PathVariable Long courseId, @PathVariable Long moduleId) throws URISyntaxException {
        return coursePlayerService.submitFeedbackActivity(courseId, moduleId, SecurityUtils.getCurrentLoginAsLong(),
                dto.getData());
    }

    /**
     * Submit quiz activity course player activity quiz submit result dto.
     *
     * @param dto      the dto
     * @param courseId the course id
     * @param moduleId the module id
     * @return the course player activity quiz submit result dto
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Submit Quiz Activity", notes = "<p>Submit Quiz Activity</p>")
    @RequestMapping(value = "/activity/quiz/submit/{courseId}/{moduleId}", method = RequestMethod.POST)
    @Timed
    public CoursePlayerActivityQuizSubmitResultDTO submitQuizActivity(
            @RequestBody CoursePlayerActivityQuizSubmitDTO dto, @PathVariable Long courseId,
            @PathVariable Long moduleId) throws URISyntaxException {
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        CoursePlayerActivityQuizSubmitResultDTO resultDTO = new CoursePlayerActivityQuizSubmitResultDTO();
        resultDTO.setResults(coursePlayerService.submitQuizQuestions(courseId, moduleId,
                SecurityUtils.getCurrentLoginAsLong(), dto));

        Map<Long, CoursePlayerActivityStatusDTO> activityStatuses = CoursePlayerActivityStatusDTO
                .valueOfAvailable(coursePlayerService.getStatusForActivitiesInSameCourse(moduleId, personTotaraId));
        resultDTO.setActivityStatus(activityStatuses.get(moduleId));
        resultDTO.setActivityStatuses(activityStatuses);

        return resultDTO;
    }

    /**
     * Gets activity statuses.
     *
     * @param moduleId the module id
     * @return the activity statuses
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Get Activity Status", notes = "<p>Get Activity Status</p>")
    @RequestMapping(value = "/activityStatuses/{moduleId}", method = RequestMethod.GET)
    @Timed
    public Map<Long, CoursePlayerActivityStatusDTO> getActivityStatuses(@PathVariable Long moduleId)
            throws URISyntaxException {
        return CoursePlayerActivityStatusDTO.valueOfAvailable(coursePlayerService
                .getStatusForActivitiesInSameCourse(moduleId, SecurityUtils.getCurrentLoginAsLong()));
    }

    /**
     * Verify course completion.
     *
     * @param courseId the course id
     */
    @ApiOperation(value = "Verify User Course Completion", notes = "<p>Verify User Course Completion</p>")
    @RequestMapping(value = "/course/completion/verify/{courseId}", method = RequestMethod.POST)
    @Timed
    public void verifyCourseCompletion(@PathVariable Long courseId) {
        coursePlayerService.saveCourseCompleteVerification(courseId, SecurityUtils.getCurrentLoginAsLong());
    }

    /**
     * Gets choice.
     *
     * @param moduleId the module id
     * @return the choice
     */
    @ApiOperation(value = "Get Choice Activity from totara.", notes = "<p>Get Choice Activity from totara.</p>")
    @RequestMapping(value = "/choice/get/{moduleId}", method = RequestMethod.GET)
    @Timed
    @Transactional
    public CoursePlayerActivityChoiceDTO getChoice(@PathVariable Long moduleId) {

        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        return coursePlayerService.getChoice(personTotaraId, moduleId);
    }

    /**
     * Submit choice course player activity choice dto.
     *
     * @param moduleId    the module id
     * @param incomingDTO the incoming dto
     * @return the course player activity choice dto
     */
    @ApiOperation(value = "Submit Choice Activity to totara.", notes = "<p>Submit Choice Activity to totara.</p>")
    @RequestMapping(value = "/choice/submit/{moduleId}", method = RequestMethod.POST)
    @Timed
    @Transactional
    public CoursePlayerActivityChoiceDTO submitChoice(@PathVariable Long moduleId,
            @RequestBody CoursePlayerActivityChoiceDTO incomingDTO) {

        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        return coursePlayerService.submitChoice(moduleId, personTotaraId, incomingDTO);
    }

    /**
     * Save video time boolean.
     *
     * @param videoTimeDTO the video time dto
     * @return the boolean
     */
    @ApiOperation(value = "Save Users Video Time", notes = "<p>Save Users Video Time</p>")
    @RequestMapping(value = "/videotime", method = RequestMethod.POST)
    @Timed
    public boolean saveVideoTime(@RequestBody CoursePlayerVideoTimeDTO videoTimeDTO) {
        Person person = personService.getPersonByLogin(SecurityUtils.getCurrentLogin(), false);
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        coursePlayerService.saveVideoTime(person.getId(), personTotaraId, videoTimeDTO.getCourseId(),
                videoTimeDTO.getActivityId(), videoTimeDTO.getVideoTime());
        return true;
    }
}
