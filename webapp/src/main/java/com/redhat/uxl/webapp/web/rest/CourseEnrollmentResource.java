package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.EnrollmentService;
import com.redhat.uxl.services.service.MessageService;
import com.redhat.uxl.webapp.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Course enrollment resource.
 */
@RestController
@RequestMapping(value = "/api/course-enrollment", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CourseEnrollmentResource {

    /**
     * The Message service.
     */
    @Inject
    MessageService messageService;

    /**
     * The Enrollment service.
     */
    @Inject
    EnrollmentService enrollmentService;

    /**
     * Request approval.
     *
     * @param sessionId the session id
     */
    @ApiOperation(value = "Request Approval for a Course", notes = "<p>Request Approval for a Course</p>")
    @RequestMapping(value = "/approval/{sessionId}", method = RequestMethod.POST)
    @Timed
    public void requestApproval(@PathVariable Long sessionId) {
        log.debug("REST request to request approval");
        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        try {
            messageService.requestCourseApproval(sessionId, currentUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Accept approval.
     *
     * @param messageId the message id
     */
    @ApiOperation(value = "Accept Approval", notes = "<p>Acceept Approval/p>")
    @RequestMapping(value = "/approve/{messageId}", method = RequestMethod.POST)
    @Timed
    public void acceptApproval(@PathVariable Long messageId) {
        log.debug("REST request to accept approval");
        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        enrollmentService.acceptSessionRequest(currentUserId, messageId);
    }

    /**
     * Reject approval.
     *
     * @param messageId the message id
     */
    @ApiOperation(value = "Reject Approval", notes = "<p>Reject Approval/p>")
    @RequestMapping(value = "/reject/{messageId}", method = RequestMethod.POST)
    @Timed
    public void rejectApproval(@PathVariable Long messageId) {
        log.debug("REST request to reject approval");
        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        enrollmentService.rejectSessionRequest(currentUserId, messageId);
    }

    /**
     * Drop from program.
     *
     * @param programId the program id
     */
    @ApiOperation(value = "Drop a user from a program", notes = "<p>Drop a user from a program</p>")
    @RequestMapping(value = "/program/{programId}/drop", method = RequestMethod.POST)
    @Timed
    public void dropFromProgram(@PathVariable Long programId) {
        log.debug("REST request to drop a user from a program");
        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();

        enrollmentService.dropUserFromProgram(programId, currentUserId);

    }

    /**
     * Enroll in course session.
     *
     * @param courseId  the course id
     * @param sessionId the session id
     */
    @ApiOperation(value = "Enroll a user in a session", notes = "<p>Enroll a user in a session</p>")
    @RequestMapping(value = "/course/{courseId}/session/{sessionId}/enroll", method = RequestMethod.POST)
    @Timed
    public void enrollInCourseSession(@PathVariable Long courseId, @PathVariable Long sessionId) {
        log.debug("REST request to enroll user in a session");
        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        enrollmentService.enrollUserInSession(courseId, sessionId, currentUserId);
    }

    /**
     * Enroll in course.
     *
     * @param courseId the course id
     */
    @ApiOperation(value = "Enroll a user in a course", notes = "<p>Enroll a user in a course</p>")
    @RequestMapping(value = "/course/{courseId}/enroll", method = RequestMethod.POST)
    @Timed
    public void enrollInCourse(@PathVariable Long courseId) {
        log.debug("REST request to enroll user in a course");

        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        enrollmentService.enrollUserToCourse(courseId, currentUserId);
    }

    /**
     * Enroll in program.
     *
     * @param programId the program id
     */
    @ApiOperation(value = "Enroll a user in a program", notes = "<p>Enroll a user in a program</p>")
    @RequestMapping(value = "/program/{programId}/enroll", method = RequestMethod.POST)
    @Timed
    public void enrollInProgram(@PathVariable Long programId) {
        log.debug("REST request to enroll user in a program");

        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        enrollmentService.enrollUserToProgram(programId, currentUserId);
    }

    /**
     * Enroll in program course.
     *
     * @param programId the program id
     * @param courseId  the course id
     */
    @ApiOperation(value = "Enroll a user in a program course", notes = "<p>Enroll a user in a program course</p>")
    @RequestMapping(value = "/program/{programId}/course/{courseId}/enroll", method = RequestMethod.POST)
    @Timed
    public void enrollInProgramCourse(@PathVariable Long programId, @PathVariable Long courseId) {
        log.debug("REST request to enroll user in a program course");

        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        enrollmentService.enrollUserToProgramCourse(courseId, programId, currentUserId);
    }
}
