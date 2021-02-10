package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.PersonSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The interface Person search repository.
 */
public interface PersonSearchRepository
    extends BaseJpaRepository<PersonSearch, Long>, JpaSpecificationExecutor<PersonSearch> {

}
