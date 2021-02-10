package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.PersonSearch;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Person search service.
 */
public interface PersonSearchService {

    /**
     * Find for paged search page.
     *
     * @param searchOperation the search operation
     * @param searchValue     the search value
     * @param pageable        the pageable
     * @return the page
     */
    Page<PersonSearch> findForPagedSearch(String searchOperation, String searchValue, Pageable pageable);

    /**
     * Gets all audit searches.
     *
     * @return the all audit searches
     */
    List<PersonSearch> getAllAuditSearches();
}
