package com.redhat.uxl.services.service;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.IndexData;
import java.util.concurrent.Future;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Solr service.
 */
public interface SolrService {

    /**
     * Save index start.
     *
     * @param userId the user id
     */
    @Transactional
    void saveIndexStart(Long userId);

    /**
     * Queue index.
     *
     * @param personTotaraId the person totara id
     */
    @Transactional
    void queueIndex(Long personTotaraId);

    /**
     * Reindex search database future.
     *
     * @return the future
     */
    @Timed
    @Async
    Future<Integer> reindexSearchDatabase();

    /**
     * Find last index data index data.
     *
     * @return the index data
     */
    @Transactional(readOnly = true)
    IndexData findLastIndexData();

    /**
     * Fin last queued data index data.
     *
     * @return the index data
     */
    IndexData finLastQueuedData();

    /**
     * Update index.
     *
     * @param indexData the index data
     */
    @Transactional
    void updateIndex(IndexData indexData);
}
