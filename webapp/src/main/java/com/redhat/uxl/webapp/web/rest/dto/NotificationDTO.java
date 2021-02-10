package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.dataobjects.domain.types.MessageActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

/**
 * The type Notification dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private Long id;
    private String pictureUrl; // Or maybe is a type?
    private String title;
    private MessageActionType type;
    private String message;
    private Long subjectId;
    private DateTime dateTime;
    private String link;

}
