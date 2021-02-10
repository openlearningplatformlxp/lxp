package com.redhat.uxl.datalayer.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * The interface Base jpa repository.
 *
 * @param <T>  the type parameter
 * @param <ID> the type parameter
 */
@NoRepositoryBean
public interface BaseJpaRepository<T, ID> extends JpaRepository<T, ID> {

    /**
     * Find by id wrapper t.
     *
     * @param id the id
     * @return the t
     */
    default T findByIdWrapper(ID id) {
        return (T) findById(id).orElse(null);
    }

    /**
     * Find one t.
     *
     * @param id the id
     * @return the t
     */
    default T findOne(ID id) {
        return (T) findById(id).orElse(null);
    }

    /**
     * Save list.
     *
     * @param <S>  the type parameter
     * @param var1 the var 1
     * @return the list
     */
    default <S extends T> List<S> save(Iterable<S> var1) {
        return saveAll(var1);
    }

    /**
     * Delete.
     *
     * @param var1 the var 1
     */
    default void delete(Iterable<? extends T> var1) {
        deleteAll(var1);
    }

    /**
     * Find all list.
     *
     * @param var1 the var 1
     * @return the list
     */
    default List<T> findAll(Iterable<ID> var1) {
        return findAllById(var1);
    }
}
