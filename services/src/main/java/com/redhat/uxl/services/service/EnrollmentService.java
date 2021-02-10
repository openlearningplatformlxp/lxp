package com.redhat.uxl.services.service;

/**
 * The interface Enrollment service.
 */
public interface EnrollmentService {

    /**
     * Accept session request.
     *
     * @param personId  the person id
     * @param messageId the message id
     */
    void acceptSessionRequest(Long personId, Long messageId);

    /**
     * Reject session request.
     *
     * @param personId  the person id
     * @param messageId the message id
     */
    void rejectSessionRequest(Long personId, Long messageId);

    /**
     * Enroll user in session.
     *
     * @param courseId       the course id
     * @param sessionId      the session id
     * @param personTotaraId the person totara id
     */
    void enrollUserInSession(Long courseId, Long sessionId, Long personTotaraId);

    /**
     * Enroll user to course.
     *
     * @param courseId       the course id
     * @param personTotaraId the person totara id
     */
    void enrollUserToCourse(Long courseId, Long personTotaraId);

    /**
     * Enroll user to program.
     *
     * @param programId      the program id
     * @param personTotaraId the person totara id
     */
    void enrollUserToProgram(Long programId, Long personTotaraId);

    /**
     * Enroll user to program course.
     *
     * @param courseId       the course id
     * @param programId      the program id
     * @param personTotaraId the person totara id
     */
    void enrollUserToProgramCourse(Long courseId, Long programId, Long personTotaraId);

    /**
     * Drop user from program.
     *
     * @param courseId the course id
     * @param personId the person id
     */
    void dropUserFromProgram(Long courseId, Long personId);
}
