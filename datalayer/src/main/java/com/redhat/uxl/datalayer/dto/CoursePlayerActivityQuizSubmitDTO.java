package com.redhat.uxl.datalayer.dto;

import lombok.Data;

import java.util.List;

/**
 * The type Course player activity quiz submit dto.
 */
@Data
public class CoursePlayerActivityQuizSubmitDTO {
    /**
     * The Attempt id.
     */
    Long attemptId;
    /**
     * The Questions.
     */
    List<CoursePlayerActivityQuizSubmitQuestionDTO> questions;
}
