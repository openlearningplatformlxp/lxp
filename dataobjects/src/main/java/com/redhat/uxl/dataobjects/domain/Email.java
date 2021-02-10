package com.redhat.uxl.dataobjects.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * The type Email.
 */
@Data
@Entity
@Table(name = "email")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Email extends AbstractAuditingEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @OneToOne
  private Person person;

  @Column(name = "email_from", length = 254, nullable = false) // max length:
                                                               // https://en.wikipedia.org/wiki/Email_address
  private String from;

  @Column(name = "email_from_name", length = 32)
  private String fromName;

  @Column(name = "email_to", length = 25653, nullable = false) // 254 (email max) * 100 (emails) +
                                                               // 253 (delimiters)
  private String to;

  @Column(name = "cc", length = 25653) // 254 (email max) * 100 (emails) + 253 (delimiters)
  private String cc;

  @Column(name = "bcc", length = 25653) // 254 (email max) * 100 (emails) + 253 (delimiters)
  private String bcc;

  @Column(length = 998, nullable = false) // max length:
                                          // http://tools.ietf.org/html/rfc5322#section-2.1.1
  private String subject;

  @Column(name = "plain_text", columnDefinition = "CLOB")
  @Lob
  @Type(type = "org.hibernate.type.TextType")
  private String plainText;

  @Column(name = "html_text", columnDefinition = "CLOB")
  @Lob
  @Type(type = "org.hibernate.type.TextType")
  private String htmlText;

  @Column(name = "sent_date")
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime sentDate;

  @Column(name = "attempt_count", nullable = false)
  private Integer attemptCount = 0;

  @Column(name = "last_attempt_date")
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime lastAttemptDate;

  @Column(name = "last_status_message", length = 4096)
  private String lastStatusMessage;
}
