package com.redhat.uxl.dataobjects.domain;

import com.redhat.uxl.dataobjects.domain.types.WikipageStatusType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jsoup.helper.Validate;

/**
 * The type Wikipage.
 */
@Data
@Entity
@Table(name = "wikipage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Wikipage extends AbstractAuditingEntity implements Serializable, Searchable {

    /**
     * The Id.
     */
    @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

    /**
     * The Status.
     */
    @NotNull
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  protected WikipageStatusType status;

    /**
     * The Person author id.
     */
    @NotNull
  @Column(name = "person_author_id")
  protected Long personAuthorId;

    /**
     * The Title.
     */
    @Column(name = "title")
  protected String title;

    /**
     * The Url.
     */
    @Column(name = "url")
  protected String url;

    /**
     * The Html content.
     */
    @Column(name = "html_content")
  protected String htmlContent;

    /**
     * The Css content.
     */
    @Column(name = "css_content")
  protected String cssContent;

    /**
     * The Index on search.
     */
    @Column(name = "index_on_search")
  protected Boolean indexOnSearch;

    /**
     * Validate.
     */
    public void validate() {
    Validate.notEmpty(title);
    Validate.notEmpty(url);
  }
}
