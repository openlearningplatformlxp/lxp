package com.redhat.uxl.services.service.solr.strategies;

import com.redhat.uxl.dataobjects.domain.dto.LinkedinCourseDTO;
import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.types.ContentSourceType;
import com.redhat.uxl.dataobjects.domain.types.CourseDocumentVisibilityType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.dto.SolrLinkedinResponseDTO;
import com.redhat.uxl.services.service.dto.SolrLinkedinTokenResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Linkedin course solr pager service.
 */
@Slf4j
@Service
public class LinkedinCourseSolrPagerServiceImpl extends BaseSolrPagerServiceImpl<LinkedinCourseDTO, HttpEntity> {

    /**
     * The Auth url.
     */
    @Value("${app.linkedin.api.auth.url}")
    protected String authUrl;
    /**
     * The Assets url.
     */
    @Value("${app.linkedin.api.assets.url}")
    protected String assetsUrl;
    /**
     * The App key.
     */
    @Value("${app.linkedin.api.client.id}")
    protected String appKey;
    /**
     * The Secret key.
     */
    @Value("${app.linkedin.api.client.secret}")
    protected String secretKey;

    @Override
    protected HttpEntity prepare() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("grant_type", "client_credentials");
        requestBody.add("client_id", appKey);
        requestBody.add("client_secret", secretKey);
        HttpEntity entity = new HttpEntity(requestBody, requestHeaders);
        ResponseEntity<SolrLinkedinTokenResponseDTO> token = restTemplate.exchange(authUrl, HttpMethod.POST, entity, SolrLinkedinTokenResponseDTO.class);
        // Prepare response with new access token
        requestHeaders = new HttpHeaders();
        requestHeaders.set("Authorization", "Bearer " + token.getBody().getAccessToken());
        return new HttpEntity(requestHeaders);
    }

    @Override
    protected int findTotalElements() {
        HttpEntity entity = prepare();
        RestTemplate testRestTemplate = new RestTemplate();
        String parameters = "?assetType=COURSE&start={start}&count={limit}&expandDepth=1&includeRetired=false&q=localeAndType&sourceLocale.country=US&sourceLocale.language=en";
        parameters = StringUtils.replace(parameters, "{start}", 1 + "");
        parameters = StringUtils.replace(parameters, "{limit}", 1 + "");
        ResponseEntity<SolrLinkedinResponseDTO> response = testRestTemplate.exchange(assetsUrl + parameters, HttpMethod.GET, entity, SolrLinkedinResponseDTO.class);
        int total = response.getBody().getPaging().getTotal();
        log.info("Total linkedin courses: " + total);
        return total;
    }

    @Override
    protected Page<LinkedinCourseDTO> findActiveItems(HttpEntity entity, int page, int limit) {
        RestTemplate testRestTemplate = new RestTemplate();
        int start = (100 * page) + 1;
        String parameters = "?assetType=COURSE&start={start}&count={limit}&expandDepth=1&includeRetired=false&q=localeAndType&sourceLocale.country=US&sourceLocale.language=en";
        parameters = StringUtils.replace(parameters, "{start}", start + "");
        parameters = StringUtils.replace(parameters, "{limit}", limit + "");
        ResponseEntity<SolrLinkedinResponseDTO> response = testRestTemplate.exchange(assetsUrl + parameters, HttpMethod.GET, entity, SolrLinkedinResponseDTO.class);
        return new PageImpl(response.getBody().getItems(), PageRequest.of(start,limit), response.getBody().getPaging().getTotal());
    }

    @Override
    protected List<CourseDocument> buildDocuments(HttpEntity kalturaClient, List<LinkedinCourseDTO> content) {

        return content.stream().map((s) -> {
            CourseDocument c = new CourseDocument();
            String id = StringUtils.replace(s.getUrn(), "urn:li:lyndaCourse:", "");
            c.setId(ContentSourceType.LYNDA + "{}" + id);
            c.setShortName(s.getTitle().getValue());
            c.setFullName(s.getTitle().getValue());
            c.setDescription(s.getDetails().getShortDescription().getValue());
            c.setType(ProgramType.COURSE);
            c.setFullNameString(s.getTitle().getValue());
            c.setShortNameString(s.getTitle().getValue());
            c.setFullNameLowerString(StringUtils.lowerCase(s.getTitle().getValue()));
            c.setDescriptionString(s.getDetails().getShortDescription().getValue());
            c.setDescriptionLowerString(StringUtils.lowerCase(s.getDetails().getShortDescription().getValue()));
            c.setContentSource(ContentSourceType.LYNDA);
            c.setVisibilityType(CourseDocumentVisibilityType.PUBLIC);
            c.setExternalUrl(s.getDetails().getUrls().getWebLaunch());
            if (s.getDetails().getTimeToComplete() != null) {
                long hours = s.getDetails().getTimeToComplete().getDuration() / 60 / 60;
                c.setDuration(String.valueOf(hours));
            } else {
                c.setDuration("0");
            }
            return c;
        }).collect(Collectors.toList());
    }

    @Override
    public int startPage() {
        return 0;
    }

    @Override
    public String logName() {
        return "Lynda";
    }
}
