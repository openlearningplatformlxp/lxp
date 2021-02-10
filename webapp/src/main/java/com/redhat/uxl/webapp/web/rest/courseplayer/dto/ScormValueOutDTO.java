package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import com.redhat.uxl.dataobjects.domain.ScormValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * The type Scorm value out dto.
 */
@Data
public class ScormValueOutDTO {
    private Map<String, String> scormValues = new HashMap<>();

    /**
     * Value of scorm value out dto.
     *
     * @param scormValues the scorm values
     * @return the scorm value out dto
     */
    public static ScormValueOutDTO valueOf(List<ScormValue> scormValues) {
        ScormValueOutDTO scormValueDTO = new ScormValueOutDTO();

        if (scormValues != null) {
            Map<String, String> valuesMap = scormValueDTO.getScormValues();

            for (ScormValue scormValue : scormValues) {
                valuesMap.put(scormValue.getKey(), scormValue.getValue());
            }
        }

        return scormValueDTO;
    }
}
