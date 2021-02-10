package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.commonjava.utils.StrUtils;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The type Paged search dto.
 */
@Data
public class PagedSearchDTO {
    private String searchValue;
    private String searchOperation;
    private int page;
    private int pageSize;

    private Map<String, String> options;
    private List<PagedSearchOrderDTO> sortOrders;

    /**
     * Expand sort orders.
     */
    public void expandSortOrders() {
        List<PagedSearchOrderDTO> sortOrders = getSortOrders();

        if (sortOrders != null && sortOrders.size() > 0) {
            for (int i = sortOrders.size() - 1; i >= 0; i--) {
                PagedSearchOrderDTO sortOrder = sortOrders.get(i);

                String field = sortOrder.getField();

                if (StrUtils.isNotBlank(field)) {
                    String[] fields = field.split("/");

                    if (fields.length > 1) {
                        sortOrder.setField(fields[0]);

                        for (int j = 1; j < fields.length; j++) {
                            sortOrders.add(i + j - 1, PagedSearchOrderDTO.valueOf(fields[j], sortOrder.getDirection()));
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets option.
     *
     * @param name the name
     * @return the option
     */
    public String getOption(String name) {
        if (StrUtils.isBlank(name) || getOptions() == null) {
            return null;
        }

        return getOptions().get(name);
    }

    /**
     * Gets pageable.
     *
     * @param defaultSortOrders the default sort orders
     * @return the pageable
     */
    public Pageable getPageable(Sort.Order... defaultSortOrders) {
        return getPageable(false, defaultSortOrders);
    }

    /**
     * Gets pageable.
     *
     * @param expandSortOrders  the expand sort orders
     * @param defaultSortOrders the default sort orders
     * @return the pageable
     */
    public Pageable getPageable(boolean expandSortOrders, Sort.Order... defaultSortOrders) {
        Sort sort = null;

        if (expandSortOrders) {
            expandSortOrders();
        }

        if (getSortOrders() != null && getSortOrders().size() > 0) {
            List<Sort.Order> orders = new ArrayList<>(getSortOrders().size());

            for (PagedSearchOrderDTO sortOrder : sortOrders) {
                orders.add(new Sort.Order(Sort.Direction.valueOf(sortOrder.getDirection()), sortOrder.getField()));
            }

            sort = Sort.by(orders);
        } else if (defaultSortOrders != null && defaultSortOrders.length > 0) {
            sort = Sort.by(Arrays.asList(defaultSortOrders));
        }

        if (sort == null) {
            return PageRequest.of(getPage(), getPageSize());
        } else {
            return PageRequest.of(getPage(), getPageSize(), sort);
        }
    }

}
