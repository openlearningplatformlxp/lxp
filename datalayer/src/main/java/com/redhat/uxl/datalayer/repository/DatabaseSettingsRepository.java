package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.DatabaseSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The interface Database settings repository.
 */
public interface DatabaseSettingsRepository
    extends BaseJpaRepository<DatabaseSettings, Long>, JpaSpecificationExecutor<DatabaseSettings> {
}
