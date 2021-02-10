package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.SpringSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Spring session service.
 */
public interface SpringSessionService {
    /**
     * Find for paged search page.
     *
     * @param searchOperation the search operation
     * @param searchValue     the search value
     * @param pageable        the pageable
     * @return the page
     */
    Page<SpringSession> findForPagedSearch(String searchOperation, String searchValue, Pageable pageable);
}
