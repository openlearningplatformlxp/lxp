package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.MessageService;
import com.redhat.uxl.services.service.dto.MessagingDTO;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import com.redhat.uxl.webapp.web.rest.dto.PagedSearchDTO;
import com.redhat.uxl.webapp.web.rest.dto.SendMessageDTO;
import io.swagger.annotations.ApiOperation;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Admin notification resource.
 */
@RestController
@RequestMapping(value = "/api/admin/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminNotificationResource extends BaseResource {
    @Inject
    private MessageService messageService;

    /**
     * Search notifications page.
     *
     * @param pagedSearchDTO the paged search dto
     * @return the page
     */
    @ApiOperation(value = "Search for Notifications.", notes = "<p>Search for Notifications.</p>")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    Page<MessagingDTO> searchNotifications(@RequestBody PagedSearchDTO pagedSearchDTO) {

        return messageService.findForPagedSearch(pagedSearchDTO.getSearchOperation(), pagedSearchDTO.getSearchValue(),
                pagedSearchDTO.getPageable());
    }

    /**
     * Send notification.
     *
     * @param dto the dto
     */
    @ApiOperation(value = "Send a notification", notes = "<p>Send a notification to users.</p>")
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    void sendNotification(@RequestBody SendMessageDTO dto) {

        if (dto.getProgramId() == null) {
            messageService.sendNotificationToAllUsers(dto.getTitle(), dto.getMessage());
        } else {
            messageService.sendNotificationToLearningPathUsers(dto.getTitle(), dto.getMessage(), dto.getProgramId());
        }
    }
}
