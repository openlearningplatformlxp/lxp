package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.dao.TotaraCourseDAO;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.PersonalPlanService;
import com.redhat.uxl.services.service.ProfileService;
import com.redhat.uxl.services.service.TeamService;
import com.redhat.uxl.services.service.dto.AchievementDTO;
import com.redhat.uxl.services.service.dto.PersonalProgramStatsDTO;
import com.redhat.uxl.services.service.dto.ProfileProgressInfoDTO;
import com.redhat.uxl.services.service.dto.TeamMemberDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.dto.TeamWrapperDTO;
import io.swagger.annotations.ApiOperation;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

/**
 * The type Team resource.
 */
@RestController
@RequestMapping(value = "/api/team", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TeamResource extends BaseResource {

    /**
     * The Team service.
     */
    @Inject
    TeamService teamService;
    @Inject
    private ProfileService profile;
    @Inject
    private PersonalPlanService personalPlanService;

    /**
     * The Totara course dao.
     */
    @Inject
    TotaraCourseDAO totaraCourseDAO;

    /**
     * Gets team data.
     *
     * @param programTypeString the program type string
     * @return the team data
     */
    @ApiOperation(value = "Get team data", notes = "<p>Get info for team page.</p>")
    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public TeamWrapperDTO getTeamData(@RequestParam(value = "type", required = false) String programTypeString) {
        log.info("REST request to get home data");

        Long currentUserId = Long.valueOf(SecurityUtils.getCurrentLogin());
        TeamWrapperDTO wrapperDTO = new TeamWrapperDTO();
        // Get Team Members

        StopWatch watch = new StopWatch();
        watch.start();

        List<TeamMemberDTO> teamMembers = teamService.findTeamMembersByManager(currentUserId);
        watch.stop();
        log.debug("Getting team members in millis: " + watch.getTotalTimeMillis());

        List<TeamMemberDTO> revisedMembers = new ArrayList<TeamMemberDTO>();
        if (teamMembers.isEmpty()) {
            return wrapperDTO;
        } else {

            watch.start();

            // Populate CECredits
            for (TeamMemberDTO member : teamMembers) {
                log.debug("Member: " + member.getFirstName() + " " + member.getLastName());
                member.setCecredits(totaraCourseDAO.getCECreditsForUser(member.getUserId()));
                revisedMembers.add(member);
            }
            watch.stop();
            log.debug("Got CECredits in millis: " + watch.getTotalTimeMillis());

        }

        wrapperDTO.getTeamMembers().addAll(revisedMembers);
        watch.start();
        ProgramType programType = null;
        if (StringUtils.isNotEmpty(programTypeString)) {
            programType = ProgramType.valueOf(programTypeString);
        }
        if (programType != null) {
            switch (programType) {
            case COURSE:
                wrapperDTO.getProgressionOverview().addAll(teamService.findCourseCompletionsByUser(teamMembers));
                break;
            case LEARNING_PATH:
                wrapperDTO.getProgressionOverview().addAll(teamService.findProgramsCompletionsByUser(teamMembers));
                break;
            }
        } else {
            wrapperDTO.getProgressionOverview().addAll(teamService.findCompletionsByUser(teamMembers));
        }
        watch.stop();
        log.debug("Got Team stats in millis: " + watch.getTotalTimeMillis());
        return wrapperDTO;
    }

    /**
     * Gets team personal stats data.
     *
     * @return the team personal stats data
     */
    @ApiOperation(value = "Get team personal stats data", notes = "<p>Get team personal stats data.</p>")
    @RequestMapping(method = RequestMethod.GET, value = "/personal")
    @Timed
    public PersonalProgramStatsDTO getTeamPersonalStatsData() {
        Long userId = Long.valueOf(SecurityUtils.getCurrentLogin());
        ;
        List<TeamMemberDTO> teamMembers = teamService.findTeamMembersByManager(userId);
        PersonalProgramStatsDTO stats = personalPlanService.getPersonalPlanStats(userId, teamMembers);
        stats.setProgressOverview(teamService.findSharedPersonalProgramsCompletionsByUser(userId, teamMembers));
        return stats;
    }

    /**
     * Gets team child data.
     *
     * @param currentUserId the current user id
     * @return the team child data
     */
    @ApiOperation(value = "Get team child data", notes = "<p>Get info for team child.</p>")
    @RequestMapping(value = "/child/{userId}", method = RequestMethod.GET)
    @Timed
    public TeamWrapperDTO getTeamChildData(@PathVariable("userId") Long currentUserId) {
        log.debug("REST request to get home data");

        TeamWrapperDTO wrapperDTO = new TeamWrapperDTO();
        // Get Team Members
        List<TeamMemberDTO> teamMembers = teamService.findTeamMembersByManager(currentUserId);
        if (teamMembers.isEmpty()) {
            return wrapperDTO;
        }
        wrapperDTO.getTeamMembers().addAll(teamMembers);
        wrapperDTO.getProgressionOverview().addAll(teamService.findCompletionsByUser(teamMembers));
        return wrapperDTO;
    }

    /**
     * Gets team child data.
     *
     * @param currentUserId the current user id
     * @param type          the type
     * @return the team child data
     */
    @ApiOperation(value = "Get team child data", notes = "<p>Get info for team child.</p>")
    @RequestMapping(value = "/child/{userId}/{type}", method = RequestMethod.GET)
    @Timed
    public TeamWrapperDTO getTeamChildData(@PathVariable("userId") Long currentUserId,
            @PathVariable(value = "type") ProgramType type) {
        log.debug("REST request to get home data");

        TeamWrapperDTO wrapperDTO = new TeamWrapperDTO();
        // Get Team Members
        List<TeamMemberDTO> teamMembers = teamService.findTeamMembersByManager(currentUserId);
        if (teamMembers.isEmpty()) {
            return wrapperDTO;
        }
        wrapperDTO.getTeamMembers().addAll(teamMembers);
        switch (type) {
        case LEARNING_PATH:
            wrapperDTO.getProgressionOverview().addAll(teamService.findCompletionsByUser(teamMembers));
            break;
        default:
            wrapperDTO.getProgressionOverview().addAll(teamService.findCourseCompletionsByUser(teamMembers));
        }
        return wrapperDTO;
    }

    /**
     * Gets team member achievement progress.
     *
     * @param userId the user id
     * @return the team member achievement progress
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Get user profile progress info", notes = "<p>Get team member achievement progress.</p>")
    @RequestMapping(value = "/{userId}/progress", method = RequestMethod.GET)
    @Timed
    public List<AchievementDTO> getTeamMemberAchievementProgress(@PathVariable("userId") Long userId)
            throws URISyntaxException {
        if (userId == null) {
            userId = Long.valueOf(SecurityUtils.getCurrentLogin());
        }
        log.debug("REST request to get selected member achievement progress info: " + userId);

        ProfileProgressInfoDTO dto = profile.getLoggedUserProfileProgressInfo(userId);

        return dto.getAchievements();
    }
}
