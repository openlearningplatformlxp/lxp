package com.redhat.uxl.services.service.dto;

import com.redhat.uxl.dataobjects.domain.PersonalPlanShare;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTeamCourseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * The type Team member completion dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberCompletionDTO {
    private TeamMemberDTO teamMember;
    private BigDecimal percentComplete;
    private DateTime dueDate;
    private List<TeamMemberCourseStatsDTO> courseStats;
    private DateTime completedDate;
    private DateTime timeEnrolled;
    private Long daysInProgress;
    private CompletableFuture<LearningPathProgressionOverviewDTO> statisticFuture;

    /**
     * Sets due date from program.
     *
     * @param program the program
     */
    public void setDueDateFromProgram(TotaraProgramDTO program) {
        if (program.getDueDate() != null && program.getDueDate() > 0) {
            setDueDate(new DateTime(program.getDueDate() * 1000));
        }
    }

    /**
     * Sets time enrolled from shared program.
     *
     * @param planShare the plan share
     */
    public void setTimeEnrolledFromSharedProgram(PersonalPlanShare planShare) {
        setTimeEnrolledFromProgramItem(planShare.getCreatedDate().getMillis() / 1000);
    }

    /**
     * Sets time enrolled from program.
     *
     * @param program the program
     */
    public void setTimeEnrolledFromProgram(TotaraProgramDTO program) {
        setTimeEnrolledFromProgramItem(program.getTimeEnrolled());
    }

    /**
     * Sets time enrolled from course.
     *
     * @param course the course
     */
    public void setTimeEnrolledFromCourse(TotaraTeamCourseDTO course) {
        setTimeEnrolledFromProgramItem(course.getTimeEnrolled());
    }

    private void setTimeEnrolledFromProgramItem(Long timeEnrolled) {
        if (timeEnrolled != null && timeEnrolled > 0) {
            setTimeEnrolled(new DateTime(timeEnrolled * 1000));

            // Determine days in progress
            LocalDateTime startDate = LocalDateTime.ofEpochSecond(timeEnrolled, 0, ZoneOffset.UTC);
            LocalDateTime endDate = LocalDateTime.now();
            setDaysInProgress(ChronoUnit.DAYS.between(startDate, endDate));
        }
    }

    /**
     * Calculate percentage from course.
     *
     * @param course the course
     */
    public void calculatePercentageFromCourse(TotaraTeamCourseDTO course) {
        if (course.getStatus() > 49) {
            setPercentComplete(new BigDecimal(100));
            setCompletedDate(new DateTime(course.getCompletedDate() * 1000));
        } else {
            setPercentComplete(BigDecimal.ZERO);
        }
    }

    /**
     * Resolve futures.
     */
    public void resolveFutures() {
        if (statisticFuture != null) {
            try {
                BigDecimal percentage = statisticFuture.get().getPercentComplete();
                if (percentage.compareTo(BigDecimal.ONE) == 1) {
                    setPercentComplete(statisticFuture.get().getPercentComplete());
                } else {
                    // Hack for LP
                    setPercentComplete(statisticFuture.get().getPercentComplete().multiply(new BigDecimal(100)));
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
