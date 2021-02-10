package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentFeedbackItemDTO;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

/**
 * The type Course player activity content feedback question dto.
 */
@Data
public class CoursePlayerActivityContentFeedbackQuestionDTO {
    private Long id;
    private String label;
    private String questionText;
    private List<String> choices;
    private String type;
    private String subtype;
    private Integer sortOrder;
    private Boolean required;

    /**
     * Value of course player activity content feedback question dto.
     *
     * @param bo the bo
     * @return the course player activity content feedback question dto
     */
    public static CoursePlayerActivityContentFeedbackQuestionDTO valueOf(
            CoursePlayerActivityContentFeedbackItemDTO bo) {
        CoursePlayerActivityContentFeedbackQuestionDTO dto = new CoursePlayerActivityContentFeedbackQuestionDTO();

        dto.setId(bo.getId());
        dto.setLabel(bo.getLabel());
        dto.setQuestionText(bo.getName());
        dto.setType(bo.getType());
        dto.setSortOrder(bo.getPosition());

        dto.setRequired((bo.getRequired() == 1));

        // process 'presentation' to get choices

        // get subtype
        if (bo.getPresentation().contains(">>>>>")) {
            String subtypeString = bo.getPresentation().substring(0, 6);

            if (subtypeString.equals("r>>>>>")) {
                subtypeString = "radio";
            } else if (subtypeString.equals("d>>>>>")) {
                subtypeString = "dropdown";
            } else if (subtypeString.equals("c>>>>>")) {
                subtypeString = "check";
            }

            dto.setSubtype(subtypeString);

            // set choices
            List<String> choiceStringList = Arrays
                    .asList(bo.getPresentation().substring(6, bo.getPresentation().length()).split("\\|"));

            dto.setChoices(choiceStringList);
        }

        // label process
        if (dto.getType().equals("label")) {
            dto.setQuestionText(bo.getPresentation());
        }

        return dto;
    }
}
