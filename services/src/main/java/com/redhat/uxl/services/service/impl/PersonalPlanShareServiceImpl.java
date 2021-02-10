package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.commonjava.utils.DateUtils;
import com.redhat.uxl.datalayer.dao.PersonalPlansDAO;
import com.redhat.uxl.datalayer.repository.PersonRepository;
import com.redhat.uxl.datalayer.repository.PersonalPlanShareRepository;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.PersonalPlanShare;
import com.redhat.uxl.dataobjects.domain.dto.PersonalLearningPathDTO;
import com.redhat.uxl.dataobjects.domain.dto.PersonalProgramDTO;
import com.redhat.uxl.dataobjects.domain.types.PersonalPlanShareType;
import com.redhat.uxl.services.constants.MessageConstants;
import com.redhat.uxl.services.service.MessageService;
import com.redhat.uxl.services.service.PersonalPlanShareService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Personal plan share service.
 */
@Service
@Slf4j
@Transactional
public class PersonalPlanShareServiceImpl implements PersonalPlanShareService {

  /**
   * The Personal plan share repository.
   */
  @Inject
  PersonalPlanShareRepository personalPlanShareRepository;
  /**
   * The Personal plans dao.
   */
  @Inject
  PersonalPlansDAO personalPlansDAO;
  /**
   * The Person repository.
   */
  @Inject
  PersonRepository personRepository;
  /**
   * The Message service.
   */
  @Inject
  MessageService messageService;

  @Override
  public List<PersonalProgramDTO> findSharedWithMePlans(Long userId) {
    return personalPlansDAO.getPlansSharedWithUser(userId);
  }

  @Override
  public void shareWithManager(Long userId, Long personalPlanId, Long managerId,
      String sharedMessage) {
    Person owner =
        personRepository.findOneByLoginIgnoreCaseAndDeletedIsFalse(String.valueOf(userId));
    PersonalPlanShare personalPlanShare = PersonalPlanShare.builder()
        .pk(PersonalPlanShare.Pk.builder().ownerUserId(userId).sharedUserId(Long.valueOf(managerId))
            .personalPlanId(personalPlanId).build())
        .type(PersonalPlanShareType.MANAGER).message(sharedMessage).build();
    personalPlanShare.setCreatedBy(owner.getDisplayName());
    personalPlanShareRepository.save(personalPlanShare);
    PersonalLearningPathDTO path = personalPlansDAO.getPersonalPlanForUser(userId, personalPlanId);
    // Send a message to the manager about this shared plan
    String title =
        String.format(MessageConstants.S_MESSAGE_PLP_SHARED_TITLE, owner.getDisplayName());
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    String messageText = String.format(MessageConstants.S_MESSAGE_PLP_SHARED_WITH_MANAGER,
        path.getTitle(), sharedMessage, DateUtils.format(path.getDueDateUpdated(), "MM/dd/yyyy"));
    messageService.sendSharedPathNotificationToUser(title, messageText, managerId, personalPlanId);
  }

  @Override
  public void shareWithDirectReports(Long userId, Long personalPlanId, List<Long> directReports,
      String sharedMessage, Date dueDate) {
    Person owner =
        personRepository.findOneByLoginIgnoreCaseAndDeletedIsFalse(String.valueOf(userId));
    List<PersonalPlanShare> shares = directReports.stream().map(dr -> {
      PersonalPlanShare personalPlanShare = PersonalPlanShare.builder()
          .pk(PersonalPlanShare.Pk.builder().ownerUserId(userId).sharedUserId(dr)
              .personalPlanId(personalPlanId).build())
          .type(PersonalPlanShareType.DIRECT_REPORTS).message(sharedMessage)
          .dueDate(new DateTime(dueDate)).build();
      personalPlanShare.setCreatedBy(owner.getDisplayName());
      return personalPlanShare;
    }).collect(Collectors.toList());
    Set<Long> existentShareUserIds =
        shares.stream().map(s -> s.getPk().getSharedUserId()).collect(Collectors.toSet());
    List<PersonalPlanShare> storedShares = personalPlanShareRepository
        .findAllByPkPersonalPlanIdAndType(personalPlanId, PersonalPlanShareType.DIRECT_REPORTS);
    List<PersonalPlanShare> sharesToDelete = storedShares.stream()
        .filter(s -> !existentShareUserIds.contains(s.getPk().getSharedUserId()))
        .collect(Collectors.toList());
    personalPlanShareRepository.delete(sharesToDelete);
    personalPlanShareRepository.save(shares);
    PersonalLearningPathDTO path = personalPlansDAO.getPersonalPlanForUser(userId, personalPlanId);
    sharesToDelete.forEach(s -> {
      // Send a message to the manager about this unshared action
      String title =
          String.format(MessageConstants.S_MESSAGE_PLP_UNSHARED_TITLE, owner.getDisplayName());
      String messageText = String.format(MessageConstants.S_MESSAGE_PLP_UNSHARED_WITH_DIRECT_REPORT,
          path.getTitle(), sharedMessage, DateUtils.format(path.getDueDateUpdated(), "MM/dd/yyyy"));
      messageService.sendSharedPathNotificationToUser(title, messageText,
          s.getPk().getSharedUserId(), personalPlanId);
    });
    shares.forEach(s -> {
      // Send a message to the manager about this shared action
      String title =
          String.format(MessageConstants.S_MESSAGE_PLP_SHARED_TITLE, owner.getDisplayName());
      String messageText = String.format(MessageConstants.S_MESSAGE_PLP_SHARED_WITH_DIRECT_REPORT,
          path.getTitle(), sharedMessage, DateUtils.format(path.getDueDateUpdated(), "MM/dd/yyyy"));
      messageService.sendSharedPathNotificationToUser(title, messageText,
          s.getPk().getSharedUserId(), personalPlanId);
    });
  }

  @Override
  public List<PersonalPlanShare> findSharesWithDirectReports(Long planId) {
    List<Long> list = new ArrayList<>();
    list.add(planId);
    return findSharesWithDirectReports(list);
  }

  @Override
  public List<PersonalPlanShare> findSharesWithDirectReports(List<Long> planIds) {
    if (planIds == null || planIds.isEmpty()) {
      return new ArrayList<>();
    }
    return personalPlanShareRepository.findAllByPkPersonalPlanIdInAndType(planIds,
        PersonalPlanShareType.DIRECT_REPORTS);
  }

}
