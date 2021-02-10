package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentQuizContentQuestionAnswerDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentQuizContentQuestionDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Course player activity content quiz question dto.
 */
@Data
public class CoursePlayerActivityContentQuizQuestionDTO {
    private Long questionId;
    private String questionText;
    private Long slot;
    private String currentUserAnswer;
    private String sequenceCheck;
    private String type;
    private List<CoursePlayerActivityContentQuizQuestionAnswerDTO> answers;
    private List<CoursePlayerActivityContentQuizContentQuestionMatchingStemDTO> matchingStems;
    private List<CoursePlayerActivityContentQuizContentQuestionMatchingChoiceDTO> matchingChoices;

    /**
     * Value of course player activity content quiz question dto.
     *
     * @param bo the bo
     * @return the course player activity content quiz question dto
     */
    public static CoursePlayerActivityContentQuizQuestionDTO valueOf(
            CoursePlayerActivityContentQuizContentQuestionDTO bo) {
        CoursePlayerActivityContentQuizQuestionDTO dto = new CoursePlayerActivityContentQuizQuestionDTO();
        dto.setQuestionId(bo.getQuestionid());
        dto.setQuestionText(bo.getQuestion_text());
        dto.setSlot(bo.getSlot());
        dto.setCurrentUserAnswer(bo.getCurrent_user_answer());
        dto.setSequenceCheck(bo.getSequence_check());
        dto.setType(bo.getType());

        List<CoursePlayerActivityContentQuizQuestionAnswerDTO> answerBOs = new ArrayList<>();

        for (CoursePlayerActivityContentQuizContentQuestionAnswerDTO answerBO : bo.getAnswers()) {
            answerBOs.add(CoursePlayerActivityContentQuizQuestionAnswerDTO.valueOf(answerBO));
        }

        dto.setMatchingStems(
                CoursePlayerActivityContentQuizContentQuestionMatchingStemDTO.valueOf(bo.getMatch_stems()));
        dto.setMatchingChoices(
                CoursePlayerActivityContentQuizContentQuestionMatchingChoiceDTO.valueOf(bo.getMatch_choices()));

        dto.setAnswers(answerBOs);

        return dto;
    }
}
