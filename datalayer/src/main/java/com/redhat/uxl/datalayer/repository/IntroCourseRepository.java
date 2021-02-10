package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.IntroCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Intro course repository.
 */
public interface IntroCourseRepository
    extends BaseJpaRepository<IntroCourse, Long>, JpaSpecificationExecutor<IntroCourse> {

    /**
     * Find intro course intro course.
     *
     * @return the intro course
     */
    @Query(value = "SELECT c from IntroCourse c where c.id = 1")
  IntroCourse findIntroCourse();

}
