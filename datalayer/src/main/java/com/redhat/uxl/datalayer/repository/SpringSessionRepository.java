package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.SpringSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The interface Spring session repository.
 */
public interface SpringSessionRepository
    extends BaseJpaRepository<SpringSession, String>, JpaSpecificationExecutor<SpringSession> {
}
