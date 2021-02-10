package com.redhat.uxl.datalayer.dto;

import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * The type Course player activity status availability dto.
 */
@Data
public class CoursePlayerActivityStatusAvailabilityDTO {
    private Long id;
    private Integer completionStatus;
    private Long completionDate;
    private String op;
    private CoursePlayerActivityStatusAvailabilityConstraintDTO[] c;
    private Boolean isLocked;
    private boolean isRequired = false;
    private List<Map<String, Object>> requiredAction;

    /**
     * Value of course player activity status availability dto.
     *
     * @param map the map
     * @return the course player activity status availability dto
     */
    public static CoursePlayerActivityStatusAvailabilityDTO valueOf(Map<String, Object> map) {
        CoursePlayerActivityStatusAvailabilityDTO bo = new CoursePlayerActivityStatusAvailabilityDTO();

        bo.setId((Long) map.get("id"));
        bo.setCompletionStatus((Integer) map.get("completionstate"));
        bo.setIsLocked(false);
        bo.setCompletionDate((Long) map.get("timemodified"));

        if (map.get("availability") != null) {

            try {
                JSONObject availability = new JSONObject((String) map.get("availability"));
                bo.setOp(availability.getString("op"));

                JSONArray cArray = availability.getJSONArray("c");

                JSONArray showCArray = null;
                if (availability.has("showc")) {
                    showCArray = availability.getJSONArray("showc");
                } else if (availability.has("show")) {
                    showCArray = new JSONArray();
                    showCArray.put(0, availability.getBoolean("show"));
                } else {
                    showCArray = new JSONArray();
                    showCArray.put(0, true);
                }

                JSONObject cObject = null;
                CoursePlayerActivityStatusAvailabilityConstraintDTO[] cArrayOut = new CoursePlayerActivityStatusAvailabilityConstraintDTO[cArray
                        .length()];
                CoursePlayerActivityStatusAvailabilityConstraintDTO cOut = null;
                for (int x = 0; x < cArray.length(); x++) {
                    cObject = cArray.getJSONObject(x);
                    cOut = new CoursePlayerActivityStatusAvailabilityConstraintDTO();
                    cOut.setType(cObject.getString("type"));

                    // completion type
                    if (cOut.getType().equals("completion")) {
                        cOut.setCm(cObject.getLong("cm"));
                        cOut.setE(cObject.getLong("e"));
                    }

                    // grade type
                    if (cOut.getType().equals("grade")) {
                        if (cObject.has("min")) {
                            cOut.setMin(cObject.getLong("min"));
                        }

                        if (cObject.has("max")) {
                            cOut.setMin(cObject.getLong("max"));
                        }

                        cOut.setId(cObject.getLong("id"));

                    }

                    if (!showCArray.isNull(x)) {
                        cOut.setShowC(showCArray.getBoolean(x));
                    } else {
                        cOut.setShowC(showCArray.getBoolean(0));
                    }
                    cArrayOut[x] = cOut;
                }
                bo.setC(cArrayOut);

            } catch (Exception e) {
            }

        }

        return bo;
    }
}
