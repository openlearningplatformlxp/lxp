package com.redhat.uxl.services.service.dto;

import com.redhat.uxl.dataobjects.domain.Messaging;
import com.redhat.uxl.dataobjects.domain.types.DeliverTo;
import com.redhat.uxl.dataobjects.domain.types.MessageActionType;
import com.redhat.uxl.dataobjects.domain.types.MessageOrigin;
import com.redhat.uxl.dataobjects.domain.types.MessageSubjectType;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Messaging dto.
 */
@Data
public class MessagingDTO {
    private Long id;
    private String message;
    private String actionButtons;
    private DeliverTo deliverTo;
    private String title;
    private MessageActionType messageActionType;
    private MessageOrigin messageOrigin;
    private DateTime createDate;
    private Long personId;
    private String firstName;
    private String lastName;
    private Long sourceId;
    private MessageSubjectType subjectType;
    private Long subjectId;

    /**
     * Value of list.
     *
     * @param messagingList the messaging list
     * @return the list
     */
    public static List<MessagingDTO> valueOf(List<Messaging> messagingList) {
        List<MessagingDTO> messagingDTOList = new ArrayList<>();
        for (Messaging m : messagingList) {
            MessagingDTO myMessagingDTO = new MessagingDTO();
            myMessagingDTO.setId(m.getId());
            myMessagingDTO.setMessage(m.getMessage());
            myMessagingDTO.setTitle(m.getTitle());
            myMessagingDTO.setActionButtons(m.getActionButtons());
            myMessagingDTO.setDeliverTo(m.getDeliverTo());
            myMessagingDTO.setMessageActionType(m.getMessageActionType());
            myMessagingDTO.setMessageOrigin(m.getMessageOrigin());
            myMessagingDTO.setCreateDate(m.getCreatedDate());
            myMessagingDTO.setPersonId(m.getParentId());
            myMessagingDTO.setSourceId(m.getChildId());
            myMessagingDTO.setSubjectType(m.getSubjectType());
            myMessagingDTO.setSubjectId(m.getSubjectId());
            messagingDTOList.add(myMessagingDTO);
        }
        return messagingDTOList;
    }
}
