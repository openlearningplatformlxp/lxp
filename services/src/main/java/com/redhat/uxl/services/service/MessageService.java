package com.redhat.uxl.services.service;

import com.redhat.uxl.services.service.dto.MessagingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Message service.
 */
public interface MessageService {

    /**
     * Retrieve learning path message messaging dto.
     *
     * @param id the id
     * @return the messaging dto
     */
    MessagingDTO retrieveLearningPathMessage(Long id);

    /**
     * Retrieve course message messaging dto.
     *
     * @param id the id
     * @return the messaging dto
     */
    MessagingDTO retrieveCourseMessage(Long id);

    /**
     * Sets default learning path message.
     *
     * @param message the message
     * @return the default learning path message
     */
    MessagingDTO setDefaultLearningPathMessage(String message);

    /**
     * Sets default course message.
     *
     * @param message the message
     * @return the default course message
     */
    MessagingDTO setDefaultCourseMessage(String message);

    /**
     * Sets learning path message.
     *
     * @param message the message
     * @param id      the id
     * @return the learning path message
     */
    MessagingDTO setLearningPathMessage(String message, Long id);

    /**
     * Sets course message.
     *
     * @param message the message
     * @param id      the id
     * @return the course message
     */
    MessagingDTO setCourseMessage(String message, Long id);

    /**
     * Find for paged search page.
     *
     * @param searchOperation the search operation
     * @param searchValue     the search value
     * @param pageable        the pageable
     * @return the page
     */
    Page<MessagingDTO> findForPagedSearch(String searchOperation, String searchValue, Pageable pageable);

    /**
     * Find messages by user list.
     *
     * @param id the id
     * @return the list
     */
    List<MessagingDTO> findMessagesByUser(Long id);

    /**
     * Find approval messages by user list.
     *
     * @param id the id
     * @return the list
     */
    List<MessagingDTO> findApprovalMessagesByUser(Long id);

    /**
     * Find notification messages by user list.
     *
     * @param id the id
     * @return the list
     */
    List<MessagingDTO> findNotificationMessagesByUser(Long id);

    /**
     * Dismiss message.
     *
     * @param id the id
     */
    void dismissMessage(Long id);

    /**
     * Dismiss all message.
     *
     * @param currentUserId the current user id
     */
    void dismissAllMessage(Long currentUserId);

    /**
     * Find course approval request list.
     *
     * @param sessionId the session id
     * @param personId  the person id
     * @return the list
     */
    List<MessagingDTO> findCourseApprovalRequest(Long sessionId, Long personId);

    /**
     * Request course approval.
     *
     * @param courseId  the course id
     * @param sessionId the session id
     * @throws Exception the exception
     */
    void requestCourseApproval(Long courseId, Long sessionId) throws Exception;

    /**
     * Gets unread count.
     *
     * @param personId the person id
     * @return the unread count
     */
    Long getUnreadCount(Long personId);

    /**
     * Send notification to all users.
     *
     * @param title   the title
     * @param message the message
     */
    void sendNotificationToAllUsers(String title, String message);

    /**
     * Send notification to learning path users.
     *
     * @param title     the title
     * @param message   the message
     * @param programId the program id
     */
    void sendNotificationToLearningPathUsers(String title, String message, Long programId);

    /**
     * Send due date notification to user.
     *
     * @param title          the title
     * @param message        the message
     * @param userId         the user id
     * @param learningPathId the learning path id
     */
    void sendDueDateNotificationToUser(String title, String message, Long userId, Long learningPathId);

    /**
     * Send shared path notification to user.
     *
     * @param title          the title
     * @param message        the message
     * @param userId         the user id
     * @param learningPathId the learning path id
     */
    void sendSharedPathNotificationToUser(String title, String message, Long userId, Long learningPathId);

    /**
     * Send notification to user.
     *
     * @param title   the title
     * @param message the message
     * @param userId  the user id
     */
    void sendNotificationToUser(String title, String message, Long userId);
}
