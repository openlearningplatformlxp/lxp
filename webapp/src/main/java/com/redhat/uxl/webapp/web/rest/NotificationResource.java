package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.MessageService;
import com.redhat.uxl.services.service.dto.MessagingDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.dto.NotificationDTO;
import com.redhat.uxl.webapp.web.rest.dto.NotificationUnreadDTO;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Notification resource.
 */
@RestController
@RequestMapping(value = "/api/notification", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class NotificationResource extends BaseResource {

    /**
     * The Message service.
     */
    @Inject
    MessageService messageService;

    @Value("${totara.url.file}")
    private String TOTARA_BASE_URL;

    /**
     * Gets notifications.
     *
     * @return the notifications
     */
    @ApiOperation(value = "Retrieve Notifications", notes = "<p>Retrieve Notifications.</p>")
    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public List<NotificationDTO> getNotifications() {
        log.debug("REST getting all the notifications for user - ");
        if (!SecurityUtils.isAuthenticated()) {
            return null;
        }
        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        List<MessagingDTO> messagingDTOList = messageService.findMessagesByUser(currentUserId);

        List<NotificationDTO> retVal = new ArrayList<>();
        for (MessagingDTO messagingDTO : messagingDTOList) {
            retVal.add(new NotificationDTO(messagingDTO.getId(), "https://via.placeholder.com/45x45",
                    messagingDTO.getTitle(), messagingDTO.getMessageActionType(), messagingDTO.getMessage(),
                    messagingDTO.getSubjectId(), messagingDTO.getCreateDate(),
                    TOTARA_BASE_URL + "/totara/message/alerts.php"));
        }
        return retVal;
    }

    /**
     * Gets unread messages.
     *
     * @return the unread messages
     */
    @ApiOperation(value = "Retrieve Current Notifications Unread", notes = "<p>Retrieve Current Notifications Unread</p>")
    @RequestMapping(value = "/unread/pull", method = RequestMethod.GET)
    @Timed
    public NotificationUnreadDTO getUnreadMessages() {
        log.debug("REST getting count all unread messages");
        if (!SecurityUtils.isAuthenticated()) {
            return null;
        }
        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        return new NotificationUnreadDTO(messageService.getUnreadCount(currentUserId).intValue());
    }

}
