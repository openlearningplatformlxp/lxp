package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.IndexData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Index data repository.
 */
public interface IndexDataRepository
    extends BaseJpaRepository<IndexData, Long>, JpaSpecificationExecutor<IndexData> {

    /**
     * Find recent index index data.
     *
     * @return the index data
     */
    @Query(value = "select i.* from index_data i" + "           order by i.started_on desc limit 1",
      nativeQuery = true)
  IndexData findRecentIndex();

    /**
     * Find last waiting index index data.
     *
     * @return the index data
     */
    @Query(value = "select i.* from index_data i" + "           where i.status = 'WAITING'"
      + "           order by i.started_on desc limit 1", nativeQuery = true)
  IndexData findLastWaitingIndex();
}
