package com.redhat.uxl.datalayer.solr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.io.Serializable;
import java.util.List;

/**
 * The interface Base solr crud repository.
 *
 * @param <T>  the type parameter
 * @param <ID> the type parameter
 */
@NoRepositoryBean
public interface BaseSolrCrudRepository<T, ID extends Serializable> extends SolrCrudRepository<T, ID> {

    /**
     * Save list.
     *
     * @param <S>  the type parameter
     * @param var1 the var 1
     * @return the list
     */
    default <S extends T> List<S> save(Iterable<S> var1) {
        return (List<S>) saveAll(var1);
    }

    /**
     * Find all list.
     *
     * @param var1 the var 1
     * @return the list
     */
    default List<T> findAll(Iterable<ID> var1) {
        return (List<T>) findAllById(var1);
    }

    /**
     * Delete.
     *
     * @param var1 the var 1
     */
    default void delete(Iterable<? extends T> var1) {
        deleteAll(var1);
    }

}
