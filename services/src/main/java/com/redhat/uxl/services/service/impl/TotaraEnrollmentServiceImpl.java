package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.datalayer.dao.TotaraEnrollmentDAO;
import com.redhat.uxl.datalayer.dto.EnrolledTotaraCourseDTO;
import com.redhat.uxl.datalayer.dto.TotaraServiceResponseDTO;
import com.redhat.uxl.services.service.TotaraEnrollmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * The type Totara enrollment service.
 */
@Service
@Slf4j
@ConditionalOnExpression("!${demo.mode.enabled:false}")
public class TotaraEnrollmentServiceImpl implements TotaraEnrollmentService {

    /**
     * The Totara enrollment dao.
     */
    @Inject
  TotaraEnrollmentDAO totaraEnrollmentDAO;

  @Override
  public Boolean isUserEnrolledInCourse(Long courseId, Long personTotaraId) {
    return totaraEnrollmentDAO.isUserEnrolledInCourse(courseId, personTotaraId);
  }

  @Override
  public Map<Long, EnrolledTotaraCourseDTO> getEnrolledCoursesById(Long personTotaraId, List<Long> courseIds) {
    return totaraEnrollmentDAO.getEnrolledCoursesById(personTotaraId, courseIds);
  }

  @Override
  public TotaraServiceResponseDTO enrollUserToProgram(Long programId, Long personTotaraId) {
    return totaraEnrollmentDAO.enrollUserToProgram(programId, personTotaraId);
  }

  @Override
  public TotaraServiceResponseDTO enrollUserToCourse(Long courseid, Long personTotaraId) {
    return totaraEnrollmentDAO.enrollUserToCourse(courseid, personTotaraId);
  }

  @Override
  public TotaraServiceResponseDTO enrollUserToProgramCourse(Long courseid, Long personTotaraId,
      Long programId) {
    return totaraEnrollmentDAO.enrollUserToProgramCourse(courseid, personTotaraId, programId);
  }
}
