package com.redhat.uxl.services.service.search;

import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.services.errors.SearchBuilderErrorCode;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Search spec.
 *
 * @param <T> the type parameter
 */
@Data
public class SearchSpec<T> implements Specification<T> {
    private SearchCriterion criterion;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if ("ends with".equals(getCriterion().getOperation())) {
            return cb.like(root.get(criterion.getField()), "%" + criterion.getValue());
        } else if ("equal".equals(getCriterion().getOperation())) {
            return cb.equal(root.<String> get(criterion.getField()), criterion.getValue());
        } else if ("iends with".equals(getCriterion().getOperation())) {
            return cb.like(cb.upper(root.get(criterion.getField())), "%" + criterion.getValue().toUpperCase());
        } else if ("iequal".equals(getCriterion().getOperation())) {
            return cb.equal(cb.upper(root.get(criterion.getField())), criterion.getValue().toUpperCase());
        } else if ("ilike".equals(getCriterion().getOperation())) {
            return cb.like(cb.upper(root.get(criterion.getField())), "%" + criterion.getValue().toUpperCase() + "%");
        } else if ("in".equals(getCriterion().getOperation())) {
            List<SearchSpec<T>> specs = new ArrayList<>();

            for (String value : criterion.getValues()) {
                specs.add(SearchSpec.equals(criterion.getField(), value));
            }

            CriteriaBuilder.In<String> in = cb.in(root.get(criterion.getField()));

            for (String value : criterion.getValues()) {
                in = in.value(value);
            }

            return cb.in(in);
        } else if ("is false".equals(getCriterion().getOperation())) {
            return cb.isFalse(root.get(criterion.getField()));
        } else if ("is not null".equals(getCriterion().getOperation())) {
            return cb.isNotNull(root.<String> get(criterion.getField()));
        } else if ("is null".equals(getCriterion().getOperation())) {
            return cb.isNull(root.<String> get(criterion.getField()));
        } else if ("is true".equals(getCriterion().getOperation())) {
            return cb.isTrue(root.get(criterion.getField()));
        } else if ("istarts with".equals(getCriterion().getOperation())) {
            return cb.like(cb.upper(root.get(criterion.getField())), criterion.getValue().toUpperCase() + "%");
        } else if ("like".equals(getCriterion().getOperation())) {
            return cb.like(root.get(criterion.getField()), "%" + criterion.getValue() + "%");
        } else if ("not in".equals(getCriterion().getOperation())) {
            List<SearchSpec<T>> specs = new ArrayList<>();

            for (String value : criterion.getValues()) {
                specs.add(SearchSpec.equals(criterion.getField(), value));
            }

            CriteriaBuilder.In<String> in = cb.in(root.get(criterion.getField()));

            for (String value : criterion.getValues()) {
                in = in.value(value);
            }

            return cb.not(in);
        } else if ("starts with".equals(getCriterion().getOperation())) {
            return cb.like(root.get(criterion.getField()), criterion.getValue() + "%");
        } else {
            throw new GeneralException(SearchBuilderErrorCode.INVALID_SPECIFICATION_CRITERION_OPERATION);
        }
    }


    /**
     * Value of search spec.
     *
     * @param <T>       the type parameter
     * @param field     the field
     * @param operation the operation
     * @param value     the value
     * @return the search spec
     */
    public static <T> SearchSpec<T> valueOf(String field, String operation, String value) {
        return valueOf(SearchCriterion.valueOf(field, operation, value));
    }


    /**
     * Equals search spec.
     *
     * @param <T>   the type parameter
     * @param field the field
     * @param value the value
     * @return the search spec
     */
    public static <T> SearchSpec<T> equals(String field, String value) {
        return valueOf(SearchCriterion.valueOf(field, "equal", value));
    }

    /**
     * Iequal search spec.
     *
     * @param <T>   the type parameter
     * @param field the field
     * @param value the value
     * @return the search spec
     */
    public static <T> SearchSpec<T> iequal(String field, String value) {
        return valueOf(SearchCriterion.valueOf(field, "iequal", value));
    }

    /**
     * Ilike search spec.
     *
     * @param <T>   the type parameter
     * @param field the field
     * @param value the value
     * @return the search spec
     */
    public static <T> SearchSpec<T> ilike(String field, String value) {
        return valueOf(SearchCriterion.valueOf(field, "ilike", value));
    }

    /**
     * In search spec.
     *
     * @param <T>    the type parameter
     * @param field  the field
     * @param values the values
     * @return the search spec
     */
    public static <T> SearchSpec<T> in(String field, String... values) {
        return valueOf(SearchCriterion.valueOf(field, "in", values));
    }

    /**
     * Is false search spec.
     *
     * @param <T>   the type parameter
     * @param field the field
     * @return the search spec
     */
    public static <T> SearchSpec<T> isFalse(String field) {
        return valueOf(SearchCriterion.valueOf(field, "is false"));
    }

    /**
     * Is not null search spec.
     *
     * @param <T>   the type parameter
     * @param field the field
     * @return the search spec
     */
    public static <T> SearchSpec<T> isNotNull(String field) {
        return valueOf(SearchCriterion.valueOf(field, "is not null"));
    }

    /**
     * Is null search spec.
     *
     * @param <T>   the type parameter
     * @param field the field
     * @return the search spec
     */
    public static <T> SearchSpec<T> isNull(String field) {
        return valueOf(SearchCriterion.valueOf(field, "is null"));
    }

    /**
     * Is true search spec.
     *
     * @param <T>   the type parameter
     * @param field the field
     * @return the search spec
     */
    public static <T> SearchSpec<T> isTrue(String field) {
        return valueOf(SearchCriterion.valueOf(field, "is true"));
    }

    /**
     * Like search spec.
     *
     * @param <T>   the type parameter
     * @param field the field
     * @param value the value
     * @return the search spec
     */
    public static <T> SearchSpec<T> like(String field, String value) {
        return valueOf(SearchCriterion.valueOf(field, "like", value));
    }

    /**
     * Not in search spec.
     *
     * @param <T>    the type parameter
     * @param field  the field
     * @param values the values
     * @return the search spec
     */
    public static <T> SearchSpec<T> notIn(String field, String... values) {
        return valueOf(SearchCriterion.valueOf(field, "not in", values));
    }

    private static <T> SearchSpec<T> valueOf(SearchCriterion criterion) {
        SearchSpec<T> specification = new SearchSpec<T>();

        specification.setCriterion(criterion);

        return specification;
    }
}
