package com.redhat.uxl.datalayer.dto;

import com.redhat.uxl.commonjava.utils.StrUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The type Course player section dto.
 */
@Data
public class CoursePlayerSectionDTO {
    private Long id;
    private String name;
    private Long sortOrder;
    private String activitySequence;
    private String summary;
    private List<CoursePlayerActivityDTO> activities;

    /**
     * Instantiates a new Course player section dto.
     *
     * @param map the map
     */
    public CoursePlayerSectionDTO(Map<String, Object> map) {
        this.setId((Long) map.get("id"));
        this.setName((String) map.get("name"));
        this.setSummary((String) map.get("summary"));
        this.setSortOrder((Long) map.get("section"));
        this.setActivitySequence((String) map.get("sequence"));
    }

    /**
     * Sort activities.
     */
    public void sortActivities() {

        if (StrUtils.isEmpty(this.activitySequence)) {
            return;
        }

        List<String> courseModuleIds = Arrays.asList(this.activitySequence.split("\\s*,\\s*"));
        List<CoursePlayerActivityDTO> sortedActivities = new ArrayList<>();

        for (String moduleId : courseModuleIds) {

            Long longId = new Long(moduleId);
            for (CoursePlayerActivityDTO activityBO : this.getActivities()) {
                if (activityBO.getId().equals(longId)) {
                    sortedActivities.add(activityBO);
                    break;
                }
            }
        }
        this.setActivities(sortedActivities);
    }
}
