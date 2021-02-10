package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.repository.PersonSearchRepository;
import com.redhat.uxl.dataobjects.domain.PersonSearch;
import com.redhat.uxl.services.service.PersonSearchService;
import com.redhat.uxl.services.service.search.SearchBuilder;
import com.redhat.uxl.services.service.search.SearchSpec;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Person search service.
 */
@Service
@Transactional
@Slf4j
public class PersonSearchServiceImpl implements PersonSearchService {

  @Inject
  private PersonSearchRepository personSearchRepository;

  @Override
  @Timed
  @Transactional
  public Page<PersonSearch> findForPagedSearch(String searchOperation, String searchValue,
      Pageable pageable) {
    SearchBuilder<PersonSearch> searchBuilder = new SearchBuilder<>(personSearchRepository);

    searchBuilder =
        searchBuilder.where("or", SearchSpec.valueOf("searchTerm", searchOperation, searchValue));

    Page<PersonSearch> page = searchBuilder.findForPagedSearch(pageable);

    return page;
  }

  @Override
  public List<PersonSearch> getAllAuditSearches() {
    return personSearchRepository.findAll();
  }
}
