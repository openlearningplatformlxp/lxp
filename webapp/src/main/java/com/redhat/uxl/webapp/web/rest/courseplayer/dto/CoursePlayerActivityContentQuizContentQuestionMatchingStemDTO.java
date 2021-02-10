package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Course player activity content quiz content question matching stem dto.
 */
@Data
public class CoursePlayerActivityContentQuizContentQuestionMatchingStemDTO {
    private String value;
    private String choiceIndex; // pre-selected choice from in-flight attempt

    /**
     * Value of list.
     *
     * @param bos the bos
     * @return the list
     */
    public static List<CoursePlayerActivityContentQuizContentQuestionMatchingStemDTO> valueOf(
            List<com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentQuizContentQuestionMatchingStemDTO> bos) {
        if (null == bos) {
            return new ArrayList<>();
        }

        List<CoursePlayerActivityContentQuizContentQuestionMatchingStemDTO> dtos = new ArrayList<>();
        CoursePlayerActivityContentQuizContentQuestionMatchingStemDTO dto;
        for (com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentQuizContentQuestionMatchingStemDTO bo : bos) {
            dto = new CoursePlayerActivityContentQuizContentQuestionMatchingStemDTO();
            dto.setValue(bo.getValue());
            dto.setChoiceIndex(bo.getChoiceIndex());
            dtos.add(dto);
        }
        return dtos;
    }
}
