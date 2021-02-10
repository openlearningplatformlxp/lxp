package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.FeaturedSearchInstance;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The interface Featured search instance repository.
 */
public interface FeaturedSearchInstanceRepository
        extends BaseJpaRepository<FeaturedSearchInstance, Long>, JpaSpecificationExecutor<FeaturedSearchInstance> {

    /**
     * Find by featured search id list.
     *
     * @param id the id
     * @return the list
     */
    List<FeaturedSearchInstance> findByFeaturedSearchId(Long id);
}
