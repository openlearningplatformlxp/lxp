package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.utils.SolrUtils;
import com.redhat.uxl.commonjava.utils.solr.StringSolrBuffer;
import com.redhat.uxl.dataobjects.domain.types.ContentSourceType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.CourseService;
import com.redhat.uxl.services.service.ProgramItemService;
import com.redhat.uxl.services.service.SolrService;
import com.redhat.uxl.services.service.TagsService;
import com.redhat.uxl.services.service.dto.ProgramItemDTO;
import com.redhat.uxl.services.service.dto.ProgramItemWrapperDTO;
import com.redhat.uxl.services.service.dto.SearchDTO;
import com.redhat.uxl.services.service.dto.SearchFilterDTO;
import com.redhat.uxl.services.service.dto.SearchInputFiltersDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The type Search resource.
 */
@RestController
@RequestMapping(value = "/api/search", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class SearchResource extends BaseResource {

    /**
     * The constant MAX_SIZE.
     */
    public static final int MAX_SIZE = 10;
    /**
     * The constant MAX_SIZE_AUTOCOMPLETE.
     */
    public static final int MAX_SIZE_AUTOCOMPLETE = 5;
    @Inject
    private ProgramItemService programItemService;
    @Inject
    private CourseService courseService;
    @Inject
    private SolrService solrService;
    @Inject
    private TagsService tagsService;

    /**
     * Gets tags.
     *
     * @return the tags
     */
    @ApiOperation(value = "Get search filter values", notes = "<p>Get filter tags for skill level and language</p>")
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    @Timed
    public SearchFilterDTO getTags() {
        log.debug("REST request to get search filter values.");
        SearchFilterDTO filterDTO = tagsService.getSearchFilterValues();
        filterDTO.setLocations(courseService.getEventLocations());
        return filterDTO;
    }

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
        search = StringUtils.trim(search);
        StringSolrBuffer bufferSearch = SolrUtils.sanitizeInput(search);
        if ("IN_PROGRESS".equals(status)) {
            Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
            // we will not use solr for in progress items
            ProgramItemWrapperDTO items = new ProgramItemWrapperDTO();
            Page<ProgramItemDTO> p = new PageImpl<>(new ArrayList<>());
            if (type != null) {
                for (ProgramType t : type) {
                    switch (t) {
                    case LEARNING_PATH:
                    case COURSE:
                    case CLASSROOM:
                        items = programItemService.getInProgressProgramItemsWrapper(t, page, currentUserId, MAX_SIZE);
                        // Only find the first type selected for inprogress
                        break;
                    }
                }

                p = new PageImpl<>(items.getProgramItems(), PageRequest.of(page, MAX_SIZE), items.getTotalCount());
            }
            return new SearchDTO(p);

        } else if (role != null) {
            // Show all courses under the role
            Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
            return new SearchDTO(programItemService.findCoursesByTag(currentUserId, role, page, MAX_SIZE));
        } else {
            if (StringUtils.isEmpty(search)) {
                search = "*";
            }
            SearchInputFiltersDTO inputFilters = new SearchInputFiltersDTO();
            inputFilters.setType(type);
            inputFilters.setCms(cms);
            inputFilters.setTopic(topic);
            inputFilters.setDelivery(delivery);
            inputFilters.setSkillLevels(skillLevels);
            inputFilters.setLanguages(languages);
            inputFilters.setCountry(country);
            inputFilters.setCity(city);
            if (inputFilters.getType() != null) {
                // A search for courses / lp / or classes should be only in LMS
                inputFilters.setCms(Arrays.asList(ContentSourceType.LMS));
            }
            return programItemService.searchItems(bufferSearch, inputFilters, page, MAX_SIZE,
                    SecurityUtils.getCurrentLoginAsLong());
        }
    }

    /**
     * Search list.
     *
     * @param search the search
     * @return the list
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    @ApiOperation(value = "Simple Search by a keyword for autocomplete", notes = "<p>Search learning path and courses by keyword. and return simple dto</p>")
    @RequestMapping(value = "/simple-autocomplete/{search}", method = RequestMethod.GET)
    @Timed
    public List<ProgramItemDTO> search(@PathVariable String search) throws UnsupportedEncodingException {
        log.debug("REST request to search by: " + search);
        if (StringUtils.isEmpty(search)) {
            search = "*";
        }
        SearchInputFiltersDTO inputFilters = new SearchInputFiltersDTO();
        SearchDTO dto = programItemService.searchItems(SolrUtils.sanitizeInput(search), inputFilters, 0,
                MAX_SIZE_AUTOCOMPLETE, SecurityUtils.getCurrentLoginAsLong());
        List<ProgramItemDTO> results = new ArrayList<>();
        // First add featured
        results.addAll(dto.getFeaturedItems());
        // Then add normal results
        results.addAll(dto.getItems().getContent());
        results.stream().forEach(p -> {
            if (StringUtils.isNotEmpty(p.getTitle())) {
                p.setTitle(StringUtils.replace(StringUtils.replace(p.getTitle(), "<strong>", ""), "</strong>", ""));
            }
            if (StringUtils.isNotEmpty(p.getDescription())) {
                p.setDescription(
                        StringUtils.replace(StringUtils.replace(p.getDescription(), "<strong>", ""), "</strong>", ""));
            }
            switch (p.getCms()) {
            case LMS:
                switch (p.getType()) {
                case COURSE:
                    p.setImageType("/assets/public/book.svg");
                    break;
                case LEARNING_PATH:
                    p.setImageType("/assets/public/learningpath.svg");
                    break;
                case CLASSROOM:
                    p.setImageType("/assets/public/book.svg");
                    break;
                }
                break;
            case LYNDA:
                p.setImageType("/assets/public/logo-linkedin.png");
                break;
            case KALTURA:
                p.setImageType("/assets/public/logo-kaltura.png");
                break;
            case ALLEGO:
                p.setImageType("/assets/public/logo-allego.png");
                break;
            }

        });
        return results;
    }

}
