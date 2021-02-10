package com.redhat.uxl.services.service.search;

import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.services.errors.SearchBuilderErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The type Search builder.
 *
 * @param <T> the type parameter
 */
public class SearchBuilder<T> {

    private JpaSpecificationExecutor repository;

    /**
     * Instantiates a new Search builder.
     *
     * @param repository the repository
     */
    public SearchBuilder(JpaSpecificationExecutor<T> repository) {
        this.repository = repository;
    }

    /**
     * The Result.
     */
    Specification<T> result;

    /**
     * Where search builder.
     *
     * @param spec the spec
     * @return the search builder
     */
    public SearchBuilder<T> where(SearchSpec spec) {
        this.result = Specification.where(spec);

        return this;
    }

    /**
     * Where search builder.
     *
     * @param joiner the joiner
     * @param specs  the specs
     * @return the search builder
     */
    public SearchBuilder<T> where(String joiner, SearchSpec... specs) {
        Specification<T> result = buildSpecifications(joiner, specs);

        this.result = result;

        return this;
    }

    /**
     * And search builder.
     *
     * @param spec the spec
     * @return the search builder
     */
    public SearchBuilder<T> and(SearchSpec spec) {
        this.result = Specification.where(this.result).and(spec);

        return this;
    }

    /**
     * And search builder.
     *
     * @param joiner the joiner
     * @param specs  the specs
     * @return the search builder
     */
    public SearchBuilder<T> and(String joiner, SearchSpec... specs) {
        Specification<T> result = buildSpecifications(joiner, specs);

        this.result = Specification.where(this.result).and(result);

        return this;
    }

    /**
     * Find for paged search page.
     *
     * @param pageable the pageable
     * @return the page
     */
    public Page<T> findForPagedSearch(Pageable pageable) {
        return repository.findAll(result, pageable);
    }

    // Private helper methods

    private Specification<T> buildSpecifications(String joiner, SearchSpec... specs) {
        Specification<T> result = specs[0];

        if ("or".equals(joiner)) {
            for (int i = 1; i < specs.length; i++) {
                result = Specification.where(result).or(specs[i]);
            }
        } else if ("and".equals(joiner)) {
            for (int i = 1; i < specs.length; i++) {
                result = Specification.where(result).and(specs[i]);
            }
        } else {
            throw new GeneralException(SearchBuilderErrorCode.INVALID_SPECIFICATION_JOINER);
        }

        return result;
    }
}
