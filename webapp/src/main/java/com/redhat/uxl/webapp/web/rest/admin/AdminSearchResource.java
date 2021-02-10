package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.utils.SolrUtils;
import com.redhat.uxl.commonjava.utils.solr.StringSolrBuffer;
import com.redhat.uxl.dataobjects.domain.types.ContentSourceType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.ProgramItemService;
import com.redhat.uxl.services.service.dto.SearchDTO;
import com.redhat.uxl.services.service.dto.SearchInputFiltersDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * The type Admin search resource.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/admin/search", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminSearchResource extends BaseResource {
    @Inject
    private ProgramItemService programItemService;
    /**
     * The constant MAX_SIZE.
     */
    public static final int MAX_SIZE = 10;

    /**
     * Search search dto.
     *
     * @param type        the type
     * @param status      the status
     * @param topic       the topic
     * @param role        the role
     * @param cms         the cms
     * @param delivery    the delivery
     * @param skillLevels the skill levels
     * @param languages   the languages
     * @param country     the country
     * @param city        the city
     * @param page        the page
     * @return the search dto
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    @ApiOperation(value = "Search by a keyword", notes = "<p>Search learning path and courses by keyword.</p>")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @Timed
    public SearchDTO search(@RequestParam(required = false, value = "type") List<ProgramType> type,
            @RequestParam(required = false, value = "status") String status,
            @RequestParam(required = false, value = "topic") String topic,
            @RequestParam(required = false, value = "role") Long role,
            @RequestParam(required = false, value = "cms") List<ContentSourceType> cms,
            @RequestParam(required = false, value = "delivery") List<Integer> delivery,
            @RequestParam(required = false, value = "skillLevel") String skillLevels,
            @RequestParam(required = false, value = "language") String languages,
            @RequestParam(required = false, value = "country") String country,
            @RequestParam(required = false, value = "city") String city, @RequestParam(required = false) Integer page)
            throws UnsupportedEncodingException {
        return search(null, type, status, role, topic, cms, delivery, skillLevels, languages, country, city, page);
    }

    /**
     * Search search dto.
     *
     * @param search      the search
     * @param type        the type
     * @param status      the status
     * @param role        the role
     * @param topic       the topic
     * @param cms         the cms
     * @param delivery    the delivery
     * @param skillLevels the skill levels
     * @param languages   the languages
     * @param country     the country
     * @param city        the city
     * @param page        the page
     * @return the search dto
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    @ApiOperation(value = "Search by a keyword", notes = "<p>Search learning path and courses by keyword.</p>")
    @RequestMapping(value = "/{search}", method = RequestMethod.GET)
    @Timed
    public SearchDTO search(@PathVariable String search,
            @RequestParam(required = false, value = "type") List<ProgramType> type,
            @RequestParam(required = false, value = "status") String status,
            @RequestParam(required = false, value = "role") Long role,
            @RequestParam(required = false, value = "topic") String topic,
            @RequestParam(required = false, value = "cms") List<ContentSourceType> cms,
            @RequestParam(required = false, value = "delivery") List<Integer> delivery,
            @RequestParam(required = false, value = "skillLevel") String skillLevels,
            @RequestParam(required = false, value = "language") String languages,
            @RequestParam(required = false, value = "country") String country,
            @RequestParam(required = false, value = "city") String city, @RequestParam(required = false) Integer page)
            throws UnsupportedEncodingException {
        log.debug("REST request to search by: " + search);
        if (StringUtils.isNotEmpty(search)) {
            search = URLDecoder.decode(search, "UTF-8");
        }
        search = StringUtils.lowerCase(search);
        if (StringUtils.isEmpty(search)) {
            search = "*";
        }
        StringSolrBuffer buffer = SolrUtils.sanitizeInput(search);
        SearchInputFiltersDTO inputFilters = new SearchInputFiltersDTO();
        inputFilters.setType(type);
        inputFilters.setCms(cms);
        inputFilters.setTopic(topic);
        inputFilters.setDelivery(delivery);
        inputFilters.setSkillLevels(skillLevels);
        inputFilters.setLanguages(languages);
        inputFilters.setCountry(country);
        inputFilters.setCity(city);
        inputFilters.setAdminSearch(true);
        return programItemService.searchItems(buffer, inputFilters, page, MAX_SIZE,
                SecurityUtils.getCurrentLoginAsLong());
    }

}
