package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.redhat.uxl.datalayer.dao.TotaraCourseDAO;
import com.redhat.uxl.datalayer.dao.TotaraMessageDAO;
import com.redhat.uxl.datalayer.dao.TotaraProfileDAO;
import com.redhat.uxl.datalayer.dao.TotaraUserDAO;
import com.redhat.uxl.datalayer.repository.MessagingRepository;
import com.redhat.uxl.dataobjects.domain.Messaging;
import com.redhat.uxl.dataobjects.domain.dto.TotaraFaceToFaceSessionDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraMessageDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.dataobjects.domain.types.DeliverTo;
import com.redhat.uxl.dataobjects.domain.types.MessageActionType;
import com.redhat.uxl.dataobjects.domain.types.MessageOrigin;
import com.redhat.uxl.dataobjects.domain.types.MessageSubjectType;
import com.redhat.uxl.dataobjects.domain.types.MessageType;
import com.redhat.uxl.dataobjects.domain.util.messaging.CourseApprovalMessageButton;
import com.redhat.uxl.dataobjects.domain.util.messaging.MessageButton;
import com.redhat.uxl.services.constants.MessageConstants;
import com.redhat.uxl.services.service.MessageService;
import com.redhat.uxl.services.service.dto.MessagingDTO;
import com.redhat.uxl.services.service.search.SearchBuilder;
import com.redhat.uxl.services.service.search.SearchSpec;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The type Message service.
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    /**
     * The Messaging repository.
     */
    @Inject
  MessagingRepository messagingRepository;

    /**
     * The Totara profile dao.
     */
    @Inject
  TotaraProfileDAO totaraProfileDAO;

    /**
     * The Totara course dao.
     */
    @Inject
  TotaraCourseDAO totaraCourseDAO;

    /**
     * The Totara message dao.
     */
    @Inject
  TotaraMessageDAO totaraMessageDAO;

    /**
     * The Totara user dao.
     */
    @Inject
  TotaraUserDAO totaraUserDAO;

  @Override
  @Transactional(readOnly = true)
  public MessagingDTO retrieveLearningPathMessage(Long id) {
    return getMessage(MessageType.LEARNING_PATH, id);
  }

  @Override
  @Transactional(readOnly = true)
  public MessagingDTO retrieveCourseMessage(Long id) {
    return getMessage(MessageType.COURSE, id);
  }

  private MessagingDTO getMessage(MessageType type, Long id) {
    Messaging messaging = messagingRepository.findByObjectTypeAndParentId(type, id);
    if (messaging == null) {
      messaging = messagingRepository.findByObjectTypeAndParentId(type, 0L);
    }
    if (messaging == null) {
      log.error("There was an error returning the message {} {}", type.name(), id);
      throw new RuntimeException("There was an error returning the message");
    }
    MessagingDTO m = new MessagingDTO();
    m.setMessage(messaging.getMessage());
    m.setId(messaging.getId());
    return m;
  }

  @Override
  @Transactional
  public MessagingDTO setDefaultLearningPathMessage(String message) {
    return setMessage(MessageType.LEARNING_PATH, message, 0L);
  }

  @Override
  @Transactional
  public MessagingDTO setDefaultCourseMessage(String message) {
    return setMessage(MessageType.COURSE, message, 0L);
  }

  @Override
  public MessagingDTO setLearningPathMessage(String message, Long id) {
    return setMessage(MessageType.LEARNING_PATH, message, id);
  }

  @Override
  public MessagingDTO setCourseMessage(String message, Long id) {
    return setMessage(MessageType.COURSE, message, id);
  }

  private MessagingDTO setMessage(MessageType messageType, String message, Long id) {
    Messaging messaging = messagingRepository.findByObjectTypeAndParentId(messageType, id);
    if (messaging == null) {
      messaging = new Messaging();
    }

    messaging.setMessage(message);
    messaging.setObjectType(messageType);
    messaging.setParentId(id);
    try {
      messagingRepository.save(messaging);
    } catch (Exception e) {
      log.error("There was an error storing the default message for {}! - {}", messageType.name(),
          e.getLocalizedMessage());
      throw new RuntimeException(
          "There was an error storing the default message for " + messageType.name());
    }
    MessagingDTO m = new MessagingDTO();
    return m;
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public Page<MessagingDTO> findForPagedSearch(String searchOperation, String searchValue,
      Pageable pageable) {
    SearchBuilder<Messaging> searchBuilder = new SearchBuilder<>(messagingRepository);
    searchBuilder =
        searchBuilder.where("or", SearchSpec.valueOf("title", searchOperation, searchValue),
            SearchSpec.valueOf("message", searchOperation, searchValue));
    Page<Messaging> messagingPage = searchBuilder.findForPagedSearch(pageable);

    List<MessagingDTO> messagingDTOS = MessagingDTO.valueOf(messagingPage.getContent());

    Set<Long> userIds = new HashSet<>();
    for (MessagingDTO messagingDTO : messagingDTOS) {
      userIds.add(messagingDTO.getPersonId());
    }

    List<TotaraUserDTO> users = totaraUserDAO.findUsersByIds(userIds);
    Map<Long, TotaraUserDTO> userMap = new HashMap<>();
    for (TotaraUserDTO user : users) {
      userMap.put(user.getId(), user);
    }

    for (MessagingDTO messagingDTO : messagingDTOS) {
      TotaraUserDTO user = userMap.get(messagingDTO.getPersonId());
      if (user != null) {
        messagingDTO.setFirstName(user.getFirstName());
        messagingDTO.setLastName(user.getLastName());
      }
    }

    Page<MessagingDTO> page =
        new PageImpl<>(messagingDTOS, pageable, messagingPage.getTotalElements());
    return page;
  }

  @Override
  public List<MessagingDTO> findMessagesByUser(Long id) {
    List<DeliverTo> deliverToList = new ArrayList<>();
    deliverToList.add(DeliverTo.ALL_PERSON);
    deliverToList.add(DeliverTo.PERSON);
    List<Messaging> messagings = messagingRepository
        .findByParentIdAndDeliverToInAndActiveOrderByCreatedDateDesc(id, deliverToList, true);
    return MessagingDTO.valueOf(messagings);
  }

  @Override
  public List<MessagingDTO> findApprovalMessagesByUser(Long id) {
    List<DeliverTo> deliverToList = new ArrayList<>();
    deliverToList.add(DeliverTo.ALL_PERSON);
    deliverToList.add(DeliverTo.PERSON);
    List<Messaging> messagings =
        messagingRepository.findByParentIdAndDeliverToInAndMessageActionTypeAndActive(id,
            deliverToList, MessageActionType.APPROVAL, true);
    return MessagingDTO.valueOf(messagings);
  }

  @Override
  public void dismissMessage(Long id) {
    Messaging message = messagingRepository.findOne(id);
    message.setActive(false);
    messagingRepository.save(message);

    // Need to add code here to dismiss totara message
    totaraMessageDAO.deleteTotaraMessage(message.getTotaraNotificationId());
  }

  @Override
  public void dismissAllMessage(Long currentUserId) {
    messagingRepository.readAllMessagesByUser(currentUserId);

    // Need to add code here to dismiss totara message
    totaraMessageDAO.deleteAllTotaraMessageByUser(currentUserId);
  }

  @Override
  public List<MessagingDTO> findNotificationMessagesByUser(Long id) {
    List<TotaraMessageDTO> totaraMessageDTOS = new ArrayList<TotaraMessageDTO>();
    // Find Totara notification messages

    // Need to clean up any totara dismissed messages

    // Check if there are team messages
    totaraMessageDTOS.addAll(totaraMessageDAO.findTotaraNotificationMessagesForTeam(id));

    // Check if there are mdl_messages
    totaraMessageDTOS.addAll(totaraMessageDAO.findTotaraNotificationMessagesForMessage(id));

    // Get all ul messages
    List<Messaging> messagingList =
        messagingRepository.findByParentIdAndDeliverToIn(id, Arrays.asList(DeliverTo.PERSON));
    List<Long> currentMessageList = new ArrayList<>();
    for (Messaging m : messagingList) {
      if (m.getMessageOrigin().equals(MessageOrigin.TOTARA))
        currentMessageList.add(m.getTotaraNotificationId());
    }

    for (TotaraMessageDTO tm : totaraMessageDTOS) {
      if (currentMessageList.contains(tm.getId())) {
        // Once we find it, lets remove it so we can see if we have messages that Totara deleted
        currentMessageList.remove(tm.getId());
        continue;
      }

      Messaging message = new Messaging();
      message.setActive(true);
      message.setMessageOrigin(MessageOrigin.TOTARA);

      if (tm.getUserId() != null) {
        // Use team member for variable clean up...remove HTML as well
        // Check to see if this request requires course approval (code 32) - if so, submit to
        // framework
        if (tm.getAction() != null && tm.getAction() == 32 && tm.getSession() != null) {
          try {
            requestCourseApproval(tm.getSession(), tm.getUserId());
          } catch (Exception e) {
            // Sometimes this happens if the session id cant be found
            log.error("Error creating notification: " + e.getMessage());
            message.setActive(false); // so we dont reprocess...
          }

        }

      }

      if (tm.getTitle().length() >= 100)
        message.setTitle(tm.getTitle().substring(0, 99));
      else
        message.setTitle(tm.getTitle());

      if (tm.getMessage() != null)
        if (tm.getMessage().length() >= 255)
          message.setMessage((Jsoup.parse(tm.getMessage().substring(0, 255)).text()) + "...");
        else
          message.setMessage((Jsoup.parse(tm.getMessage()).text()));
      else
        message.setMessage(message.getTitle());

      message.setObjectType(MessageType.NOTIFICATION);
      message.setMessageActionType(MessageActionType.MESSAGE);
      message.setDeliverTo(DeliverTo.PERSON);
      message.setParentId(id);
      message.setTotaraNotificationId(tm.getId());

      try {
        messagingRepository.save(message);
        MessageButton button = new MessageButton();
        button.setAction("/api/message/dismiss/" + message.getId());
        button.setText("Dismiss");
        Gson gson = new Gson();
        message.setActionButtons(gson.toJson(Arrays.asList(button)));
        messagingRepository.save(message);
      } catch (Exception e) {
        log.error("Error saving message: ", e);
      }

    }

    // Clean up any leftovers
    if (!currentMessageList.isEmpty()) {
      for (Long badMessage : currentMessageList) {
        messagingRepository.deleteByTotaraNotificationId(badMessage);

      }
    }
    return MessagingDTO
        .valueOf(messagingRepository.findByParentIdAndDeliverToInAndActiveOrderByCreatedDateDesc(id,
            Arrays.asList(DeliverTo.PERSON), true));
  }

  @Override
  public List<MessagingDTO> findCourseApprovalRequest(Long sessionId, Long personId) {
    return MessagingDTO
        .valueOf(messagingRepository.findByChildIdAndSubjectTypeAndSubjectIdAndActive(personId,
            MessageSubjectType.FACE_TO_FACE_SESSION, sessionId, true));
  }

  @Override
  public void requestCourseApproval(Long sessionId, Long personId) throws Exception {
    TotaraUserDTO user = totaraProfileDAO.getUserProfile(personId);
    // Need to find the manager
    List<TotaraUserDTO> managers = totaraProfileDAO.getUserManagers(personId);
    if (managers == null || managers.isEmpty()) {
      log.error("There were no managers to approve a request for user: {}, course {}", personId,
          sessionId);
      throw new RuntimeException("No managers found for request");
    }

    if (managers.size() > 1) {
      log.warn("There was more than one manager found for user {}", personId);
    }

    // Need to get the course details
    TotaraFaceToFaceSessionDTO session = totaraCourseDAO.findFaceToFaceSessionById(sessionId);

    // Insert message
    Messaging message = new Messaging();
    message.setTitle(MessageConstants.S_MESSAGE_TITLE_ENROLLMENT_REQUEST);
    message.setMessage(user.getDisplayName() + " would like to enroll in the "
        + session.getCourseName() + " course");
    message.setChildId(personId);
    message.setParentId(managers.get(0).getId());
    message.setSubjectId(sessionId);
    message.setSubjectType(MessageSubjectType.FACE_TO_FACE_SESSION);
    message.setDeliverTo(DeliverTo.PERSON);
    message.setMessageActionType(MessageActionType.APPROVAL);
    message.setObjectType(MessageType.USER);
    message.setMessageOrigin(MessageOrigin.WEBAPP);
    // Insert the message
    message = messagingRepository.save(message);

    // Set Action Buttons
    CourseApprovalMessageButton approve = new CourseApprovalMessageButton();
    approve.setOrder(1);
    approve.setText(MessageConstants.S_MESSAGE_APPROVE);
    approve.setAction("/api/course-enrollment/approve/" + message.getId());
    approve.setPersonId(personId);
    approve.setSessionId(sessionId);
    approve.setCourseId(session.getCourseId());

    CourseApprovalMessageButton reject = new CourseApprovalMessageButton();
    reject.setOrder(1);
    reject.setText(MessageConstants.S_MESSAGE_REJECT);
    reject.setAction("/api/course-enrollment/reject/" + message.getId());
    approve.setPersonId(personId);
    approve.setSessionId(sessionId);
    approve.setCourseId(session.getCourseId());

    List<MessageButton> messageButtonList = Arrays.asList(approve, reject);

    Gson gson = new Gson();
    message.setActionButtons(gson.toJson(messageButtonList));
    messagingRepository.save(message);
    return;
  }

  @Override
  public Long getUnreadCount(Long personId) {
    return messagingRepository.countByParentIdAndActive(personId, true);
  }

  @Override
  @Async
  @Transactional
  public void sendNotificationToAllUsers(String title, String message) {
    List<TotaraUserDTO> users = totaraUserDAO.findAllActiveUsers();
    List<Messaging> messageList = new ArrayList<>();

    for (TotaraUserDTO user : users) {
      Messaging m = new Messaging();
      m.setObjectType(MessageType.USER);
      m.setMessage(message);
      m.setParentId(user.getId());
      m.setMessageOrigin(MessageOrigin.TOTARA);
      m.setDeliverTo(DeliverTo.PERSON);
      m.setMessageActionType(MessageActionType.MESSAGE);
      m.setTitle(title);
      messageList.add(m);
    }
    messagingRepository.save(messageList);
  }

  @Override
  public void sendDueDateNotificationToUser(String title, String message, Long userId,
      Long learningPathId) {
    Messaging m = new Messaging();
    m.setObjectType(MessageType.USER);
    m.setMessage(message);
    m.setParentId(userId);
    m.setMessageOrigin(MessageOrigin.WEBAPP);
    m.setDeliverTo(DeliverTo.PERSON);
    m.setMessageActionType(MessageActionType.DUE_DATE);
    m.setTitle(title);
    m.setSubjectId(learningPathId);
    messagingRepository.save(m);
  }

  @Override
  public void sendSharedPathNotificationToUser(String title, String message, Long userId,
      Long learningPathId) {
    sendSharePathNotificationToUser(title, message, userId, learningPathId,
        MessageActionType.SHARED_PATH);
  }

  private void sendSharePathNotificationToUser(String title, String message, Long userId,
      Long learningPathId, MessageActionType type) {
    Messaging m = new Messaging();
    m.setObjectType(MessageType.USER);
    m.setMessage(message);
    m.setParentId(userId);
    m.setMessageOrigin(MessageOrigin.WEBAPP);
    m.setDeliverTo(DeliverTo.PERSON);
    m.setMessageActionType(type);
    m.setTitle(title);
    m.setSubjectId(learningPathId);
    messagingRepository.save(m);
  }

  @Override
  public void sendNotificationToUser(String title, String message, Long userId) {
    Messaging m = new Messaging();
    m.setObjectType(MessageType.USER);
    m.setMessage(message);
    m.setParentId(userId);
    m.setMessageOrigin(MessageOrigin.WEBAPP);
    m.setDeliverTo(DeliverTo.PERSON);
    m.setMessageActionType(MessageActionType.MESSAGE);
    m.setTitle(title);
    messagingRepository.save(m);
  }

  @Override
  @Async
  @Transactional
  public void sendNotificationToLearningPathUsers(String title, String message, Long programId) {
    List<TotaraUserDTO> users = totaraUserDAO.findAllActiveUsersInProgram(programId);
    List<Messaging> messageList = new ArrayList<>();

    for (TotaraUserDTO user : users) {
      Messaging m = new Messaging();
      m.setObjectType(MessageType.USER);
      m.setMessage(message);
      m.setParentId(user.getId());
      m.setMessageOrigin(MessageOrigin.TOTARA);
      m.setDeliverTo(DeliverTo.PERSON);
      m.setMessageActionType(MessageActionType.MESSAGE);
      m.setTitle(title);
      messageList.add(m);
    }
    messagingRepository.save(messageList);

  }
}
