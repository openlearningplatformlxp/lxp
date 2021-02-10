package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.PersonAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The interface Person access repository.
 */
public interface PersonAccessRepository
    extends BaseJpaRepository<PersonAccess, Long>, JpaSpecificationExecutor<PersonAccess> {

}
