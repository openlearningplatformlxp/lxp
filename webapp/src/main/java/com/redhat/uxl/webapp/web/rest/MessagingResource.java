package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.types.MessageType;
import com.redhat.uxl.services.service.MessageService;
import com.redhat.uxl.services.service.TotaraCourseService;
import com.redhat.uxl.services.service.dto.KeyValueDTO;
import com.redhat.uxl.services.service.dto.MessagingDTO;
import com.redhat.uxl.services.service.dto.TotaraCourseDTO;
import com.redhat.uxl.services.service.dto.TotaraLeaningPathDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.dto.MessageDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Messaging resource.
 */
@RestController
@RequestMapping(value = "/api/message", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MessagingResource extends BaseResource {

    /**
     * The Message service.
     */
    @Inject
    MessageService messageService;

    /**
     * The Course service.
     */
    @Inject
    TotaraCourseService courseService;

    /**
     * Create message messaging dto.
     *
     * @param message the message
     * @return the messaging dto
     */
    @ApiOperation(value = "Add a new message", notes = "<p>Add a new message.</p>")
    @RequestMapping(method = RequestMethod.POST)
    @Timed
    public MessagingDTO createMessage(@RequestBody MessageDTO message) {
        log.debug("REST request to create a message - {}", message.getMessage());
        if (message.getType().equals(MessageType.LEARNING_PATH)) {
            if (message.isDefault()) {
                return messageService.setDefaultLearningPathMessage(message.getMessage());
            } else {
                return messageService.setLearningPathMessage(message.getMessage(), message.getParentId());
            }
        } else {
            if (message.isDefault()) {
                return messageService.setDefaultCourseMessage(message.getMessage());
            } else {
                return messageService.setCourseMessage(message.getMessage(), message.getParentId());
            }
        }
    }

    /**
     * Retrieve message messaging dto.
     *
     * @param message the message
     * @return the messaging dto
     */
    @ApiOperation(value = "Retrieve a message for a specific type and id", notes = "<p>Retrieve a message for a specific type and id.</p>")
    @RequestMapping(value = "/retrieve", method = RequestMethod.POST)
    @Timed
    public MessagingDTO retrieveMessage(@RequestBody MessageDTO message) {
        log.debug("REST request to create a message - {}", message.getMessage());
        if (!SecurityUtils.isAuthenticated()) {
            return null;
        }

        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();

        if (message.getType().equals(MessageType.LEARNING_PATH)) {
            return messageService.retrieveLearningPathMessage(message.getParentId());
        } else if (message.getType().equals(MessageType.NOTIFICATION)) {
            List<MessagingDTO> messagingDTOList = messageService.findNotificationMessagesByUser(currentUserId);
            if (!messagingDTOList.isEmpty()) {
                return messagingDTOList.get(0);
            } else {
                return null;
            }
        } else {
            return messageService.retrieveCourseMessage(message.getParentId());
        }
    }

    /**
     * Dismiss message.
     *
     * @param messageId the message id
     */
    @ApiOperation(value = "Dismiss a message", notes = "<p>Dismiss a message.</p>")
    @RequestMapping(value = "/dismiss/{messageId}", method = RequestMethod.POST)
    @Timed
    public void dismissMessage(@PathVariable Long messageId) {

        log.debug("Dismissing message - {}", messageId);
        messageService.dismissMessage(messageId);
    }

    /**
     * Dismiss all message.
     */
    @ApiOperation(value = "Dismiss all messages", notes = "<p>Dismiss all messages.</p>")
    @RequestMapping(value = "/dismissAll", method = RequestMethod.POST)
    @Timed
    public void dismissAllMessage() {

        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        log.debug("Dismissing all message - {}", currentUserId);
        messageService.dismissAllMessage(currentUserId);
    }

    /**
     * Gets courses.
     *
     * @return the courses
     */
    @ApiOperation(value = "Retrieve course name and Id", notes = "<p>Retrieve course name and Id.</p>")
    @RequestMapping(value = "/courses", method = RequestMethod.GET)
    @Timed
    public List<KeyValueDTO> getCourses() {
        log.debug("REST getting all the courses - ");
        List<TotaraCourseDTO> courses = courseService.findActiveCourses();
        List<KeyValueDTO> retVal = new ArrayList<>();
        for (TotaraCourseDTO dto : courses) {
            KeyValueDTO kv = new KeyValueDTO();
            kv.setKey(Long.toString(dto.getId()));
            kv.setValue(dto.getFullName());
            retVal.add(kv);
        }
        return retVal;
    }

    /**
     * Gets lps.
     *
     * @return the lps
     */
    @ApiOperation(value = "Retrieve course name and Id", notes = "<p>Retrieve course name and Id.</p>")
    @RequestMapping(value = "/lps", method = RequestMethod.GET)
    @Timed
    public List<KeyValueDTO> getLPS() {
        log.debug("REST getting all the programs");
        List<TotaraLeaningPathDTO> programs = courseService.findActivePrograms();
        List<KeyValueDTO> retVal = new ArrayList<>();
        for (TotaraLeaningPathDTO dto : programs) {
            KeyValueDTO kv = new KeyValueDTO();
            kv.setKey(Long.toString(dto.getProgramId()));
            kv.setValue(dto.getProgramName());
            retVal.add(kv);
        }
        return retVal;
    }

    /**
     * Gets my messages.
     *
     * @return the my messages
     */
    @ApiOperation(value = "Retrieve Current Messages", notes = "<p>Retrieve Current Messages.</p>")
    @RequestMapping(value = "/pull", method = RequestMethod.GET)
    @Timed
    public List<MessagingDTO> getMyMessages() {
        log.debug("REST getting all my messages");
        if (!SecurityUtils.isAuthenticated()) {
            return null;
        }
        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        return messageService.findApprovalMessagesByUser(currentUserId);
    }

}
