package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.datalayer.dao.TotaraCourseDAO;
import com.redhat.uxl.datalayer.dao.TotaraProfileDAO;
import com.redhat.uxl.datalayer.dao.TotaraUrlDAO;
import com.redhat.uxl.datalayer.repository.LearningLockerJobExecutionDetailRepository;
import com.redhat.uxl.datalayer.repository.LearningLockerJobExecutionRepository;
import com.redhat.uxl.dataobjects.domain.LearningLockerJobExecution;
import com.redhat.uxl.dataobjects.domain.LearningLockerJobExecutionDetail;
import com.redhat.uxl.dataobjects.domain.dto.TotaraActivityDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUrlDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.dataobjects.domain.types.LearningLockerJobExecutionType;
import com.redhat.uxl.services.exceptions.TotaraActivityNotCompletedException;
import com.redhat.uxl.services.exceptions.TotaraActivityNotFoundException;
import com.redhat.uxl.services.exceptions.TotaraUrlNotFoundException;
import com.redhat.uxl.services.exceptions.TotaraUserNotFoundException;
import com.redhat.uxl.services.service.LearningLockerService;
import com.redhat.uxl.services.service.TotaraActivityService;
import com.redhat.uxl.services.service.dto.LearningLockerResponseDTO;
import com.redhat.uxl.datalayer.dto.TotaraActivityCompletionDTO;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;

import static org.joda.time.DateTimeZone.UTC;

/**
 * The type Learning locker service.
 */
@Log
@Service
public class LearningLockerServiceImpl implements LearningLockerService {

  @Inject
  private LearningLockerStatementImpl learningLockerStatementImpl;

  @Inject
  private LearningLockerJobExecutionRepository learningLockerJobExecutionRepository;

  @Inject
  private LearningLockerJobExecutionDetailRepository jobExecutionDetailRepository;

  @Inject
  private TotaraProfileDAO totaraProfileDAO;

  @Inject
  private TotaraUrlDAO totaraUrlDAO;

  @Inject
  private TotaraCourseDAO totaraCourseDAO;

  @Inject
  private TotaraActivityService activityService;

  @Override
  public DateTime findLastJobExecution(LearningLockerJobExecutionType type) {
    List<LearningLockerJobExecution> executionList = learningLockerJobExecutionRepository
        .findOneByTypeOrderByCreatedDateDesc(type,PageRequest.of(0, 1));
    // For testing purposes we go one day back for first start. Then we always use last execution
    // date.
    if (executionList != null && executionList.size() > 0) {
      DateTime executionDate = executionList.get(0).getExecutionDate();
      if (executionDate == null) {
        return DateTime.now().minusDays(1).toDateTime(DateTimeZone.getDefault()).toDateTime(UTC)
            .withTimeAtStartOfDay();
      } else {
        return executionDate;
      }
    }
    // today 00 hours
    return DateTime.now().minusDays(5).toDateTime(DateTimeZone.getDefault()).toDateTime(UTC)
        .withTimeAtStartOfDay();
  }

  @Override
  public LearningLockerResponseDTO pullStatementsDataAfter(LearningLockerJobExecutionType type,
      DateTime executionDate, String cursor) {
    LearningLockerResponseDTO response = null;
    switch (type) {
      case KALTURA:
        response = learningLockerStatementImpl.getPagedKulturaStatements(executionDate, cursor);
        break;
      case LYNDA:
        response = learningLockerStatementImpl.getPagedLyndaStatements(executionDate, cursor);
        break;
      case ALLEGO:
        response = learningLockerStatementImpl.getPagedAllegoStatements(executionDate, cursor);
        break;
    }
    return response;
  }

  @Override
  public void completeActivity(LearningLockerJobExecutionType type, String email,
      String externalUrl, LearningLockerJobExecutionDetail detail) {
    String urlPathId = null;
    TotaraUserDTO user = null;

    // Determine and store url and key information
    detail.setUrl(externalUrl);
    if (externalUrl != null) {
      String[] paths = null;
      switch (type) {
        case KALTURA:
        case LYNDA:
          paths = StringUtils.splitByWholeSeparator(externalUrl, "/");
          urlPathId = paths[paths.length - 1];
          detail.setUrlKey(urlPathId);
          break;
        case ALLEGO:
          paths = StringUtils.splitByWholeSeparator(externalUrl, "_");
          urlPathId = paths[paths.length - 1];
          detail.setUrlKey(urlPathId);
          break;
      }
    }

    // Determine and store user information
    detail.setAuthor(email);
    try {
      user = totaraProfileDAO.getUserProfileByEmail(email);
      detail.setPersonId(user.getId());
    } catch (RuntimeException e) {
      e.printStackTrace();
    }

    // Check for missing information
    // NOTE: Intentionally delaying exceptions to allow as much info in detail as possible
    if (user == null) {
      throw new TotaraUserNotFoundException(email);
    }
    if (urlPathId == null) {
      throw new TotaraUrlNotFoundException(externalUrl);
    }

    // Map url key to totara external urls
    List<TotaraUrlDTO> totaraUrlDTOS = totaraUrlDAO.findByExternalUrlPathId(type, urlPathId);
    if (totaraUrlDTOS == null || totaraUrlDTOS.size() == 0) {
      throw new TotaraUrlNotFoundException(externalUrl);
    }

    // Process activity completion
    TotaraUserDTO lambdaUser = user;
    totaraUrlDTOS.stream().forEach(totaraUrlDTO -> {
      TotaraActivityDTO activity = totaraCourseDAO.findActivityByInstanceId(totaraUrlDTO.getId());
      if (activity == null) {
        throw new TotaraActivityNotFoundException(totaraUrlDTO.getId());
      }
      log.log(Level.INFO,
          "Completing activity: " + activity.getId() + " with user id: " + lambdaUser.getId());
      TotaraActivityCompletionDTO dto =
          activityService.completeActivity(activity.getId(), lambdaUser.getId(), 1);
      if (dto == null) {
        throw new TotaraActivityNotCompletedException(totaraUrlDTO.getCourse());
      } else {
        activityService.completeURLSingleActCourse(activity.getCourseId(), lambdaUser.getId());
      }
    });
  }

  @Override
  public void postJobExecution(LearningLockerJobExecution execution,
      List<LearningLockerJobExecutionDetail> details) {
    if (execution.isCompleteFailure()) {
      execution.setExecutionDate(findLastJobExecution(execution.getType()));
    }
    execution.setCreatedBy("system");
    execution.setLastModifiedBy("system");
    learningLockerJobExecutionRepository.save(execution);
    details.forEach(detail -> {
      detail.setJobExecutionLearningLocker(execution);
    });
    jobExecutionDetailRepository.save(details);
  }

}
