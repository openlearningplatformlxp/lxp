package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.utils.DateUtils;
import com.redhat.uxl.dataobjects.domain.dto.PersonalLearningPathCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.PersonalLearningPathDTO;
import com.redhat.uxl.dataobjects.domain.dto.PersonalLearningPathShareDTO;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseSetDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.exceptions.MaxPersonalLearningPlanReachedException;
import com.redhat.uxl.services.service.PersonalPlanService;
import com.redhat.uxl.services.service.PersonalPlanShareService;
import com.redhat.uxl.services.service.ProfileService;
import com.redhat.uxl.services.service.ProgramItemService;
import com.redhat.uxl.services.service.TeamService;
import com.redhat.uxl.services.service.dto.ProgramItemDTO;
import com.redhat.uxl.services.service.dto.ProgramItemWrapperDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.dto.LearningPathOverviewDTO;
import com.redhat.uxl.webapp.web.rest.dto.SuccessDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * The type Personal program resource.
 */
@RestController
@RequestMapping(value = "/api/course/program/personal", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class PersonalProgramResource {

    /**
     * The Program item service.
     */
    @Inject
    ProgramItemService programItemService;
    /**
     * The Personal plan service.
     */
    @Inject
    PersonalPlanService personalPlanService;
    /**
     * The Personal plan share service.
     */
    @Inject
    PersonalPlanShareService personalPlanShareService;
    @Inject
    private ProfileService profileService;
    @Inject
    private TeamService teamService;

    /**
     * Gets personal program.
     *
     * @param pathId the path id
     * @return the personal program
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Get in personal learning path", notes = "<p>Get current logged user personal learning paths.</p>")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Timed
    public LearningPathOverviewDTO getPersonalProgram(@PathVariable("id") Long pathId) throws URISyntaxException {
        log.debug("REST request to get personal learning paths:");

        LearningPathOverviewDTO overviewDTO = new LearningPathOverviewDTO();
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();
        PersonalLearningPathDTO p = personalPlanService.getPersonalPlanForUser(personTotaraId, pathId);

        ProgramItemDTO programItem = new ProgramItemDTO();
        programItem.setId(p.getId());
        programItem.setTitle(p.getTitle());
        programItem.setDescription(p.getDescription());
        programItem.setDueDate(p.getDueDate());
        programItem.setPersonal(true);
        programItem.setType(ProgramType.LEARNING_PATH);
        programItem.setEnrolled(true);
        programItem.setOwnerId(p.getUserId());
        programItem.setAccountId(personTotaraId);
        programItem.setArchived(p.isArchived());
        overviewDTO.setTeamMembers(teamService.findTeamMembersByManager(personTotaraId));
        TotaraUserDTO manager = profileService.getFirstManager(personTotaraId);
        if (manager != null) {
            programItem.setManager(manager.getDisplayName());
        }
        programItem.setShared(p.isShared());
        if (p.isShared()) {
            programItem.setSharedWithManager(p.isSharedWithManager());
            programItem.setSharedWithManagerOn(p.getSharedWithManagerOn());
            programItem.setSharedWithReports(p.isSharedWithReports());
            programItem.setShares(p.getShares());
        }
        overviewDTO.setProgram(programItem);
        overviewDTO.setCourseSets(p.getCourseSets());
        overviewDTO.setPercentComplete(p.getPercentComplete().intValue());
        if (p.getCourseSets() != null) {
            Integer total = 0;
            for (ProgramCourseSetDTO cs : p.getCourseSets()) {
                if (cs.getCourses() != null) {
                    total += cs.getCourses().size();
                }
            }
            overviewDTO.setTotalNumOfCourses(total);
        }
        return overviewDTO;
    }

    /**
     * Gets personal programs.
     *
     * @return the personal programs
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Get personal learning paths", notes = "<p>Get current logged user personal learning paths.</p>")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @Timed
    public ProgramItemWrapperDTO getPersonalPrograms() throws URISyntaxException {
        log.debug("REST request to get personal learning paths:");

        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();
        TotaraUserDTO manager = profileService.getFirstManager(personTotaraId);
        return programItemService.getPersonalPrograms(personTotaraId, manager);
    }

    /**
     * Create personal programs personal learning path dto.
     *
     * @param personalLearningPath the personal learning path
     * @return the personal learning path dto
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Create a user progress learning path", notes = "<p>Create a user personal learning path.</p>")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @Timed
    public PersonalLearningPathDTO createPersonalPrograms(@RequestBody PersonalLearningPathDTO personalLearningPath)
            throws URISyntaxException {
        log.debug("REST request to create a personal learning path");
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        return programItemService.createPersonalProgram(personTotaraId, personalLearningPath);
    }

    /**
     * Update personal programs personal learning path dto.
     *
     * @param personalLearningPath the personal learning path
     * @return the personal learning path dto
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Update a user progress learning path", notes = "<p>Update a user personal learning path.</p>")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @Timed
    public PersonalLearningPathDTO updatePersonalPrograms(@RequestBody PersonalLearningPathDTO personalLearningPath)
            throws URISyntaxException {
        log.debug("REST request to update a personal learning path");
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        return programItemService.updatePersonalProgram(personTotaraId, personalLearningPath);
    }

    /**
     * Clone personal programs response entity.
     *
     * @param pathId the path id
     * @return the response entity
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Clone another user learning path to current user", notes = "<p>Clone another user learning path to current user</p>")
    @RequestMapping(value = "/clone/{id}", method = RequestMethod.POST)
    @Timed
    public ResponseEntity clonePersonalPrograms(@PathVariable("id") Long pathId) throws URISyntaxException {
        log.debug("REST request to update a personal learning path");
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        TotaraUserDTO manager = profileService.getFirstManager(personTotaraId);
        ProgramItemWrapperDTO wrapperDTO = programItemService.getPersonalPrograms(personTotaraId, manager);
        if (wrapperDTO.getTotalCount() < wrapperDTO.getMaxNumber()) {
            Long id = personalPlanService.clonePersonalProgram(personTotaraId, pathId);
            return ResponseEntity.ok(personalPlanService.getPersonalPlanForUser(personTotaraId, id));
        } else {
            SuccessDTO success = new SuccessDTO();
            success.setException(new MaxPersonalLearningPlanReachedException(wrapperDTO.getMaxNumber()));
            success.setData(wrapperDTO.getMaxNumber());
            return ResponseEntity.status(HttpStatus.INSUFFICIENT_SPACE_ON_RESOURCE).body(success);
        }
    }

    /**
     * Archive personal program.
     *
     * @param pathId the path id
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Archive a specific user personal learning path", notes = "<p>Archive a specific user personal learning path.</p>")
    @RequestMapping(value = "/{id}/archive", method = RequestMethod.PUT)
    @Timed
    public void archivePersonalProgram(@PathVariable("id") Long pathId) throws URISyntaxException {
        log.debug("REST request to update a personal learning path");
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        personalPlanService.archivePersonalProgram(personTotaraId, pathId);
    }

    /**
     * Toggle manual completion.
     *
     * @param courseDTO the course dto
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Toggle manual completion for external and offline tasks", notes = "<p>Toggle manual completion for external and offline tasks</p>")
    @RequestMapping(value = "/manual/", method = RequestMethod.POST)
    @Timed
    public void toggleManualCompletion(@RequestBody PersonalLearningPathCourseDTO courseDTO) throws URISyntaxException {
        log.debug("REST request to create a personal learning path");
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();

        programItemService.updatePersonalPlanManualCompletion(courseDTO.getItemId(), courseDTO.getStatus(),
                personTotaraId);
    }

    /**
     * Share to manager.
     *
     * @param shareDTO the share dto
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Share personal learning plan against my manager", notes = "<p>Share personal learning plan against my manager</p>")
    @RequestMapping(value = "/share/manager/", method = RequestMethod.POST)
    @Timed
    public void shareToManager(@RequestBody PersonalLearningPathShareDTO shareDTO) throws URISyntaxException {
        log.debug("REST request to create a personal learning path");
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();
        TotaraUserDTO manager = profileService.getFirstManager(personTotaraId);
        personalPlanShareService.shareWithManager(personTotaraId, shareDTO.getItemId(), manager.getId(),
                shareDTO.getMessage());
    }

    /**
     * Share to direct reports.
     *
     * @param shareDTO the share dto
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Share personal learning plan against my manager", notes = "<p>Share personal learning plan against my manager</p>")
    @RequestMapping(value = "/share/direct-reports/", method = RequestMethod.POST)
    @Timed
    public void shareToDirectReports(@RequestBody PersonalLearningPathShareDTO shareDTO) throws URISyntaxException {
        log.debug("REST request to create a personal learning path");
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();
        TotaraUserDTO manager = profileService.getFirstManager(personTotaraId);
        Date dueDate = null;
        try {
            DateTime startDateTime = DateUtils.parseDate(shareDTO.getDueDate());
            if (startDateTime != null) {
                dueDate = startDateTime.toDate();
            }
        } catch (RuntimeException e) {
            log.error("Parsing invalid date", e);
        }
        personalPlanShareService.shareWithDirectReports(personTotaraId, shareDTO.getItemId(),
                shareDTO.getDirectReportIds(), shareDTO.getMessage(), dueDate);
    }
}
