package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.AssetType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Asset type repository.
 */
public interface AssetTypeRepository extends BaseJpaRepository<AssetType, Long> {
    /**
     * Find by enabled is true fetch subtypes list.
     *
     * @return the list
     */
    @Query("select distinct(at) from AssetType at left join fetch at.subtypes order by at.name")
  List<AssetType> findByEnabledIsTrueFetchSubtypes();
}
