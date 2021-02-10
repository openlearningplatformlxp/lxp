package com.redhat.uxl.services.service;

/**
 * The interface Person access service.
 */
public interface PersonAccessService {
    /**
     * Register access to course.
     *
     * @param personId the person id
     * @param courseId the course id
     */
    void registerAccessToCourse(Long personId, Long courseId);

    /**
     * Register access to program.
     *
     * @param personId  the person id
     * @param programId the program id
     */
    void registerAccessToProgram(Long personId, Long programId);
}
