package com.redhat.uxl.services.service.demo.impl;

import com.redhat.uxl.datalayer.dto.EnrolledTotaraCourseDTO;
import com.redhat.uxl.datalayer.dto.TotaraServiceResponseDTO;
import com.redhat.uxl.services.service.TotaraEnrollmentService;
import com.redhat.uxl.services.service.demo.dto.UserCourseDTO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Demo totara enrollment service.
 */
@Slf4j
@Service
@ConditionalOnExpression("${demo.mode.enabled:true}")
public class DemoTotaraEnrollmentServiceImpl implements TotaraEnrollmentService {

    /**
     * The Demo utility service.
     */
    @Inject DemoUtilityService demoUtilityService;

  @Override
  public Boolean isUserEnrolledInCourse(Long courseId, Long personTotaraId) {
    List<UserCourseDTO> courses = demoUtilityService.getUserCourses();
    for (UserCourseDTO course : courses) {
      if (course.getCourseId().equals(courseId)) {
        if (course.getUserId().equals(personTotaraId)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public Map<Long, EnrolledTotaraCourseDTO> getEnrolledCoursesById(Long personTotaraId,
      List<Long> courseIds) {
    List<UserCourseDTO> courses = demoUtilityService.getUserCourses();
    Map<Long, EnrolledTotaraCourseDTO> map = new HashMap<>();
    for (UserCourseDTO course : courses) {
      for (Long cId : courseIds) {
        if (course.getCourseId().equals(cId)) {
          if (course.getUserId().equals(personTotaraId)) {
            EnrolledTotaraCourseDTO bo = new EnrolledTotaraCourseDTO(new HashMap<>());
            bo.setCompletedActivitiesCount(1l);
            bo.setCompletionPercentage(50d);
            bo.setCourseFullName("Course Name");
            bo.setDescription("Course Description");
            bo.setDueDate(DateTime.now());
            bo.setCourseID(cId);
            map.put(course.getCourseId(), bo);
          }
        }
      }
    }
    return map;
  }

  @Override
  public TotaraServiceResponseDTO enrollUserToCourse(Long courseid, Long personTotaraId) {
    return new TotaraServiceResponseDTO();
  }

  @Override
  public TotaraServiceResponseDTO enrollUserToProgram(Long programId, Long personTotaraId) {
    return new TotaraServiceResponseDTO();
  }

  @Override
  public TotaraServiceResponseDTO enrollUserToProgramCourse(Long courseid, Long personTotaraId,
      Long programId) {
    return new TotaraServiceResponseDTO();
  }
}
