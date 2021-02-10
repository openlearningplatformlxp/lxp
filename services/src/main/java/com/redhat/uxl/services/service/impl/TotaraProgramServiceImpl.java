package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.datalayer.dao.TotaraProgramDAO;
import com.redhat.uxl.datalayer.dto.LearningPathProgressionDTO;
import com.redhat.uxl.datalayer.dto.TotaraCourseSetDTO;
import com.redhat.uxl.datalayer.dto.TotaraProgramDTO;
import com.redhat.uxl.datalayer.dto.TotaraTextActivityDTO;
import com.redhat.uxl.services.service.TotaraProgramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * The type Totara program service.
 */
@Service
@Slf4j
@ConditionalOnExpression("!${demo.mode.enabled:false}")
public class TotaraProgramServiceImpl implements TotaraProgramService {

    /**
     * The Totara program dao.
     */
    @Inject
  TotaraProgramDAO totaraProgramDAO;

  @Override
  public TotaraProgramDTO getProgramById(Long programId) {
      return totaraProgramDAO.getProgramById(programId);
  }

  @Override
  public List<TotaraCourseSetDTO> getCourseSetsInProgram(Long personTotaraId, Long programId, Boolean withStatus) {
      return totaraProgramDAO.getCourseSetsInProgram(personTotaraId, programId, withStatus);
  }

  @Override
  public Boolean isUserEnrolledInProgram(Long personTotaraId, Long programId) {
      return totaraProgramDAO.isUserEnrolledInProgram(personTotaraId, programId);
  }

  @Override
  public List<LearningPathProgressionDTO> findLearningPathProgression(Long programId, Long userId) {
      return totaraProgramDAO.findLearningPathProgression(programId, userId);
  }

  @Override
  public LearningPathProgressionDTO findLearningPathTotalDuration(Long programId) {
      return totaraProgramDAO.findLearningPathTotalDuration(programId);
  }

  @Override
  public Long isCourseANestedProgram(Long courseId) {
      return totaraProgramDAO.isCourseANestedProgram(courseId);
  }

  @Override
  public TotaraTextActivityDTO isCourseATextEntry(Long userId, Long courseId) {
      return totaraProgramDAO.isCourseATextEntry(userId, courseId);
  }

  @Override
  public List<LearningPathProgressionDTO> findLearningPathProgressionNonEnrolled(Long programId) {
      return totaraProgramDAO.findLearningPathProgressionNonEnrolled(programId);
  }

  @Override
  public void dropUser(Long programId, Long userId) {
      totaraProgramDAO.dropUser(programId, userId);
  }

}
