package com.redhat.uxl.services.service.solr.strategies;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaConfiguration;
import com.kaltura.client.enums.KalturaSearchOperatorType;
import com.kaltura.client.enums.KalturaSessionType;
import com.kaltura.client.services.KalturaMediaService;
import com.kaltura.client.types.KalturaFilterPager;
import com.kaltura.client.types.KalturaMediaEntry;
import com.kaltura.client.types.KalturaMediaEntryFilter;
import com.kaltura.client.types.KalturaMediaListResponse;
import com.kaltura.client.types.KalturaMetadata;
import com.kaltura.client.types.KalturaMetadataFilter;
import com.kaltura.client.types.KalturaMetadataListResponse;
import com.kaltura.client.types.KalturaMetadataSearchItem;
import com.kaltura.client.types.KalturaSearchCondition;
import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.types.ContentSourceType;
import com.redhat.uxl.dataobjects.domain.types.CourseDocumentVisibilityType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.dto.SolrKalturaMediaEntrySearchableDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Kaltura course solr pager service.
 */
@Slf4j
@Service
public class KalturaCourseSolrPagerServiceImpl
        extends BaseSolrPagerServiceImpl<SolrKalturaMediaEntrySearchableDTO, KalturaClient> {

    /**
     * The constant EXPIRY.
     */
    public static final int EXPIRY = 8640000;
    /**
     * The constant ANONYMOUS.
     */
    public static final String ANONYMOUS = "anonymous";
    /**
     * The Url.
     */
    @Value("${app.kaltura.api.url}")
    protected String url;
    /**
     * The Partner id.
     */
    @Value("${app.kaltura.api.partnerID}")
    protected int partnerID;
    /**
     * The Category id.
     */
    @Value("${app.kaltura.api.categoryID}")
    protected String categoryID;
    /**
     * The Metadata profile id.
     */
    @Value("${app.kaltura.api.metadataProfileID}")
    protected int metadataProfileID;
    /**
     * The Metadata profile name.
     */
    @Value("${app.kaltura.api.metadataProfileName}")
    protected String metadataProfileName;
    /**
     * The Secret key.
     */
    @Value("${app.kaltura.api.secret}")
    protected String secretKey;

    @Override
    protected KalturaClient prepare() {
        try {
            KalturaConfiguration configuration = new KalturaConfiguration();
            configuration.setEndpoint(url);
            KalturaClient kalturaClient = new KalturaClient(configuration);
            String sessionId = kalturaClient.generateSession(secretKey, ANONYMOUS, KalturaSessionType.ADMIN, partnerID,
                    EXPIRY, "");
            kalturaClient.setSessionId(sessionId);
            return kalturaClient;
        } catch (Exception e) {
            throw new RuntimeException("Failed to preparate session for kaltura client", e);
        }

    }

    @Override
    protected int findTotalElements() {
        KalturaClient entity = prepare();
        Page<SolrKalturaMediaEntrySearchableDTO> page = findActiveItems(entity, 1, 1);
        Long total = page.getTotalElements();
        return total.intValue();
    }

    @Override
    protected Page<SolrKalturaMediaEntrySearchableDTO> findActiveItems(KalturaClient kalturaClient, int page, int maxSize) {
        KalturaMediaService entryService = kalturaClient.getMediaService();
        KalturaMediaEntryFilter filter = new KalturaMediaEntryFilter();
        filter.partnerIdEqual = partnerID;
        KalturaMetadataSearchItem metadataSearchItem = new KalturaMetadataSearchItem();
        metadataSearchItem.metadataProfileId = metadataProfileID;
        metadataSearchItem.type = KalturaSearchOperatorType.SEARCH_OR;
        filter.advancedSearch = metadataSearchItem;
        KalturaSearchCondition condition = new KalturaSearchCondition();
        condition.field = "/*[local-name()='metadata']/*[local-name()='" + metadataProfileName + "']";
        condition.value = "Yes";
        metadataSearchItem.items = new ArrayList<>();
        metadataSearchItem.items.add(condition);
        KalturaFilterPager pager = new KalturaFilterPager();
        pager.pageSize = maxSize;
        pager.pageIndex = page;
        try {
            KalturaMediaListResponse listResponse = entryService.list(filter, pager);
            List<KalturaMediaEntry> results = listResponse.objects;
            List<SolrKalturaMediaEntrySearchableDTO> searchableResults = results.stream().map((r) -> {
                SolrKalturaMediaEntrySearchableDTO s = new SolrKalturaMediaEntrySearchableDTO();
                s.id = r.id;
                s.name = r.name;
                s.description = r.description;
                s.tags = r.tags;
                s.type = r.type;
                s.downloadUrl = r.downloadUrl;
                s.dataUrl = r.dataUrl;
                s.thumbnailUrl = r.thumbnailUrl;
                s.mediaType = r.mediaType;
                s.createdAt = r.createdAt;
                return s;
            }).collect(Collectors.toList());
            return new PageImpl<SolrKalturaMediaEntrySearchableDTO>(searchableResults, PageRequest.of(page,maxSize), listResponse.totalCount);
        } catch (
            KalturaApiException e) {
            throw new RuntimeException("Failed to parse kaltura items", e);
        }

    }

    /**
     * Has metadata boolean.
     *
     * @param kalturaClient the kaltura client
     * @param objectId      the object id
     * @param key           the key
     * @param value         the value
     * @return the boolean
     */
    public boolean hasMetadata(KalturaClient kalturaClient, String objectId, String key, String value) {
        List<KalturaMetadata> metadataList = findMetadata(kalturaClient, objectId);
        boolean hasMetadata = false;
        for (KalturaMetadata m : metadataList) {
            String search = String.format("<%s>%s</%s>", key, value, key);
            if (StringUtils.contains(m.xml, search)) {
                hasMetadata = true;
                break;
            }
        }
        return hasMetadata;
    }

    /**
     * Find metadata value string.
     *
     * @param metadataList the metadata list
     * @param key          the key
     * @return the string
     */
    public String findMetadataValue(List<KalturaMetadata> metadataList, String key) {
        String value = null;
        String openingTag = String.format("<%s>", key);
        String closingTag = String.format("</%s>", key);
        for (KalturaMetadata m : metadataList) {
            if (StringUtils.contains(m.xml, openingTag) && StringUtils.contains(m.xml, closingTag)) {
                value = StringUtils.splitByWholeSeparator(m.xml, openingTag)[1];
                value = StringUtils.splitByWholeSeparator(value, closingTag)[0];
                break;
            }
        }
        return value;
    }

    /**
     * Find metadata list.
     *
     * @param kalturaClient the kaltura client
     * @param objectId      the object id
     * @return the list
     */
    public List<KalturaMetadata> findMetadata(KalturaClient kalturaClient, String objectId) {
        log.info("Looking for metadata for object id: " + objectId);
        KalturaMetadataFilter metadataFilter = new KalturaMetadataFilter();
        metadataFilter.objectIdEqual = objectId;
        try {
            KalturaMetadataListResponse metadataList = kalturaClient.getMetadataService().list(metadataFilter);
            return metadataList.objects;
        } catch (KalturaApiException e) {
            log.error("Failed to retrieve metadata for object id: " + objectId, e);
            return null;
        }

    }

    @Override
    protected List<CourseDocument> buildDocuments(KalturaClient kalturaClient, List<SolrKalturaMediaEntrySearchableDTO> content) {

        return content.stream().map((s) -> {
            CourseDocument c = new CourseDocument();
            c.setId(s.id);
            c.setShortName(s.name);
            c.setFullName(s.name);
            c.setDescription(s.description);
            if (s.tags != null) {
                c.setTags(Arrays.asList(StringUtils.splitByWholeSeparator(s.tags, ", ")));
            }
            c.setType(ProgramType.COURSE);
            c.setFullNameString(s.name);
            c.setFullNameLowerString(StringUtils.lowerCase(s.name));
            c.setShortNameString(s.name);
            c.setDescriptionString(s.description);
            c.setDescriptionLowerString(StringUtils.lowerCase(s.description));
            c.setContentSource(ContentSourceType.KALTURA);
            c.setVisibilityType(CourseDocumentVisibilityType.PUBLIC);
            c.setExternalUrl("https://videos.learning.redhat.com/media/" + s.name + "/" + s.id);
            c.setTimeCreated(new Long(s.createdAt));
            List<KalturaMetadata> metadataList = findMetadata(kalturaClient, s.id);
            c.setLanguage(findMetadataValue(metadataList, "Language"));
            c.setFirstTopic(findMetadataValue(metadataList, "SelectTopic"));
            c.setSkillLevel(findMetadataValue(metadataList, "SkillLevel"));
            return c;
        }).collect(Collectors.toList());
    }

    @Override
    public String logName() {
        return "Kaltura";
    }

    /**
     * Find real url string.
     *
     * @param dataUrl the data url
     * @return the string
     */
    public String findRealUrl(String dataUrl) {
        Validate.notNull(dataUrl);
        RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) {
                connection.setInstanceFollowRedirects(false);
            }
        });
        ResponseEntity<String> result = restTemplate.exchange(dataUrl, HttpMethod.GET, new HttpEntity<String>(""), String.class, new Object());
        String location = result.getHeaders().getLocation() == null ? "" : result.getHeaders().getLocation().toString();
        if (location != null) {
            location = StringUtils.replace(location, "http://", "https://");
        }
        return location;
    }
}
