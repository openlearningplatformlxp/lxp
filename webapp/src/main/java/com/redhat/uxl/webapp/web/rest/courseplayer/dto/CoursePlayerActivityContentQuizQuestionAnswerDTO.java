package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentQuizContentQuestionAnswerDTO;
import lombok.Data;

/**
 * The type Course player activity content quiz question answer dto.
 */
@Data
public class CoursePlayerActivityContentQuizQuestionAnswerDTO {
    private Long answerId;
    private String answerText;
    private String feedback;

    /**
     * Value of course player activity content quiz question answer dto.
     *
     * @param bo the bo
     * @return the course player activity content quiz question answer dto
     */
    public static CoursePlayerActivityContentQuizQuestionAnswerDTO valueOf(
            CoursePlayerActivityContentQuizContentQuestionAnswerDTO bo) {
        CoursePlayerActivityContentQuizQuestionAnswerDTO dto = new CoursePlayerActivityContentQuizQuestionAnswerDTO();

        dto.setAnswerId(bo.getAnswerid());
        dto.setAnswerText(bo.getAnswer_text());
        dto.setFeedback(bo.getFeedback());

        return dto;
    }
}
