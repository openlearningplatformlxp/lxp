package com.redhat.uxl.services.service.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

/**
 * The type Search criterion.
 */
@Data
public class SearchCriterion {
    private String field;
    private String operation;
    private List<String> values = new ArrayList<>();

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        List<String> values = getValues();

        if (values != null && values.size() > 0) {
            return values.get(0);
        } else {
            return null;
        }
    }

    /**
     * Value of search criterion.
     *
     * @param field     the field
     * @param operation the operation
     * @param values    the values
     * @return the search criterion
     */
    public static SearchCriterion valueOf(String field, String operation, String... values) {
        SearchCriterion c = new SearchCriterion();

        c.setField(field);
        c.setOperation(operation);
        c.getValues().addAll(Arrays.asList(values));

        return c;
    }
}
