package com.redhat.uxl.services.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.datalayer.dao.TotaraCourseDAO;
import com.redhat.uxl.datalayer.repository.MessagingRepository;
import com.redhat.uxl.dataobjects.domain.Messaging;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.util.messaging.CourseApprovalMessageButton;
import com.redhat.uxl.services.constants.MessageConstants;
import com.redhat.uxl.services.service.EnrollmentService;
import com.redhat.uxl.services.service.MessageService;
import com.redhat.uxl.services.service.TotaraEnrollmentService;
import com.redhat.uxl.services.service.TotaraProgramService;
import com.redhat.uxl.datalayer.dto.TotaraServiceResponseDTO;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The type Enrollment service.
 */
@Service
@Slf4j
public class EnrollmentServiceImpl implements EnrollmentService {
    /**
     * The Messaging repository.
     */
    @Inject
  MessagingRepository messagingRepository;

    /**
     * The Totara course dao.
     */
    @Inject
  TotaraCourseDAO totaraCourseDAO;

    /**
     * The Message service.
     */
    @Inject
  MessageService messageService;

    /**
     * The Totara enrollment service.
     */
    @Inject
  TotaraEnrollmentService totaraEnrollmentService;

    /**
     * The Totara program service.
     */
    @Inject
  TotaraProgramService totaraProgramService;

  @Override
  public void dropUserFromProgram(Long programId, Long personTotaraId) {
    totaraProgramService.dropUser(programId, personTotaraId);

  }

  @Override
  public void acceptSessionRequest(Long personId, Long messageId) {
    Messaging message = messagingRepository.findOne(messageId);
    Gson gson = new Gson();
    Type listType = new TypeToken<ArrayList<CourseApprovalMessageButton>>() {}.getType();

    List<CourseApprovalMessageButton> buttons = gson.fromJson(message.getActionButtons(), listType);
    CourseApprovalMessageButton button = buttons.get(0);
    int signupId = totaraCourseDAO.addFaceToFaceSignup(button.getSessionId(), button.getPersonId());
    int statusId = totaraCourseDAO.addFaceToFaceSignupStatus(Integer.toUnsignedLong(signupId));
    try {
      enrollUserToCourseInternal(button.getCourseId(), button.getPersonId());

      TotaraCourseDTO dto = totaraCourseDAO.findCourseByCourseId(button.getCourseId());
      // Need to add a message to the approved user
      String messageText = MessageConstants.S_MESSAGE_APPROVE_MESSAGE;
      messageText = messageText.replaceAll("\\{course\\}", dto.getShortName());
      messageService.sendNotificationToUser(MessageConstants.S_MESSAGE_APPROVE, messageText,
          button.getPersonId());
    } catch (Exception e) {
      log.error("There was an error enrolling user in the course", e);

    }
    message.setActive(false);
    messagingRepository.save(message);
  }

  @Override
  public void rejectSessionRequest(Long personId, Long messageId) {
    Messaging message = messagingRepository.findOne(messageId);
    message.setActive(false);
    messagingRepository.save(message);

    Gson gson = new Gson();
    Type listType = new TypeToken<ArrayList<CourseApprovalMessageButton>>() {}.getType();

    List<CourseApprovalMessageButton> buttons = gson.fromJson(message.getActionButtons(), listType);
    CourseApprovalMessageButton button = buttons.get(0);

    TotaraCourseDTO dto = totaraCourseDAO.findCourseByCourseId(button.getCourseId());
    // Need to add a message to the rejected user
    String messageText = MessageConstants.S_MESSAGE_REJECT_MESSAGE;
    messageText = messageText.replaceAll("\\{course\\}", dto.getShortName());
    messageService.sendNotificationToUser(MessageConstants.S_MESSAGE_REJECT, messageText,
        button.getPersonId());

  }

  @Override
  public void enrollUserInSession(Long courseId, Long sessionId, Long personTotaraId) {
    int signupId = totaraCourseDAO.addFaceToFaceSignup(sessionId, personTotaraId);
    int statusId = totaraCourseDAO.addFaceToFaceSignupStatus(Integer.toUnsignedLong(signupId));
    try {
      enrollUserToCourseInternal(courseId, personTotaraId);
    } catch (Exception e) {
      log.error("There was an error enrolling user in the course", e);
    }
  }

  @Override
  public void enrollUserToCourse(Long courseId, Long personTotaraId) {
    enrollUserToCourseInternal(courseId, personTotaraId);
  }

  private void enrollUserToCourseInternal(Long courseId, Long personTotaraId) {
    boolean isEnrolled = totaraEnrollmentService.isUserEnrolledInCourse(courseId, personTotaraId);

    // TODO: (WJK) Should this complain if already enrolled?

    // enroll user if they are not already enrolled
    if (!isEnrolled) {
      TotaraServiceResponseDTO response =
          totaraEnrollmentService.enrollUserToCourse(courseId, personTotaraId);

      if (response == null || !response.getSuccess()) {
        throw new GeneralException(ErrorCodeGeneral.INTERNAL_SERVER_ERROR, "Error enrolling user");
      }
    }
  }

  @Override
  public void enrollUserToProgram(Long programId, Long personTotaraId) {
    boolean isEnrolled = totaraProgramService.isUserEnrolledInProgram(personTotaraId, programId);

    // TODO: (WJK) Should this complain if already enrolled?

    // enroll user if they are not already enrolled
    if (!isEnrolled) {
      TotaraServiceResponseDTO response =
          totaraEnrollmentService.enrollUserToProgram(programId, personTotaraId);

      if (response == null || response.getSuccess() == null || !response.getSuccess()) {
        log.error(response.getMessage());
        log.error("Program: " + programId);
        log.error("Person: " + personTotaraId);

        throw new GeneralException(ErrorCodeGeneral.INTERNAL_SERVER_ERROR, "Error enrolling user");
      }
    }
  }

  @Override
  public void enrollUserToProgramCourse(Long courseId, Long programId, Long personTotaraId) {
    boolean isEnrolled = totaraEnrollmentService.isUserEnrolledInCourse(courseId, personTotaraId);

    // TODO: (WJK) Should this complain if already enrolled?

    // enroll user if they are not already enrolled
    if (!isEnrolled) {
      TotaraServiceResponseDTO response =
          totaraEnrollmentService.enrollUserToProgramCourse(courseId, personTotaraId, programId);
      if (response != null) {
        log.warn("Response message" + response.getMessage());
      }
      if (response == null || !Boolean.TRUE.equals(response.getSuccess())) {

        throw new GeneralException(ErrorCodeGeneral.INTERNAL_SERVER_ERROR, "Error enrolling user");
      }
    }
  }
}
