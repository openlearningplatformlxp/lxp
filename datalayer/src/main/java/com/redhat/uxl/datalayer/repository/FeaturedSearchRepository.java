package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.FeaturedSearch;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The interface Featured search repository.
 */
public interface FeaturedSearchRepository
        extends BaseJpaRepository<FeaturedSearch, Long>, JpaSpecificationExecutor<FeaturedSearch> {

    /**
     * Find featured searches containing list.
     *
     * @param search the search
     * @return the list
     */
    @Query("Select f from FeaturedSearch f where f.keywords like %:search%")
    List<FeaturedSearch> findFeaturedSearchesContaining(@Param("search") String search);
}
