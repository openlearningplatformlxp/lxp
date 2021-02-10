package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.LearningLockerJobExecution;
import com.redhat.uxl.dataobjects.domain.types.LearningLockerJobExecutionType;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Learning locker job execution repository.
 */
public interface LearningLockerJobExecutionRepository
        extends BaseJpaRepository<LearningLockerJobExecution, Long>, JpaSpecificationExecutor<LearningLockerJobExecution> {

    /**
     * Find one by type order by created date desc list.
     *
     * @param learningLockerJobExecutionType the learning locker job execution type
     * @param limit                          the limit
     * @return the list
     */
    @Query("select l from LearningLockerJobExecution l where l.type = ?1 order by l.createdDate desc")
    List<LearningLockerJobExecution> findOneByTypeOrderByCreatedDateDesc(
            LearningLockerJobExecutionType learningLockerJobExecutionType, Pageable limit);

}
