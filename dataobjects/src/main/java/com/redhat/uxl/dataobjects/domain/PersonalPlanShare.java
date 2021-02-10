package com.redhat.uxl.dataobjects.domain;

import com.redhat.uxl.dataobjects.domain.types.PersonalPlanShareType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * The type Personal plan share.
 */
@Data
@Entity
@Table(name = "personal_plan_share")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalPlanShare extends AbstractAuditingEntity implements Serializable {

    /**
     * The type Pk.
     */
    @Embeddable
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Pk implements Serializable {

    @NotNull
    @Column(name = "owner_user_id")
    private Long ownerUserId;

    @NotNull
    @Column(name = "shared_user_id")
    private Long sharedUserId;

    @NotNull
    @Column(name = "personal_plan_id")
    private Long personalPlanId;
  }

  @EmbeddedId
  private Pk pk;

  @NotNull
  @Column
  @Enumerated(EnumType.STRING)
  private PersonalPlanShareType type;

  @Column
  private String message;

  @Column(name = "due_date")
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime dueDate;

}
