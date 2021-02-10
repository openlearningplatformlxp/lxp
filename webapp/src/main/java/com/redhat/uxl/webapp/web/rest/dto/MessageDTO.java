package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.dataobjects.domain.types.MessageType;
import lombok.Data;

/**
 * The type Message dto.
 */
@Data
public class MessageDTO {
    private boolean isDefault = false;
    private String message;
    private Long parentId;
    private MessageType type;

}
