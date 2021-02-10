package com.redhat.uxl.services.service.solr.strategies;

import com.redhat.uxl.datalayer.repository.AllegoContentRecipientRepository;
import com.redhat.uxl.dataobjects.domain.AllegoContentRecipient;
import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.services.service.dto.SolrAllegoRecipientResponseDTO;
import com.redhat.uxl.services.utils.SSLUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Allego recipients solr pager service.
 */
@Slf4j
@Service
public class AllegoRecipientsSolrPagerServiceImpl
    extends BaseSolrPagerServiceImpl<AllegoContentRecipient, HttpEntity> {

    /**
     * The constant MAX_GROUPS_SIZE_VALUE.
     */
    public static final int MAX_GROUPS_SIZE_VALUE = 4000;
    /**
     * The Local.
     */
    protected boolean local = false;
    /**
     * The Url.
     */
    @Value("${app.allego.api.recipient.url}")
  protected String url;
    /**
     * The Auth key.
     */
    @Value("${app.allego.api.key.auth}")
  protected String authKey;
  @Inject
  private AllegoContentRecipientRepository allegoContentRecipientRepository;

  @Override
  protected HttpEntity prepare() {

    allegoContentRecipientRepository.deleteAll();

    HttpHeaders headers = new HttpHeaders();
    if (local) {
      // TODO set a local cookie to access the dev server as an auth person
      headers.add("Cookie", "");
    } else {
      headers.add("Authorization", "Allego " + authKey);
    }

    HttpEntity<String> entity = new HttpEntity<>("", headers);
    return entity;
  }

  @Override
  protected Page<AllegoContentRecipient> findActiveItems(HttpEntity entity, int start, int limit) {
    RestTemplate restTemplate = new RestTemplate();
    start = (start - startPage()) * limit;
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(url).queryParam("from", start).queryParam("size", limit);
    // ONLY FOR LOCAL
    if (local) {
      // We will hit our dev server as a proxy pass to cloudfront not allowed servers
      SSLUtils.disableSNI();
      builder =
          UriComponentsBuilder.fromHttpUrl("https://rhl.dev.synegen.com/api/allego/recipients")
              .queryParam("from", start).queryParam("size", limit);
      return doAllegoCall(entity, start, limit, restTemplate, builder);
    } else {
      log.warn("Enabling SNI");
      SSLUtils.enableSNI();
      try {
        log.warn("Trying Allego call");
        return doAllegoCall(entity, start, limit, restTemplate, builder);
      } finally {
        log.warn("Finally Disabling SNI");
        SSLUtils.disableSNI();;
      }
    }
  }

  private Page<AllegoContentRecipient> doAllegoCall(HttpEntity entity, int start, int limit,
      RestTemplate restTemplate, UriComponentsBuilder builder) {
    log.warn("Calling " + builder.toUriString());
    ResponseEntity<SolrAllegoRecipientResponseDTO> response = restTemplate.exchange(
        builder.toUriString(), HttpMethod.GET, entity, SolrAllegoRecipientResponseDTO.class);
    // Total is not returned by API so we make a hack here
    int total = 1;
    if (response.getBody() != null && response.getBody().getItems() != null) {
      if (response.getBody().getItems().size() == limit) {
        total = response.getBody().getItems().size() + 1;
      }
      return new PageImpl(response.getBody().getItems(),PageRequest.of(start, limit), total);
    } else {
      return new PageImpl(new ArrayList(),PageRequest.of(start, limit), total);
    }
  }

  @Override
  protected List<CourseDocument> buildDocuments(HttpEntity client,
      List<AllegoContentRecipient> content) {
    content.stream().forEach(c -> {
      if (StringUtils.contains(c.getGroups(), "Everyone at my company")) {
        c.setCreatedBy("system");
        c.setCreatedDate(new DateTime());
        c.setGroups(StringUtils.left(c.getGroups(), MAX_GROUPS_SIZE_VALUE));
        allegoContentRecipientRepository.save(c);
      }
    });
    // Store content recipients
    return new ArrayList<>();
  }

  @Override
  public int startPage() {
    return 1;
  }

  @Override
  public String logName() {
    return "Allego Recipients";
  }
}
