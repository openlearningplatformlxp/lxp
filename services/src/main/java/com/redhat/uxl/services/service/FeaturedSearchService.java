package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.FeaturedSearchInstance;
import com.redhat.uxl.services.service.dto.FeaturedSearchDTO;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Featured search service.
 */
public interface FeaturedSearchService {

    /**
     * Find for paged search page.
     *
     * @param searchOperation the search operation
     * @param searchValue     the search value
     * @param pageable        the pageable
     * @return the page
     */
    @Transactional(readOnly = true)
    Page<FeaturedSearchDTO> findForPagedSearch(String searchOperation, String searchValue, Pageable pageable);

    /**
     * Find featured search by id featured search dto.
     *
     * @param featuredSearchId the featured search id
     * @return the featured search dto
     */
    @Transactional(readOnly = true)
    FeaturedSearchDTO findFeaturedSearchById(Long featuredSearchId);

    /**
     * Create featured search featured search dto.
     *
     * @param userId the user id
     * @param dto    the dto
     * @return the featured search dto
     */
    @Transactional
    FeaturedSearchDTO createFeaturedSearch(Long userId, FeaturedSearchDTO dto);

    /**
     * Update featured search featured search dto.
     *
     * @param userId the user id
     * @param dto    the dto
     * @return the featured search dto
     */
    @Transactional
    FeaturedSearchDTO updateFeaturedSearch(Long userId, FeaturedSearchDTO dto);

    /**
     * Delete feature search.
     *
     * @param featuredSearchId the featured search id
     */
    @Transactional
    void deleteFeatureSearch(Long featuredSearchId);

    /**
     * Find featured search by query set.
     *
     * @param search the search
     * @return the set
     */
    Set<FeaturedSearchInstance> findFeaturedSearchByQuery(String search);
}
