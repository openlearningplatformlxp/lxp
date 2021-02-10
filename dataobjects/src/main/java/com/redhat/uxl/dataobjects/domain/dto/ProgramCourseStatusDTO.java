package com.redhat.uxl.dataobjects.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * The type Program course status dto.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramCourseStatusDTO {

    private boolean enrolled = false;
    private boolean inProgress = false;
    private boolean completed = false;
    private boolean locked = false;
    private boolean optional = false;

    /**
     * Gets percentage.
     *
     * @param manualCompletable the manual completable
     * @return the percentage
     */
    public int getPercentage(boolean manualCompletable) {
        return isCompleted() ? 100 : isInProgress() && !manualCompletable ? 50 : 0;
    }
}
