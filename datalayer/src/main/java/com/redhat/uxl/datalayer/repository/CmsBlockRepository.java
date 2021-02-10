package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.CmsBlock;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * The interface Cms block repository.
 */
public interface CmsBlockRepository extends BaseJpaRepository<CmsBlock, Long>, JpaSpecificationExecutor<CmsBlock> {
    /**
     * Find all current blocks set.
     *
     * @return the set
     */
    @Query("select cb from CmsBlock cb")
    Set<CmsBlock> findAllCurrentBlocks();

    /**
     * Find one by key cms block.
     *
     * @param key the key
     * @return the cms block
     */
    CmsBlock findOneByKey(String key);

    /**
     * Find by key in set.
     *
     * @param keys the keys
     * @return the set
     */
    Set<CmsBlock> findByKeyIn(Set<String> keys);
}
