package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityChoiceDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityQuizSubmitDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityStatusAvailabilityDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerCourseDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerSectionDTO;
import com.redhat.uxl.datalayer.dto.QuizScoreDTO;
import com.redhat.uxl.datalayer.dto.TotaraActivityCompletionDTO;
import com.redhat.uxl.datalayer.repository.VideoTimeRepository;
import com.redhat.uxl.dataobjects.domain.VideoTime;
import com.redhat.uxl.dataobjects.domain.types.ActivityType;
import com.redhat.uxl.services.service.CoursePlayerService;
import com.redhat.uxl.services.service.TotaraActivityService;
import com.redhat.uxl.services.service.TotaraCourseService;
import com.redhat.uxl.services.service.TotaraEnrollmentService;
import com.redhat.uxl.services.service.TotaraSectionService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Course player service.
 */
@Service
public class CoursePlayerServiceImpl implements CoursePlayerService {

    /**
     * The Totara enrollment service.
     */
    @Inject
  TotaraEnrollmentService totaraEnrollmentService;

    /**
     * The Totara course service.
     */
    @Inject
  TotaraCourseService totaraCourseService;

    /**
     * The Totara section service.
     */
    @Inject
  TotaraSectionService totaraSectionService;

    /**
     * The Totara activity service.
     */
    @Inject
  TotaraActivityService totaraActivityService;

    /**
     * The Video time repository.
     */
    @Inject
  VideoTimeRepository videoTimeRepository;

  @Override
  public CoursePlayerCourseDTO getCourseAndSectionsHaveAcitivitesForUser(Long courseId,
      Long personTotaraId, boolean includeActivities) {

    // check if user is enrolled in course
    if (!totaraEnrollmentService.isUserEnrolledInCourse(courseId, personTotaraId)) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "This user is not enrolled in this course.");
    }

    // get course
    CoursePlayerCourseDTO course = totaraCourseService.getEnrolledCourse(personTotaraId, courseId);

    // get sections for course
    List<CoursePlayerSectionDTO> sections =
        totaraSectionService.getGeneralSectionsHaveActivitiesForCourse(courseId);
    course.setSections(sections);

    // TODO: (WJK) Replace this with something more efficient
    if (includeActivities) {
      for (CoursePlayerSectionDTO section : sections) {
        List<CoursePlayerActivityDTO> activities =
            totaraActivityService.getActivitiesBySection(courseId, section.getId());

        section.setActivities(activities);
        section.sortActivities();
      }
    }

    return course;
  }

  @Override
  public CoursePlayerSectionDTO getActivitiesForSectionForCourse(Long courseId, Long sectionId,
      Long personTotaraId) {

    // check if user is enrolled in course
    if (!totaraEnrollmentService.isUserEnrolledInCourse(courseId, personTotaraId)) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "This user is not enrolled in this course.");
    }

    // get the section for the course
    CoursePlayerSectionDTO section = totaraSectionService.getSectionForCourse(courseId, sectionId);

    // get the activities for the section
    List<CoursePlayerActivityDTO> activities =
        totaraActivityService.getActivitiesBySection(courseId, sectionId);

    section.setActivities(activities);
    section.sortActivities();

    return section;

  }

  @Override
  public CoursePlayerActivityDTO getActivityContent(Long courseId, Long activityId, Long personId,
      Long personTotaraId) {

    // check if user is enrolled in course
    if (!totaraEnrollmentService.isUserEnrolledInCourse(courseId, personTotaraId)) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "This user is not enrolled in this course.");
    }

    return totaraActivityService.getActivityContent(activityId, personId, personTotaraId);
  }

  @Override
  public CoursePlayerActivityDTO markActivityComplete(Long courseId, Long activityId, Long personId,
      Long personTotaraId) {

    return markActivityState(courseId, activityId, personId, personTotaraId, 1);
  }

  @Override
  public CoursePlayerActivityDTO markActivityIncomplete(Long courseId, Long activityId,
      Long personId, Long personTotaraId) {

    return markActivityState(courseId, activityId, personId, personTotaraId, 0);
  }

  private CoursePlayerActivityDTO markActivityState(Long courseId, Long activityId, Long personId,
      Long personTotaraId, int targetState) {

    // check if user is enrolled in course
    if (!totaraEnrollmentService.isUserEnrolledInCourse(courseId, personTotaraId)) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "This user is not enrolled in this course.");
    }

    CoursePlayerActivityDTO bo =
        totaraActivityService.getActivityContent(activityId, personId, personTotaraId);

    if (bo.getIsLocked()) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "This activity is locked and cannot be marked complete.");
    }

    // TODO: Verify the completion criteria here allows for direct learner completion

    // if activity is URL or Resource or Certificate or Label Mark Complete.
    if (bo.getType().equals(ActivityType.URL) || bo.getType().equals(ActivityType.RESOURCE)
        || bo.getType().equals(ActivityType.CERTIFICATE) || bo.getType().equals(ActivityType.LABEL)
        || bo.getType().equals(ActivityType.PAGE) || bo.getType().equals(ActivityType.SCORM)) {
      totaraActivityService.completeActivity(bo.getId(), personTotaraId, targetState);
    }

    return bo;
  }

  @Override
  public List<CoursePlayerActivityStatusAvailabilityDTO> getStatusForActivitiesInCourse(
      Long courseId, Long personTotaraId) {
    return totaraActivityService.getStatusForActivitiesInCourse(courseId, personTotaraId);
  }

  @Override
  public List<CoursePlayerActivityStatusAvailabilityDTO> getStatusForActivitiesInSameCourse(
      Long activityId, Long personTotaraId) {
    return totaraActivityService.getStatusForActivitiesInSameCourse(activityId, personTotaraId);
  }

  @Override
  public TotaraActivityCompletionDTO submitFeedbackActivity(Long courseId, Long moduleId,
      Long personTotaraId, String data) {

    // check if user is enrolled in course
    if (!totaraEnrollmentService.isUserEnrolledInCourse(courseId, personTotaraId)) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "This user is not enrolled in this course.");
    }

    return totaraActivityService.completeActivityWithData(moduleId, personTotaraId, 1, data);
  }

  @Override
  public Map<String, Object> submitQuizQuestions(Long courseId, Long moudeId, Long personTotaraId,
      CoursePlayerActivityQuizSubmitDTO dto) {

    // check if user is enrolled in course
    if (!totaraEnrollmentService.isUserEnrolledInCourse(courseId, personTotaraId)) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "This user is not enrolled in this course.");
    }

    // send questions
    String responseString =
        totaraActivityService.submitTotaraActivityQuizQuestionAnswer(dto, personTotaraId);

    // grade quiz
    QuizScoreDTO quizScore =
        totaraActivityService.submitTotaraActivityQuizAttempt(dto.getAttemptId(), personTotaraId);

    // mark quiz as complete
    totaraActivityService.completeActivity(moudeId, personTotaraId, 1);

    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("questionResponse", responseString);
    responseMap.put("scoreResponse", quizScore);
    return responseMap;
  }

  @Override
  public void saveCourseCompleteVerification(Long courseId, Long personTotaraId) {
    if (courseId == null) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "Cannot find a course without an id.");
    }

    // write into user customize table
  }

  @Override
  public CoursePlayerActivityChoiceDTO getChoice(Long personTotaraId, Long moduleId) {
    return totaraActivityService.getChoiceContent(personTotaraId, moduleId);
  }

  @Override
  public CoursePlayerActivityChoiceDTO submitChoice(Long moduleId, Long personTotraId,
      CoursePlayerActivityChoiceDTO incomingDTO) {
    return totaraActivityService.submitChoice(personTotraId, moduleId, incomingDTO);
  }

  @Override
  @Transactional
  public void saveVideoTime(Long personId, Long personTotaraId, Long courseId, Long activityId,
      Double videoTime) {

    // check if user is enrolled in course
    if (!totaraEnrollmentService.isUserEnrolledInCourse(courseId, personTotaraId)) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "This user is not enrolled in this course.");
    }

    VideoTime videoTimeObj = videoTimeRepository.findByModuleIdAndPersonId(activityId, personId);
    if (videoTimeObj == null) {
      videoTimeObj = new VideoTime();
      videoTimeObj.setCourseId(courseId);
      videoTimeObj.setModuleId(activityId);
      videoTimeObj.setPersonId(personId);
      videoTimeObj.setTime(videoTime);
      videoTimeRepository.save(videoTimeObj);
    } else {
      videoTimeRepository.updateTime(activityId, personId, videoTime);
    }

  }
}
