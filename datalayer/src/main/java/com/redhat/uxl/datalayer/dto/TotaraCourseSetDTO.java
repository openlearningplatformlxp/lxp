package com.redhat.uxl.datalayer.dto;

import com.redhat.uxl.dataobjects.domain.types.TotaraCourseSetCompletionType;
import com.redhat.uxl.dataobjects.domain.types.TotaraCourseSetNextSetOperatorType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Totara course set dto.
 */
@Data
public class TotaraCourseSetDTO implements Comparable {
    private Long id;
    private String label;
    private Long sortOrder;
    private TotaraCourseSetNextSetOperatorType nextSetOperator;
    private TotaraCourseSetCompletionType completionType;
    private List<TotaraCourseContentDTO> courses = new ArrayList<>();
    private Integer minCourses;
    private Long courseSumField;
    private Integer courseSumFieldTotal;
    private Integer userStatus;
    private Integer duration;
    private Boolean isLocked;
    private String description;

    /**
     * Value of totara course set dto.
     *
     * @param map the map
     * @return the totara course set dto
     */
    public static TotaraCourseSetDTO valueOf(Map<String, Object> map) {
        TotaraCourseSetDTO bo = new TotaraCourseSetDTO();
        bo.setId((Long) map.get("coursesetid"));
        bo.setLabel((String) map.get("coursesetlabel"));
        bo.setSortOrder((Long) map.get("coursesetorder"));
        bo.setNextSetOperator(
                TotaraCourseSetNextSetOperatorType.fromInteger((Integer) map.get("coursesetnextsetoperator")));
        bo.setCompletionType(TotaraCourseSetCompletionType.fromInteger((Integer) map.get("coursesetcompletiontype")));
        bo.setMinCourses((Integer) map.get("coursesetmincourses"));
        bo.setCourseSumField((Long) map.get("coursesetsumfield"));
        bo.setCourseSumFieldTotal((Integer) map.get("coursesetsumfieldtotal"));
        bo.setDuration(((Long) map.get("timeallowed")).intValue());
        if (map.containsKey("coursesetstatus")) {
            bo.setUserStatus((Integer) map.get("coursesetstatus"));
        }
        bo.setDescription((String) map.get("description"));
        return bo;
    }

    @Override
    public int compareTo(Object o) {
        TotaraCourseSetDTO a = (TotaraCourseSetDTO) o;
        if (this.getSortOrder() > a.getSortOrder()) {
            return 1;
        } else if (this.getSortOrder() < a.getSortOrder()) {
            return -1;
        }
        return 0;
    }

}
