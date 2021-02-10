package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import com.redhat.uxl.dataobjects.domain.ScormValue;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentFeedbackItemDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * The type Course player activity content dto.
 */
@Data
public class CoursePlayerActivityContentDTO {

    // resource/url/scorm
    private String url;
    private Boolean shouldDisplayInNewWindow;
    private Double videoTime;

    /**
     * The Questions.
     */
    // feedback
    List<CoursePlayerActivityContentFeedbackQuestionDTO> questions;

    /**
     * The Values.
     */
    // scorm
    Map<String, Object> values;

    // quiz
    private CoursePlayerActivityContentQuizDTO quiz;

    private String description;

    // label/page
    private String html;

    /**
     * Value of course player activity content dto.
     *
     * @param bo the bo
     * @return the course player activity content dto
     */
    public static CoursePlayerActivityContentDTO valueOf(
            com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentDTO bo) {
        CoursePlayerActivityContentDTO dto = new CoursePlayerActivityContentDTO();

        if (bo == null) {
            return dto;
        }

        // resource/url/scorm
        dto.setUrl(bo.getUrl());
        dto.setShouldDisplayInNewWindow(bo.getShouldDisplayInNewWindow());
        dto.setVideoTime(bo.getVideoTime());

        // feedback
        if (bo.getItems() != null && !bo.getItems().isEmpty()) {

            List<CoursePlayerActivityContentFeedbackQuestionDTO> questions = new ArrayList<>();
            for (CoursePlayerActivityContentFeedbackItemDTO itemBO : bo.getItems()) {
                questions.add(CoursePlayerActivityContentFeedbackQuestionDTO.valueOf(itemBO));
            }

            dto.setQuestions(questions);
        }

        // scorm
        if (bo.getValues() != null) {
            Map<String, Object> values = new HashMap<>();
            for (ScormValue value : bo.getValues()) {
                values.put(value.getKey(), value.getKey());
            }
            dto.setValues(values);
        } else {
            dto.setValues(new HashMap<>());
        }

        // quiz
        if (bo.getQuiz() != null) {
            dto.setQuiz(CoursePlayerActivityContentQuizDTO.valueOf(bo));
        }

        dto.setDescription(bo.getDescription());
        dto.setHtml(bo.getHtml());

        return dto;
    }
}
