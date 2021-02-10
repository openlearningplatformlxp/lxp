package com.redhat.uxl.dataobjects.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jsoup.helper.Validate;

/**
 * The type Announcement.
 */
@Data
@Entity
@Table(name = "announcement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Announcement extends AbstractAuditingEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "message")
  private String message;

  @Column(name = "link_url")
  private String linkUrl;

  @Column(name = "link_text")
  private String linkText;

    /**
     * Validate.
     */
    public void validate() {
    Validate.notEmpty(message);
  }
}
