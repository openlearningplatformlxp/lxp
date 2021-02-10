package com.redhat.uxl.datalayer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * The interface Jpa read only repository.
 *
 * @param <T>  the type parameter
 * @param <ID> the type parameter
 */
public interface JpaReadOnlyRepository<T, ID extends Serializable> extends Repository<T, ID> {

    /**
     * Find all list.
     *
     * @return the list
     */
    List<T> findAll();

    /**
     * Find all list.
     *
     * @param sort the sort
     * @return the list
     */
    List<T> findAll(Sort sort);

    /**
     * Find all list.
     *
     * @param ids the ids
     * @return the list
     */
    List<T> findAll(Iterable<ID> ids);

    /**
     * Gets one.
     *
     * @param id the id
     * @return the one
     */
    T getOne(ID id);

    /**
     * Find all page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<T> findAll(Pageable pageable);

    /**
     * Find one t.
     *
     * @param id the id
     * @return the t
     */
    T findOne(ID id);

    /**
     * Exists boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean exists(ID id);

    /**
     * Count long.
     *
     * @return the long
     */
    long count();
}
