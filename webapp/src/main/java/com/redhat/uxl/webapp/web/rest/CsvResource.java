package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.utils.DateUtils;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.CsvService;
import com.redhat.uxl.webapp.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The type Csv resource.
 */
@RestController
@RequestMapping(value = "/api/csv", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CsvResource {
    /**
     * The Csv service.
     */
    @Inject
    CsvService csvService;

    /**
     * Export all users report.
     *
     * @param response the response
     * @param request  the request
     */
    @ApiOperation(value = "Get csv for all users", notes = "Get csv for all users")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @Timed
    public void exportAllUsersReport(HttpServletResponse response, HttpServletRequest request) {
        try {
            csvService.getAllUsers(request, response);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Export team program progress report by type.
     *
     * @param type      the type
     * @param search    the search
     * @param startDate the start date
     * @param endDate   the end date
     * @param response  the response
     */
    @ApiOperation(value = "Get csv for team program progress with program Type", notes = "Get csv for team program progress with program Type")
    @RequestMapping(value = "/team-progress/export/type/{type}", method = RequestMethod.GET)
    @Timed
    public void exportTeamProgramProgressReportByType(@PathVariable ProgramType type,
            @RequestParam(value = "q", required = false) String search,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate, HttpServletResponse response) {

        Long currentUserId = Long.valueOf(SecurityUtils.getCurrentLogin());
        DateTime startDateTime = DateUtils.parseDate(startDate);
        DateTime endDateTime = DateUtils.parseDate(endDate);

        try {
            switch (type) {
            case LEARNING_PATH:
                csvService.getTeamProgressCSVByLearningPathPrograms(currentUserId, search, startDateTime, endDateTime,
                        response);
                break;
            case COURSE:
                csvService.getTeamProgressCSVByCoursePrograms(currentUserId, search, startDateTime, endDateTime,
                        response);
                break;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Export team shared program progress report.
     *
     * @param search    the search
     * @param startDate the start date
     * @param endDate   the end date
     * @param response  the response
     */
    @ApiOperation(value = "Get csv for team shared program progress", notes = "Get csv for team program shared progress")
    @RequestMapping(value = "/team-personal-plans/export", method = RequestMethod.GET)
    @Timed
    public void exportTeamSharedProgramProgressReport(@RequestParam(value = "q", required = false) String search,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate, HttpServletResponse response) {

        Long currentUserId = Long.valueOf(SecurityUtils.getCurrentLogin());
        DateTime startDateTime = DateUtils.parseDate(startDate);
        DateTime endDateTime = DateUtils.parseDate(endDate);
        try {
            csvService.getTeamProgressCSVBySharedPrograms(currentUserId, response, search, startDateTime, endDateTime);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Export team program progress report.
     *
     * @param userId   the user id
     * @param response the response
     * @param request  the request
     */
    @ApiOperation(value = "Get csv for team program progress of user", notes = "Get csv for team program progress of user")
    @RequestMapping(value = "/team-progress/export/{userId}", method = RequestMethod.GET)
    @Timed
    public void exportTeamProgramProgressReport(@PathVariable Long userId, HttpServletResponse response,
            HttpServletRequest request) {
        try {
            csvService.getTeamProgressCSVByProgram(userId, request, response);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Export team associate progress report.
     *
     * @param response  the response
     * @param request   the request
     * @param search    the search
     * @param startDate the start date
     * @param endDate   the end date
     */
    @ApiOperation(value = "Get csv for team associate progress", notes = "Get csv for team associate progress")
    @RequestMapping(value = "/team-associate-progress/export", method = RequestMethod.GET)
    @Timed
    public void exportTeamAssociateProgressReport(HttpServletResponse response, HttpServletRequest request,
            @RequestParam(value = "q", required = false) String search,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {

        Long currentUserId = Long.valueOf(SecurityUtils.getCurrentLogin());
        DateTime startDateTime = DateUtils.parseDate(startDate);
        DateTime endDateTime = DateUtils.parseDate(endDate);

        try {
            csvService.getTeamProgressCSVByTeamMember(currentUserId, request, response, search, startDateTime,
                    endDateTime);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Export team associate progress report.
     *
     * @param userId    the user id
     * @param search    the search
     * @param startDate the start date
     * @param endDate   the end date
     * @param response  the response
     * @param request   the request
     */
    @ApiOperation(value = "Get csv for team associate progress of user", notes = "Get csv for team associate progress of user")
    @RequestMapping(value = "/team-associate-progress/export/{userId}", method = RequestMethod.GET)
    @Timed
    public void exportTeamAssociateProgressReport(@PathVariable Long userId,
            @RequestParam(value = "q", required = false) String search,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate, HttpServletResponse response,
            HttpServletRequest request) {
        DateTime startDateTime = DateUtils.parseDate(startDate);
        DateTime endDateTime = DateUtils.parseDate(endDate);
        try {
            csvService.getTeamProgressCSVByTeamMember(userId, request, response, search, startDateTime, endDateTime);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Export all feedback report.
     *
     * @param response the response
     * @param request  the request
     */
    @ApiOperation(value = "Get csv for all feedback", notes = "Get csv for all feedback")
    @RequestMapping(value = "/feedback", method = RequestMethod.GET)
    @Timed
    public void exportAllFeedbackReport(HttpServletResponse response, HttpServletRequest request) {
        try {
            csvService.getAllFeedBack(request, response);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Export all audit searches report.
     *
     * @param response the response
     * @param request  the request
     */
    @ApiOperation(value = "Get csv for all audit searches", notes = "Get csv for all audit searches")
    @RequestMapping(value = "/audit-searches", method = RequestMethod.GET)
    @Timed
    public void exportAllAuditSearchesReport(HttpServletResponse response, HttpServletRequest request) {
        try {
            csvService.getAllAuditSearches(request, response);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
