package com.redhat.uxl.dataobjects.domain;

import com.redhat.uxl.dataobjects.domain.types.DeliverTo;
import com.redhat.uxl.dataobjects.domain.types.MessageActionType;
import com.redhat.uxl.dataobjects.domain.types.MessageOrigin;
import com.redhat.uxl.dataobjects.domain.types.MessageSubjectType;
import com.redhat.uxl.dataobjects.domain.types.MessageType;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * The type Messaging.
 */
@Data
@Entity
@Table(name = "messaging")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Messaging extends AbstractAuditingEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(name = "object_type")
  @Enumerated(EnumType.STRING)
  private MessageType objectType;

  @Column(name = "child_id")
  private Long childId;

  @NotNull
  @Column(name = "parent_id")
  private Long parentId;

  @Column(name = "subject_id")
  private Long subjectId;

  @Column(name = "subject_type")
  @Enumerated(EnumType.STRING)
  private MessageSubjectType subjectType;

  @NotNull
  @Column(name = "message")
  private String message;

  @Column(name = "action_buttons")
  private String actionButtons;

  @Column(name = "deliver_to")
  @Enumerated(EnumType.STRING)
  private DeliverTo deliverTo;

  @Column(name = "title")
  private String title;

  @Column(name = "message_type")
  @Enumerated(EnumType.STRING)
  private MessageActionType messageActionType;

  @Column(name = "message_origin")
  @Enumerated(EnumType.STRING)
  private MessageOrigin messageOrigin;

  private Boolean active = true;

  @Column(name = "totara_notification_id")
  private Long totaraNotificationId;

}
