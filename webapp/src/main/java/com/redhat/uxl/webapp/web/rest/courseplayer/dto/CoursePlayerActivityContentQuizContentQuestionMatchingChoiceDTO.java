package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Course player activity content quiz content question matching choice dto.
 */
@Data
public class CoursePlayerActivityContentQuizContentQuestionMatchingChoiceDTO {
    private String choice;

    /**
     * Value of list.
     *
     * @param bos the bos
     * @return the list
     */
    public static List<CoursePlayerActivityContentQuizContentQuestionMatchingChoiceDTO> valueOf(
            List<com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentQuizContentQuestionMatchingChoiceDTO> bos) {
        if (null == bos) {
            return new ArrayList<>();
        }

        List<CoursePlayerActivityContentQuizContentQuestionMatchingChoiceDTO> dtos = new ArrayList<>();
        CoursePlayerActivityContentQuizContentQuestionMatchingChoiceDTO dto;
        for (com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentQuizContentQuestionMatchingChoiceDTO bo : bos) {
            dto = new CoursePlayerActivityContentQuizContentQuestionMatchingChoiceDTO();
            dto.setChoice(bo.getChoice());
            dtos.add(dto);
        }
        return dtos;
    }
}
