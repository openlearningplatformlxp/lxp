package com.redhat.uxl.services.service.dto;

import com.redhat.uxl.dataobjects.domain.FeaturedSearchInstance;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * The type Featured search dto.
 */
@Data
public class FeaturedSearchDTO implements Serializable {

    private Long featuredSearchId;
    private String keywords;
    private List<FeaturedSearchInstanceDTO> instances = new ArrayList<>();
    private DateTime createdDate;

    /**
     * Contains instance boolean.
     *
     * @param instance the instance
     * @return the boolean
     */
    public boolean containsInstance(FeaturedSearchInstance instance) {
        for (FeaturedSearchInstanceDTO dto : instances) {
            if (dto.getInstanceId().equals(instance.getInstanceId())
                    && dto.getInstanceType().equals(instance.getInstanceType())) {
                return true;
            }
        }
        return false;
    }
}
