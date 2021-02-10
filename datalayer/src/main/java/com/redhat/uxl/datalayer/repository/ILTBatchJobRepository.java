package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.ILTBatchJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The interface Ilt batch job repository.
 */
public interface ILTBatchJobRepository
    extends BaseJpaRepository<ILTBatchJob, Long>, JpaSpecificationExecutor<ILTBatchJob> {
}
