package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.IndexData;
import com.redhat.uxl.dataobjects.domain.PersonSearch;
import com.redhat.uxl.services.service.PersonSearchService;
import com.redhat.uxl.services.service.SolrService;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import com.redhat.uxl.webapp.web.rest.dto.LastIndexRunDTO;
import com.redhat.uxl.webapp.web.rest.dto.PagedSearchDTO;
import com.redhat.uxl.webapp.web.rest.dto.SuccessDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

/**
 * The type Admin audit search resource.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/admin/audit-searches", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminAuditSearchResource extends BaseResource {
    /**
     * The constant MAX_INDEX_PERIOD_HOURS.
     */
    public static final int MAX_INDEX_PERIOD_HOURS = 2;
    @Inject
    private PersonSearchService personSearchService;
    @Inject
    private SolrService solrService;
    @Value("${app.solr.manualKey}")
    private String solrManualKey;

    /**
     * Search audit searches page.
     *
     * @param pagedSearchDTO the paged search dto
     * @return the page
     */
    @ApiOperation(value = "Search for Audit Searches.", notes = "<p>Search for Audit Searches.</p>")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    Page<PersonSearch> searchAuditSearches(@RequestBody PagedSearchDTO pagedSearchDTO) {

        return personSearchService.findForPagedSearch(pagedSearchDTO.getSearchOperation(),
                pagedSearchDTO.getSearchValue(), pagedSearchDTO.getPageable());
    }

    /**
     * Gets last index run.
     *
     * @return the last index run
     */
    @ApiOperation(value = "Get index data.", notes = "<p>Get last index run.</p>")
    @RequestMapping(value = "/lastIndex", method = RequestMethod.GET)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    LastIndexRunDTO getLastIndexRun() {
        LastIndexRunDTO lastIndexRun = new LastIndexRunDTO();
        IndexData indexData = solrService.findLastIndexData();
        if (indexData != null) {
            lastIndexRun.setTime(indexData.getStartedOn().toDate().getTime());
            boolean enabled = !indexData.getStartedOn().isAfter(new LocalDateTime().minusHours(MAX_INDEX_PERIOD_HOURS));
            lastIndexRun.setIndexEnabled(enabled);
        }
        return lastIndexRun;
    }

    /**
     * Reindex manual success dto.
     *
     * @param key the key
     * @return the success dto
     */
    @ApiOperation(value = "Reindex Solr system", notes = "<p>Reindex Solr system with data from our database</p>")
    @RequestMapping(value = "/reindex", method = RequestMethod.GET)
    @Timed
    public SuccessDTO reindexManual(@RequestParam("key") String key) {
        SuccessDTO successDTO = new SuccessDTO();
        if (solrManualKey.equals(key)) {
            Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();
            solrService.saveIndexStart(personTotaraId);
            solrService.reindexSearchDatabase();
        } else {
            successDTO.setSuccess(false);
        }
        return successDTO;
    }

    /**
     * Reindex success dto.
     *
     * @return the success dto
     */
    @ApiOperation(value = "Reindex Solr system", notes = "<p>Reindex Solr system with data from our database</p>")
    @RequestMapping(value = "/reindex", method = RequestMethod.POST)
    @Timed
    public SuccessDTO reindex() {
        log.debug("REST request to reindex");
        Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();
        IndexData indexData = solrService.findLastIndexData();
        boolean enabled = true;
        if (indexData != null) {
            enabled = !indexData.getStartedOn().isAfter(new LocalDateTime().minusHours(MAX_INDEX_PERIOD_HOURS));
        }
        SuccessDTO successDTO = new SuccessDTO();
        if (enabled) {
            solrService.queueIndex(personTotaraId);
        } else {
            successDTO.setSuccess(false);
        }
        return successDTO;
    }
}
