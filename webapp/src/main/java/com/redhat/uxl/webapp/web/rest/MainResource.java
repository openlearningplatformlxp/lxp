package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.ProfileService;
import com.redhat.uxl.services.service.ProgramItemService;
import com.redhat.uxl.services.service.ProgramStatisticsService;
import com.redhat.uxl.services.service.dto.AppointmentItemDTO;
import com.redhat.uxl.services.service.dto.LearningPathProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.ProgramItemDTO;
import com.redhat.uxl.services.service.dto.ProgramItemWrapperDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.dto.HomeDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * The type Main resource.
 */
@RestController
@RequestMapping(value = "/api/main", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MainResource extends BaseResource {

    @Inject
    private ProgramItemService programItemService;
    @Inject
    private ProgramStatisticsService programStatisticsService;
    @Inject
    private ProfileService profileService;

    /**
     * Gets home data.
     *
     * @return the home data
     * @throws ExecutionException   the execution exception
     * @throws InterruptedException the interrupted exception
     */
    @ApiOperation(value = "Get home data", notes = "<p>Get info for home page.</p>")
    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public HomeDTO getHomeData() throws ExecutionException, InterruptedException {
        log.debug("REST request to get home data");
        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        TotaraUserDTO manager = profileService.getFirstManager(currentUserId);

        HomeDTO homeDTO = new HomeDTO();

        homeDTO.setNextAppointmentDate(new DateTime().plusHours(12));
        CompletableFuture<ProgramItemWrapperDTO> learningPaths = CompletableFuture.supplyAsync(() -> programItemService
                .getInProgressProgramItemsWrapper(ProgramType.LEARNING_PATH, 0, currentUserId, 3));
        CompletableFuture<ProgramItemWrapperDTO> courses = CompletableFuture.supplyAsync(
                () -> programItemService.getInProgressProgramItemsWrapper(ProgramType.COURSE, 0, currentUserId, 3));
        CompletableFuture<ProgramItemWrapperDTO> classes = CompletableFuture.supplyAsync(
                () -> programItemService.getInProgressProgramItemsWrapper(ProgramType.CLASSROOM, 0, currentUserId, 3));
        CompletableFuture<ProgramItemWrapperDTO> personalLearningPaths = CompletableFuture.supplyAsync(
                () -> programItemService.getInProgressPersonalProgramItemsWrapper(currentUserId, manager, 3));
        homeDTO.setCourses(courses.get().getProgramItems());
        homeDTO.setTotalCourses(courses.get().getTotalCount());
        homeDTO.setClassrooms(classes.get().getProgramItems());
        homeDTO.setTotalClassrooms(classes.get().getTotalCount());
        homeDTO.setPersonalLearningPaths(personalLearningPaths.get().getProgramItems());
        homeDTO.setTotalPersonalLearningPaths(personalLearningPaths.get().getTotalCount());
        log.debug(String.format("Progress Items were found successfully. Courses: %d Classes: %d",
                courses.get().getTotalCount(), classes.get().getTotalCount()));

        // Filter out any LP that are complete (this can happen when completion criteria is not set up
        // right)
        List<ProgramItemDTO> learningPathCollection = new ArrayList<ProgramItemDTO>();
        List<ProgramItemDTO> learningPathCollectionTemp = learningPaths.get().getProgramItems();

        learningPathCollectionTemp.parallelStream().forEach(program -> {
            log.debug(String.format("Looking for program statistics of %d title %s", program.getId(),
                    program.getTitle()));
            LearningPathProgressionOverviewDTO overviewDTO = programStatisticsService
                    .getProgramStatistics(program.getId(), currentUserId);
            if (overviewDTO.getPercentComplete().compareTo(BigDecimal.valueOf(1)) < 0) {
                // Add it
                log.debug(String.format("Adding %d title %s", program.getId(), program.getTitle()));

                learningPathCollection.add(program);
            }
        });

        if (!learningPathCollection.isEmpty()) {
            homeDTO.setLearningPaths(learningPathCollection);
            homeDTO.setTotalLearningPaths(learningPaths.get().getTotalCount());
            ProgramItemDTO currentProgram = learningPathCollection.get(0);
            log.debug(String.format("Looking for current program %d title %s for main banner", currentProgram.getId(),
                    currentProgram.getTitle()));
            LearningPathProgressionOverviewDTO overviewDTO = programStatisticsService
                    .getProgramStatistics(currentProgram.getId(), currentUserId);
            homeDTO.setCurrentItem(currentProgram);
            homeDTO.setCurrentItemProgression(overviewDTO.getPercentComplete().multiply(new BigDecimal(100)));
            homeDTO.setCurrentItemMinutesLeft(overviewDTO.getTotalMinutesLeft());
            log.debug(String.format("Looking for appointments", currentProgram.getId(), currentProgram.getTitle()));
            homeDTO.setNextAppointments(
                    programItemService.determineAppointments(currentUserId, currentProgram.getId()));
            for (AppointmentItemDTO aDto : homeDTO.getNextAppointments()) {
                BigDecimal duration = overviewDTO.getDurationMap().get(aDto.getCourseId());
                aDto.setDurationMinutes((duration != null) ? duration.multiply(new BigDecimal(60)).intValue() : 0);
            }

        }
        log.debug("Returning home DTO");

        return homeDTO;
    }

}
