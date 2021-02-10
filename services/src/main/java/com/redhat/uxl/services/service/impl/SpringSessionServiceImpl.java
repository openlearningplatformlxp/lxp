package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.repository.SpringSessionRepository;
import com.redhat.uxl.dataobjects.domain.SpringSession;
import com.redhat.uxl.services.service.SpringSessionService;
import com.redhat.uxl.services.service.search.SearchBuilder;
import com.redhat.uxl.services.service.search.SearchSpec;
import javax.inject.Inject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Spring session service.
 */
@Service
@Transactional
public class SpringSessionServiceImpl implements SpringSessionService {
  @Inject
  private SpringSessionRepository springSessionRepository;

  @Override
  @Timed
  @Transactional(readOnly = true)
  public Page<SpringSession> findForPagedSearch(String searchOperation, String searchValue,
      Pageable pageable) {
    SearchBuilder<SpringSession> searchBuilder = new SearchBuilder<>(springSessionRepository);

    Page<SpringSession> springSessionsPage = searchBuilder
        .where("or", SearchSpec.valueOf("sessionId", searchOperation, searchValue),
            SearchSpec.valueOf("principalName", searchOperation, searchValue))
        .findForPagedSearch(pageable);

    return springSessionsPage;
  }
}
