package com.redhat.uxl.services.service.solr.strategies;

import com.redhat.uxl.datalayer.repository.AllegoContentRecipientRepository;
import com.redhat.uxl.dataobjects.domain.dto.AllegoCourseDTO;
import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.types.ContentSourceType;
import com.redhat.uxl.dataobjects.domain.types.CourseDocumentVisibilityType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.dto.SolrAllegoResponseDTO;
import com.redhat.uxl.services.utils.SSLUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

/**
 * The type Allego course solr pager service.
 */
@Slf4j
@Service
public class AllegoCourseSolrPagerServiceImpl
    extends BaseSolrPagerServiceImpl<AllegoCourseDTO, HttpEntity> {

    /**
     * The Local.
     */
    protected boolean local = false;
    /**
     * The Url.
     */
    @Value("${app.allego.api.search.url}")
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
    HttpHeaders headers = new HttpHeaders();
    if (local) {
      headers.add("Cookie", "");
    } else {
      headers.add("Authorization", "Allego " + authKey);
    }

    HttpEntity<String> entity = new HttpEntity<>("", headers);
    return entity;
  }

  @Override
  protected Page<AllegoCourseDTO> findActiveItems(HttpEntity entity, int start, int limit) {
    RestTemplate restTemplate = new RestTemplate();

    start = (start - startPage()) * limit;

    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(url).queryParam("from", start).queryParam("size", limit);

    // ONLY FOR LOCAL
    if (local) {
      // We will hit our dev server as a proxy pass to cloudfront not allowed servers
      SSLUtils.disableSNI();
      builder = UriComponentsBuilder.fromHttpUrl("https://rhl.dev.synegen.com/api/allego/")
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

  private Page<AllegoCourseDTO> doAllegoCall(HttpEntity entity, int start, int limit,
      RestTemplate restTemplate, UriComponentsBuilder builder) {
    log.warn("Calling " + builder.toUriString());
    ResponseEntity<SolrAllegoResponseDTO> response = restTemplate.exchange(builder.toUriString(),
        HttpMethod.GET, entity, SolrAllegoResponseDTO.class);

    // Total is not returned by API so we make a hack here
    int total = 1;
    if (response.getBody() != null && response.getBody().getItems() != null) {
      if (response.getBody().getItems().size() == limit) {
        total = response.getBody().getItems().size() + 1;
      }
      return new PageImpl(response.getBody().getItems(), PageRequest.of(start, limit), total);
    } else {
      return new PageImpl(new ArrayList(), PageRequest.of(start, limit), total);
    }
  }

  @Override
  protected List<CourseDocument> buildDocuments(HttpEntity kalturaClient,
      List<AllegoCourseDTO> content) {

    // If content id is found on the recipient table I keep it. Otherwise I filter that result
    content = content.stream()
        .filter(c -> !allegoContentRecipientRepository.findByContentId(c.getId()).isEmpty())
        .collect(Collectors.toList());

    return content.stream().map((s) -> {

      CourseDocument c = new CourseDocument();

      c.setId(String.valueOf(s.getId()));
      c.setShortName(s.getName());
      c.setFullName(s.getName());
      c.setDescription(s.getDescription());
      c.setType(ProgramType.COURSE);
      c.setFullNameString(s.getName());
      c.setFullNameLowerString(StringUtils.lowerCase(s.getName()));
      c.setShortNameString(s.getName());
      c.setDescriptionString(s.getDescription());
      c.setDescriptionLowerString(StringUtils.lowerCase(s.getDescription()));
      c.setContentSource(ContentSourceType.ALLEGO);
      c.setVisibilityType(CourseDocumentVisibilityType.PUBLIC);
      c.setExternalUrl("https://my.allego.com/play.do?contentId=" + s.getId());
      if (s.getTimeCreated() != null) {
        // We use time without millis
        c.setTimeCreated(s.getTimeCreated().getTime() / 1000);
      }
      return c;
    }).collect(Collectors.toList());
  }

  @Override
  public int startPage() {
    return 1;
  }

  @Override
  public String logName() {
    return "Allego";
  }
}
