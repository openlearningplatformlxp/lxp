package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentQuizContentQuestionDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Course player activity content quiz dto.
 */
@Data
public class CoursePlayerActivityContentQuizDTO {
    private String name;
    private Long moduleId;
    private Long courseId;
    private Boolean required;
    private Long attemptId;

    private String description;

    private List<CoursePlayerActivityContentQuizQuestionDTO> questions;

    /**
     * Value of course player activity content quiz dto.
     *
     * @param bo the bo
     * @return the course player activity content quiz dto
     */
    public static CoursePlayerActivityContentQuizDTO valueOf(CoursePlayerActivityContentDTO bo) {

        CoursePlayerActivityContentQuizDTO dto = new CoursePlayerActivityContentQuizDTO();

        dto.setName(bo.getQuiz().getName());
        dto.setCourseId(bo.getQuiz().getCourseid());
        dto.setModuleId(bo.getQuiz().getModuleid());
        dto.setRequired(bo.getQuiz().getRequired());
        if (bo.getQuiz().getContent() != null) {
            dto.setAttemptId(bo.getQuiz().getContent().getQuizattemptid());
            dto.setDescription(bo.getQuiz().getContent().getDescription());

            List<CoursePlayerActivityContentQuizQuestionDTO> questions = new ArrayList<>();
            for (CoursePlayerActivityContentQuizContentQuestionDTO questionBO : bo.getQuiz().getContent()
                    .getQuestions()) {
                questions.add(CoursePlayerActivityContentQuizQuestionDTO.valueOf(questionBO));
            }

            dto.setQuestions(questions);
        }

        return dto;
    }

}
