package com.redhat.uxl.services.service.demo.impl;

import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.services.service.TotaraProgramService;
import com.redhat.uxl.datalayer.dto.LearningPathProgressionDTO;
import com.redhat.uxl.datalayer.dto.TotaraCourseSetDTO;
import com.redhat.uxl.datalayer.dto.TotaraTextActivityDTO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * The type Demo totara program service.
 */
@Slf4j
@Service
@ConditionalOnExpression("${demo.mode.enabled:true}")
public class DemoTotaraProgramServiceImpl implements TotaraProgramService {

    /**
     * The Demo utility service.
     */
    @Inject DemoUtilityService demoUtilityService;

  @Override
  public com.redhat.uxl.datalayer.dto.TotaraProgramDTO getProgramById(Long programId) {
    List<TotaraProgramDTO> programs = demoUtilityService.getPrograms();
    for (TotaraProgramDTO program : programs) {
      if (programId.equals(program.getProgramId())) {
        com.redhat.uxl.datalayer.dto.TotaraProgramDTO
            bo = new com.redhat.uxl.datalayer.dto.TotaraProgramDTO();
        bo.setCategory(1L);// Todo: whats a category
        bo.setCertificationId(1L);// Todo: whats a certId
        bo.setCourseSets(Collections.emptyList());
        bo.setEnrolledCourses(new HashMap<>());
        bo.setFullName(program.getProgramShortName());
        bo.setIsSelfEnrollEnabled(false);
        bo.setIsUserEnrolled(false);
        bo.setId(programId);
        return bo;
      }
    }
    return null;
  }

  @Override
  public List<TotaraCourseSetDTO> getCourseSetsInProgram(Long personTotaraId, Long programId,
      Boolean withStatus) {
    return Collections.emptyList();
  }

  @Override
  public Boolean isUserEnrolledInProgram(Long personTotaraId, Long programId) {
    List<TotaraProgramDTO> programs = demoUtilityService.getPrograms();
    List<LearningPathProgressionDTO> dtos = new ArrayList<>();
    for (TotaraProgramDTO program : programs) {
      if (programId.equals(program.getProgramId())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public List<LearningPathProgressionDTO> findLearningPathProgression(Long programId, Long userId) {
    List<TotaraProgramDTO> programs = demoUtilityService.getPrograms();
    List<LearningPathProgressionDTO> dtos = new ArrayList<>();
    for (TotaraProgramDTO program : programs) {
      if (programId.equals(program.getProgramId())) {
        LearningPathProgressionDTO dto = new LearningPathProgressionDTO();
        dto.setCccompletion(0L);
        dto.setCourseId(dto.getCourseId());
        dto.setDuration(dto.getDuration());
        dto.setProgramId(programId);
        dto.setProgramName(dto.getProgramName());
        dto.setStatus(0L);
        dto.setTimecompleted(DateTime.now().getMillis());
        dto.setUserId(1L);
        dtos.add(dto);
      }
    }
    return dtos;
  }

  @Override
  public List<LearningPathProgressionDTO> findLearningPathProgressionNonEnrolled(Long programId) {
    return Collections.emptyList();
  }

  @Override
  public void dropUser(Long programId, Long userId) {

  }

  @Override
  public LearningPathProgressionDTO findLearningPathTotalDuration(Long programId) {
    List<TotaraProgramDTO> programs = demoUtilityService.getPrograms();
    for (TotaraProgramDTO program : programs) {
      if (programId.equals(program.getProgramId())) {
        LearningPathProgressionDTO dto = new LearningPathProgressionDTO();
        dto.setUserId(1L);
        dto.setTimecompleted(0L);
        dto.setStatus(0L);
        dto.setProgramName(program.getProgramName());
        dto.setDuration(new BigDecimal(program.getDuration()));
        dto.setProgramId(programId);
        dto.setCourseId(dto.getCourseId());
        return dto;
      }
    }
    return null;
  }

  @Override
  public Long isCourseANestedProgram(Long courseId) {
    return null;
  }

  @Override
  public TotaraTextActivityDTO isCourseATextEntry(Long userId, Long courseId) {
    return null;
  }
}
