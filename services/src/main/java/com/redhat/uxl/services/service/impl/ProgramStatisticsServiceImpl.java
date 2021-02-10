package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.services.service.ProgramStatisticsService;
import com.redhat.uxl.services.service.TotaraProgramService;
import com.redhat.uxl.datalayer.dto.LearningPathProgressionDTO;
import com.redhat.uxl.services.service.dto.LearningPathProgressionOverviewDTO;
import com.redhat.uxl.datalayer.dto.TotaraProgramDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The type Program statistics service.
 */
@Service
@Slf4j
@Transactional
public class ProgramStatisticsServiceImpl implements ProgramStatisticsService {

    /**
     * The Totara program service.
     */
    @Inject
    TotaraProgramService totaraProgramService;

    @Override
    public LearningPathProgressionOverviewDTO getProgramStatistics(Long programId, Long userId) {
        LearningPathProgressionOverviewDTO overviewDTO = buildOverview(userId, programId);
        BigDecimal durationComplete = BigDecimal.ZERO;
        Map<Long, BigDecimal> durationMap = new HashMap<>();
        for (LearningPathProgressionDTO pDto : overviewDTO.getProgressions()) {
            // If we have a course that is a program link, we need to get the duration for that program if the flag is
            // not set. This will eliminte nesting
            // Check if program link
            log.debug("Looking for program statistics -> Checking if courses are nested programs");
            Long courseProgramId = totaraProgramService.isCourseANestedProgram(pDto.getCourseId());
            if (courseProgramId != null) {
                LearningPathProgressionOverviewDTO myOverviewDTO = getProgramStatistics(courseProgramId, userId);

                pDto.setDuration(myOverviewDTO.getDurationComplete());

                overviewDTO.setCourseCount(overviewDTO.getCourseCount().add(myOverviewDTO.getCourseCount()));

                durationComplete = durationComplete.add(pDto.getDuration());

            } else if (pDto.getDuration() != null && pDto.getCccompletion() != null && pDto.getCccompletion() != 4) {
                durationComplete = durationComplete.add(pDto.getDuration());
            }
            if (pDto.isCourseComplete()) {
                overviewDTO.setCoursesComplete(overviewDTO.getCoursesComplete().add(BigDecimal.ONE));
            }
            durationMap.put(pDto.getCourseId(), pDto.getDuration());
        }

        log.debug("Returning program statistics results");
        overviewDTO.calculateCompletion();
        overviewDTO.calculateTimeLeft();
        overviewDTO.setDurationMap(durationMap);
        overviewDTO.setDurationComplete(durationComplete);
        return overviewDTO;
    }

    /**
     * Build overview learning path progression overview dto.
     *
     * @param userId    the user id
     * @param programId the program id
     * @return the learning path progression overview dto
     */
    protected LearningPathProgressionOverviewDTO buildOverview(Long userId, Long programId) {
        try {
            final CompletableFuture<TotaraProgramDTO> prog = CompletableFuture
                    .supplyAsync(() -> totaraProgramService.getProgramById(programId));
            final CompletableFuture<List<LearningPathProgressionDTO>> progressions = CompletableFuture
                    .supplyAsync(() -> {
                        List<LearningPathProgressionDTO> dtos = totaraProgramService
                                .findLearningPathProgression(programId, userId);
                        if (dtos == null || dtos.isEmpty()) {
                            // Get unenrolled details
                            dtos = totaraProgramService.findLearningPathProgressionNonEnrolled(programId);
                        }
                        return dtos;
                    });

            final CompletableFuture<BigDecimal> duration = CompletableFuture
                    .supplyAsync(() -> totaraProgramService.findLearningPathTotalDuration(programId).getDuration());

            return LearningPathProgressionOverviewDTO.builder().courseCount(new BigDecimal(progressions.get().size()))
                    .programId(programId).programName(prog.get().getFullName()).totalCourseDuration(duration.get())
                    .coursesComplete(BigDecimal.ZERO).progressions(progressions.get()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
