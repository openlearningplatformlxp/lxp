package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.datalayer.dao.TotaraActivityDAO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityChoiceDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityQuizSubmitDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityStatusAvailabilityDTO;
import com.redhat.uxl.datalayer.dto.QuizScoreDTO;
import com.redhat.uxl.datalayer.dto.TotaraActivityCompletionDTO;
import com.redhat.uxl.services.service.TotaraActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * The type Totara activity service.
 */
@Service
@Slf4j
public class TotaraActivityServiceImpl implements TotaraActivityService {


  @Inject
  private TotaraActivityDAO totaraActivityDAO;


    @SuppressWarnings("deprecation")
  @Override
  public Long getActivityCount(Long courseId) {
      return totaraActivityDAO.getActivityCount(courseId);

  }

  @SuppressWarnings("deprecation")
  @Override
  public Long getCompletedActivityCount(Long courseId, Long userId) {
      return totaraActivityDAO.getCompletedActivityCount(courseId, userId);

  }

  @SuppressWarnings("deprecation")
  @Override
  public Long getCourseStatus(Long courseId, Long userId) throws Exception {
      return totaraActivityDAO.getCourseStatus(courseId, userId);
  }

  @Override
  public void completeURLSingleActCourse(Long courseId, Long userId) {
      totaraActivityDAO.completeURLSingleActCourse(courseId, userId);
  }

  @Override
  public List<CoursePlayerActivityDTO> getActivitiesBySection(Long courseId, Long sectionId) {
      return totaraActivityDAO.getActivitiesBySection(courseId, sectionId);

  }

  @Override
  public CoursePlayerActivityDTO getActivityContent(Long activityId, Long personId, Long personTotaraId) {
      return totaraActivityDAO.getActivityContent(activityId, personId, personTotaraId);
  }

  @Override
  public List<CoursePlayerActivityStatusAvailabilityDTO> getStatusForActivitiesInCourse(Long courseId, Long personTotaraId) {
    return totaraActivityDAO.getStatusForActivitiesInCourse(courseId, personTotaraId);
  }

  @Override
  public List<CoursePlayerActivityStatusAvailabilityDTO> getStatusForActivitiesInSameCourse(Long activityId, Long personTotaraId) {
      return totaraActivityDAO.getStatusForActivitiesInSameCourse(activityId, personTotaraId);
  }

  @Override
  public TotaraActivityCompletionDTO completeActivity(Long moduleId, Long personTotaraId, int targetState) {
      return totaraActivityDAO.completeActivity(moduleId, personTotaraId, targetState);
  }

  @Override
  public TotaraActivityCompletionDTO completeActivityWithData(Long moduleId, Long personTotaraId, int targetState, String data) {
      return totaraActivityDAO.completeActivityWithData(moduleId, personTotaraId, targetState, data);
  }

  @Override
  public String submitTotaraActivityQuizQuestionAnswer(CoursePlayerActivityQuizSubmitDTO dto, Long personTotaraId) {
      return totaraActivityDAO.submitTotaraActivityQuizQuestionAnswer(dto, personTotaraId);
  }

  @Override
  public QuizScoreDTO submitTotaraActivityQuizAttempt(Long attemptid, Long personTotaraId) {
      return totaraActivityDAO.submitTotaraActivityQuizAttempt(attemptid,personTotaraId);
  }

  @Override
  public CoursePlayerActivityChoiceDTO getChoiceContent(Long personTotaraId, Long activityId) {
      return totaraActivityDAO.getChoiceContent(personTotaraId, activityId);
  }

  @Override
  public CoursePlayerActivityChoiceDTO submitChoice(Long totaraId, Long moduleId, CoursePlayerActivityChoiceDTO postData) {
      return totaraActivityDAO.submitChoice(totaraId, moduleId, postData);
  }

}
