package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.AssetDb;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Asset db repository.
 */
public interface AssetDbRepository extends BaseJpaRepository<AssetDb, Long> {
}
