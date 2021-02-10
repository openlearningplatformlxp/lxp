package com.redhat.uxl.services.service;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.Wikipage;
import com.redhat.uxl.dataobjects.domain.types.WikipageStatusType;
import com.redhat.uxl.services.service.dto.TagDTO;
import com.redhat.uxl.services.service.dto.WikipageDTO;
import com.redhat.uxl.services.service.dto.WikipageTreeNodeDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Wikipage service.
 */
public interface WikipageService {

    /**
     * Display page wikipage dto.
     *
     * @param url the url
     * @return the wikipage dto
     */
    @Timed
    @Transactional
    WikipageDTO displayPage(String url);

    /**
     * Find page by id wikipage.
     *
     * @param id the id
     * @return the wikipage
     */
    @Timed
    @Transactional(readOnly = true)
    Wikipage findPageById(Long id);

    /**
     * Find page by id with tags wikipage dto.
     *
     * @param id the id
     * @return the wikipage dto
     */
    @Timed
    @Transactional(readOnly = true)
    WikipageDTO findPageByIdWithTags(Long id);

    /**
     * Find for paged search page.
     *
     * @param searchOperation the search operation
     * @param searchValue     the search value
     * @param statusType      the status type
     * @param pageable        the pageable
     * @return the page
     */
    @Timed
    @Transactional(readOnly = true)
    Page<Wikipage> findForPagedSearch(String searchOperation, String searchValue, WikipageStatusType statusType,
            Pageable pageable);

    /**
     * Create wikipage wikipage.
     *
     * @param wikipage the wikipage
     * @param tags     the tags
     * @return the wikipage
     */
    @Timed
    @Transactional
    Wikipage createWikipage(Wikipage wikipage, List<TagDTO> tags);

    /**
     * Update wikipage wikipage.
     *
     * @param wikipage the wikipage
     * @param tags     the tags
     * @return the wikipage
     */
    @Timed
    @Transactional
    Wikipage updateWikipage(Wikipage wikipage, List<TagDTO> tags);

    /**
     * Gets unmatched wiki tags.
     *
     * @param wikiId     the wiki id
     * @param searchTerm the search term
     * @return the unmatched wiki tags
     */
    @Timed
    @Transactional(readOnly = true)
    List<TagDTO> getUnmatchedWikiTags(Long wikiId, String searchTerm);

    /**
     * Delete wikipage tags.
     *
     * @param wikiId the wiki id
     */
    @Timed
    @Transactional
    void deleteWikipageTags(Long wikiId);

    /**
     * Build tree wikipage tree node dto.
     *
     * @return the wikipage tree node dto
     */
    @Timed
    @Transactional(readOnly = true)
    WikipageTreeNodeDTO buildTree();

    /**
     * Update tree.
     *
     * @param wikipageTree the wikipage tree
     */
    @Timed
    @Transactional()
    void updateTree(WikipageTreeNodeDTO wikipageTree);
}
