package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.LearningLockerJobExecutionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The interface Learning locker job execution detail repository.
 */
public interface LearningLockerJobExecutionDetailRepository
    extends BaseJpaRepository<LearningLockerJobExecutionDetail, Long>,
    JpaSpecificationExecutor<LearningLockerJobExecutionDetail> {

}
