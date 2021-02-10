package com.redhat.uxl.services.service.demo.impl;

import com.redhat.uxl.datalayer.dao.TotaraMessageDAO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraMessageDTO;
import com.redhat.uxl.services.service.demo.dto.UserManagerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Demo totara message dao.
 */
@Slf4j
@Service
@ConditionalOnExpression("${demo.mode.enabled:true}")
public class DemoTotaraMessageDAOImpl implements TotaraMessageDAO {

  /**
   * The Demo utility service.
   */
  @Inject DemoUtilityService demoUtilityService;

  @Override
  public List<TotaraMessageDTO> findTotaraNotificationMessages(Long userId) {
    Collection<TotaraMessageDTO> messages = DemoUtilityService.messageMap.values();
    List<TotaraMessageDTO> myMessages = new ArrayList<>();
    for (TotaraMessageDTO message : messages) {
      if (message.getUserId().equals(userId)) {
        myMessages.add(message);
      }
    }
    return myMessages;
  }

    @Override
  public List<TotaraMessageDTO> findTotaraNotificationMessagesForTeam(Long userId) {
    List<UserManagerDTO> managers = demoUtilityService.getManagers();
    Set<Long> userIds = new LinkedHashSet<>();
    for (UserManagerDTO manager : managers) {
      if (manager.getManagerId().equals(userId)) {
        userIds.add(manager.getUserId());
      }
    }
    List<TotaraMessageDTO> messageList = new ArrayList<>();
    for (Long id : userIds) {
      messageList.addAll(findTotaraNotificationMessages(id));
    }
    return messageList;
  }

  @Override
  public List<TotaraMessageDTO> findTotaraNotificationMessagesForMessage(Long userId) {
    return findTotaraNotificationMessages(userId);
  }

  @Override
  public void deleteTotaraMessage(Long messageId) {
    DemoUtilityService.messageMap.remove(messageId);
  }

  @Override
  public void deleteAllTotaraMessageByUser(Long currentUserId) {
    List<TotaraMessageDTO> messages = findTotaraNotificationMessages(currentUserId);
    for (TotaraMessageDTO message : messages) {
      DemoUtilityService.messageMap.remove(message.getId());
    }
  }
}
