package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.Wikipage;
import com.redhat.uxl.dataobjects.domain.types.WikipageStatusType;
import com.redhat.uxl.services.service.WikipageService;
import com.redhat.uxl.services.service.dto.TagDTO;
import com.redhat.uxl.services.service.dto.WikipageDTO;
import com.redhat.uxl.services.service.dto.WikipageTreeNodeDTO;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import com.redhat.uxl.webapp.web.rest.dto.PagedSearchDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;

/**
 * The type Admin wikipage resource.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/admin/wikipages", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminWikipageResource extends BaseResource {
    @Inject
    private WikipageService wikipageService;

    /**
     * Search wikipages page.
     *
     * @param pagedSearchDTO the paged search dto
     * @return the page
     */
    @ApiOperation(value = "Search for Feedback.", notes = "<p>Search for Feedback.</p>")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.WIKI_EDITOR)
    @Timed
    Page<Wikipage> searchWikipages(@RequestBody PagedSearchDTO pagedSearchDTO) {

        String type = pagedSearchDTO.getOption("status");
        WikipageStatusType statusType = null;
        if (StringUtils.isNotEmpty(type) && !"ALL".equalsIgnoreCase(type)) {
            statusType = WikipageStatusType.valueOf(type);
        }

        return wikipageService.findForPagedSearch(pagedSearchDTO.getSearchOperation(), pagedSearchDTO.getSearchValue(),
                statusType, pagedSearchDTO.getPageable());
    }

    /**
     * Gets tree.
     *
     * @return the tree
     */
    @ApiOperation(value = "Get tree", notes = "<p>Get tree</p>")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @RolesAllowed(AuthoritiesConstants.WIKI_EDITOR)
    @Timed
    public WikipageTreeNodeDTO getTree() {
        log.debug("REST request to display the wikipage tree");
        return wikipageService.buildTree();
    }

    /**
     * Update wikipage wikipage tree node dto.
     *
     * @param wikipageTree the wikipage tree
     * @return the wikipage tree node dto
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Update a wikipage tree on the system", notes = "<p>Update a wikipage tree on the system</p>")
    @RequestMapping(value = "/tree", method = RequestMethod.PUT)
    @RolesAllowed(AuthoritiesConstants.WIKI_EDITOR)
    @Timed
    public WikipageTreeNodeDTO updateWikipage(@RequestBody WikipageTreeNodeDTO wikipageTree) throws URISyntaxException {
        wikipageService.updateTree(wikipageTree);
        return wikipageTree;
    }

    /**
     * Gets page.
     *
     * @param id the id
     * @return the page
     */
    @ApiOperation(value = "Get wikipage", notes = "<p>Get wikipage</p>")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RolesAllowed(AuthoritiesConstants.WIKI_EDITOR)
    @Timed
    public WikipageDTO getPage(@PathVariable("id") Long id) {
        log.debug("REST request to display a wikipage");
        return wikipageService.findPageByIdWithTags(id);
    }

    /**
     * Save wikipage wikipage.
     *
     * @param wikipageDTO the wikipage dto
     * @return the wikipage
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Create a wikipage on the system", notes = "<p>Create a wikipage on the system</p>")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.WIKI_EDITOR)
    @Timed
    public Wikipage saveWikipage(@RequestBody WikipageDTO wikipageDTO) throws URISyntaxException {
        Long userId = Long.valueOf(SecurityUtils.getCurrentLogin());
        Wikipage wikipage = new Wikipage();
        wikipage.setTitle(wikipageDTO.getTitle());
        wikipage.setUrl(wikipageDTO.getUrl());
        wikipage.setCssContent(wikipageDTO.getCssContent());
        wikipage.setHtmlContent(wikipageDTO.getHtmlContent());
        wikipage.setStatus(wikipageDTO.getStatus());
        wikipage.setIndexOnSearch(wikipageDTO.getIndexOnSearch());
        wikipage.setPersonAuthorId(userId);
        wikipage.setCreatedBy("user");
        wikipage.setCreatedDate(new DateTime());
        return wikipageService.createWikipage(wikipage, wikipageDTO.getTags());
    }

    /**
     * Gets wiki unmatched profile interests.
     *
     * @param wikiId     the wiki id
     * @param searchTerm the search term
     * @return the wiki unmatched profile interests
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Get unmatched wiki tags", notes = "<p>Get unmatched wiki tags</p>")
    @RequestMapping(value = "/tags/unmatched", method = RequestMethod.GET)
    @Timed
    public List<TagDTO> getWikiUnmatchedProfileInterests(@RequestParam(value = "id", required = false) Long wikiId,
            @RequestParam(required = false, value = "q") String searchTerm) throws URISyntaxException {
        log.debug("REST request to get user unmatched user profile interests:");
        return wikipageService.getUnmatchedWikiTags(wikiId, searchTerm);
    }

    /**
     * Update wikipage wikipage.
     *
     * @param wikipage the wikipage
     * @return the wikipage
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Update a wikipage on the system", notes = "<p>Update a wikipage on the system</p>")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @RolesAllowed(AuthoritiesConstants.WIKI_EDITOR)
    @Timed
    public Wikipage updateWikipage(@RequestBody WikipageDTO wikipage) throws URISyntaxException {
        Wikipage storedWikipage = wikipageService.findPageById(wikipage.getId());
        storedWikipage.setLastModifiedBy("user");
        storedWikipage.setLastModifiedDate(new DateTime());
        wikipageService.deleteWikipageTags(storedWikipage.getId());
        storedWikipage.setTitle(wikipage.getTitle());
        storedWikipage.setUrl(wikipage.getUrl());
        storedWikipage.setCssContent(wikipage.getCssContent());
        storedWikipage.setHtmlContent(wikipage.getHtmlContent());
        storedWikipage.setStatus(wikipage.getStatus());
        storedWikipage.setIndexOnSearch(wikipage.getIndexOnSearch());
        return wikipageService.updateWikipage(storedWikipage, wikipage.getTags());
    }
}
