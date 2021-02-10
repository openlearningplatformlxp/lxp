package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.datalayer.dao.TotaraCourseDAO;
import com.redhat.uxl.datalayer.repository.PersonAccessRepository;
import com.redhat.uxl.dataobjects.domain.PersonAccess;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.PersonAccessService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * The type Person access service.
 */
@Service
@Transactional
@Slf4j
public class PersonAccessServiceImpl implements PersonAccessService {

  @Inject
  private PersonAccessRepository personAccessRepository;
  @Inject
  private TotaraCourseDAO totaraCourseDAO;

  @Override
  public void registerAccessToCourse(Long personId, Long courseId) {
    if (personId != null && courseId != null) {
      PersonAccess personAccess = new PersonAccess();
      personAccess.setPersonTotaraId(personId);
      personAccess.setType(ProgramType.COURSE);
      personAccess.setItemId(courseId);
      personAccess.setAccess(new LocalDateTime());
      personAccess.setCreatedBy(String.valueOf(personId));
      personAccess.setLastModifiedBy(String.valueOf(personId));
      personAccessRepository.save(personAccess);
      // Find if course is inside a program
      TotaraProgramDTO program = totaraCourseDAO.findProgramByCourseId(courseId);
      if (program != null) {
        registerAccessToProgram(personId, program.getProgramId());
      }
    }

  }

  @Override
  public void registerAccessToProgram(Long personId, Long programId) {
    if (personId != null && programId != null) {
      PersonAccess personAccess = new PersonAccess();
      personAccess.setPersonTotaraId(personId);
      personAccess.setType(ProgramType.LEARNING_PATH);
      personAccess.setItemId(programId);
      personAccess.setAccess(new LocalDateTime());
      personAccess.setCreatedBy(String.valueOf(personId));
      personAccess.setLastModifiedBy(String.valueOf(personId));
      personAccessRepository.save(personAccess);
    }
  }

}
