package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.datalayer.dao.TotaraTeamDAO;
import com.redhat.uxl.dataobjects.domain.PersonalPlanShare;
import com.redhat.uxl.dataobjects.domain.dto.PersonalLearningPathDTO;
import com.redhat.uxl.dataobjects.domain.dto.PersonalProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTeamCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserWithCountsDTO;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.AmazonS3Service;
import com.redhat.uxl.services.service.PersonalPlanService;
import com.redhat.uxl.services.service.PersonalPlanShareService;
import com.redhat.uxl.services.service.ProgramStatisticsService;
import com.redhat.uxl.services.service.TeamService;
import com.redhat.uxl.services.service.dto.CourseProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.CourseSetProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.LearningPathProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.ProgramItemDTO;
import com.redhat.uxl.services.service.dto.TeamMemberCompletionDTO;
import com.redhat.uxl.services.service.dto.TeamMemberCourseStatsDTO;
import com.redhat.uxl.services.service.dto.TeamMemberDTO;
import com.redhat.uxl.services.service.dto.TeamMemberProgressOverviewDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * The type Team service.
 */
@Service
@Slf4j
public class TeamServiceImpl implements TeamService {

    /**
     * The Totara team dao.
     */
    @Inject
  TotaraTeamDAO totaraTeamDAO;

    /**
     * The Program statistics service.
     */
    @Inject
  ProgramStatisticsService programStatisticsService;

    /**
     * The Personal plan service.
     */
    @Inject
  PersonalPlanService personalPlanService;
    /**
     * The Personal plan share service.
     */
    @Inject
  PersonalPlanShareService personalPlanShareService;
    /**
     * The Amazon s 3 service.
     */
    @Inject
  AmazonS3Service amazonS3Service;

  @Override
  public Boolean isManager(Long userId) {
    return totaraTeamDAO.isManager(userId);
  }

  @Override
  public List<TeamMemberDTO> findTeamMembersByManager(Long managerId) {

    List<TotaraUserWithCountsDTO> totaraUserWithCounts =
        totaraTeamDAO.findTeamMembersWithCountsByManager(managerId);

    List<TeamMemberDTO> teamMembers = new ArrayList<>();
    for (TotaraUserWithCountsDTO tc : totaraUserWithCounts) {
      TeamMemberDTO teamMember = new TeamMemberDTO();
      teamMember.setActivities(tc.getActivityCount());
      teamMember.setCourses(tc.getCourseCount());
      teamMember.setPaths(tc.getProgramCount());
      teamMember.setFirstName(tc.getFirstName());
      teamMember.setLastName(tc.getLastName());
      teamMember.setCity(tc.getCity());
      teamMember.setCountry(tc.getCountry());
      teamMember.setAvatar(amazonS3Service.makeExternalURL("/profile-image/" + tc.getId()));
      teamMember.setUserId(tc.getId());
      teamMember.setManager(isManager(tc.getId()).booleanValue());
      teamMembers.add(teamMember);
    }

    return teamMembers;
  }

  @Override
  public List<TeamMemberProgressOverviewDTO> findCompletionsByUser(
      List<TeamMemberDTO> teamMembers) {
    List<TeamMemberProgressOverviewDTO> retList = new ArrayList<>();
    retList.addAll(findProgramsCompletionsByUser(teamMembers));
    retList.addAll(findCourseCompletionsByUser(teamMembers));
    return retList;
  }

  @Override
  public List<TeamMemberProgressOverviewDTO> findProgramsCompletionsByUser(
      List<TeamMemberDTO> teamMembers) {
    Map<Long, TeamMemberDTO> userIdTeamMembers =
        teamMembers.stream().collect(Collectors.toMap(TeamMemberDTO::getUserId, t -> t));
    try {
      final CompletableFuture<List<TotaraProgramDTO>> programList = CompletableFuture
          .supplyAsync(() -> totaraTeamDAO.findTeamMembersPrograms(userIdTeamMembers.keySet()));

      Map<String, List<TotaraProgramDTO>> programMap =
          programList.get().stream().collect(Collectors.groupingBy(TotaraProgramDTO::getProgramName));

      final List<TeamMemberProgressOverviewDTO> retList = new ArrayList<>();

      for (List<TotaraProgramDTO> programs : programMap.values()) {
        TotaraProgramDTO tp = programs.get(0);
        final Map<Long, TeamMemberCompletionDTO> userCompletionMap = new HashMap<>();
        programs.forEach(program -> {
          TeamMemberCourseStatsDTO statsDTO = TeamMemberCourseStatsDTO.builder()
              .courseName(program.getCourseFullName()).percentComplete(BigDecimal.ZERO).build();

          if (userCompletionMap.containsKey(program.getUserId())) {
            userCompletionMap.get(program.getUserId()).getCourseStats().add(statsDTO);
          } else {
            TeamMemberCompletionDTO completionDTO = TeamMemberCompletionDTO.builder()
                .teamMember(userIdTeamMembers.get(program.getUserId()))
                .courseStats(new ArrayList<>(Arrays.asList((statsDTO)))).build();
            completionDTO.setDueDateFromProgram(program);
            completionDTO.setTimeEnrolledFromProgram(program);

            if (NumberUtils.toInt(program.getProgramStatus()) == 1) {
              completionDTO.setPercentComplete(new BigDecimal(100));
              completionDTO.setCompletedDate(new DateTime(program.getCompletedDate() * 1000));
            } else {
              final CompletableFuture<LearningPathProgressionOverviewDTO> statisticFuture =
                  CompletableFuture.supplyAsync(() -> programStatisticsService
                      .getProgramStatistics(program.getProgramId(), program.getUserId()));
              completionDTO.setStatisticFuture(statisticFuture);
            }
            userCompletionMap.put(program.getUserId(), completionDTO);
          }
        });

        TeamMemberProgressOverviewDTO overview = TeamMemberProgressOverviewDTO.builder()
            .program(new ProgramItemDTO(tp)).percentComplete(BigDecimal.ZERO)
            .teamMemberCompletionList(new ArrayList<>(userCompletionMap.values())).build();
        overview.getProgram().setTitle(StringUtils.trimToEmpty(overview.getProgram().getTitle()));
        retList.add(overview);
      }

      return retList.stream().peek(e -> {
        e.getTeamMemberCompletionList().stream().filter(t -> t.getStatisticFuture() != null)
            .forEach(TeamMemberCompletionDTO::resolveFutures);
        e.calculatePercent();
      }).sorted(Comparator.comparing(o -> o.getProgram().getTitle())).collect(Collectors.toList());
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<CourseSetProgressionOverviewDTO> findSharedPersonalProgramsCompletionByUser(
      Long userId, Long programId, Long teamMemberId) {

    PersonalLearningPathDTO personalProgram =
        personalPlanService.getPersonalPlanForUser(teamMemberId, programId);

    final List<CourseSetProgressionOverviewDTO> courseSetProgressions = new ArrayList<>();

    personalProgram.getCourseSets().forEach(cs -> {
      courseSetProgressions
          .add(
              CourseSetProgressionOverviewDTO
                  .builder().courseSetName(
                      cs.getName())
                  .progressionOverviews(cs.getCourses() != null ? cs.getCourses().stream()
                      .map(c -> CourseProgressionOverviewDTO.builder()
                          .courseName(
                              c.getCourseName() != null ? c.getCourseName() : c.getFullName())
                          .type(ProgramType.valueOf(c.getType()))
                          .percentComplete(c.getStatus().getPercentage(c.isManualCompletable()))
                          .build())
                      .collect(Collectors.toList()) : null)
                  .build());
    });

    return courseSetProgressions;
  }

  @Override
  public List<TeamMemberProgressOverviewDTO> findSharedPersonalProgramsCompletionsByUser(
      Long userId, List<TeamMemberDTO> teamMembers) {
    Map<Long, TeamMemberDTO> userIdTeamMembers =
        teamMembers.stream().collect(Collectors.toMap(TeamMemberDTO::getUserId, t -> t));
    List<PersonalProgramDTO> personalProgramDTOS =
        personalPlanService.getPersonalSharedPlansForUser(userId);
    List<PersonalPlanShare> allShares = personalPlanShareService.findSharesWithDirectReports(
        personalProgramDTOS.stream().map(PersonalProgramDTO::getProgramId).collect(Collectors.toList()));

    Map<Long, List<PersonalPlanShare>> sharesByProgramIdMap =
        allShares.stream().collect(Collectors.groupingBy(p -> p.getPk().getPersonalPlanId()));

    final List<TeamMemberProgressOverviewDTO> retList = new ArrayList<>();

    for (PersonalProgramDTO program : personalProgramDTOS) {

      List<PersonalPlanShare> programShares = sharesByProgramIdMap.get(program.getProgramId());

      TeamMemberCourseStatsDTO statsDTO = TeamMemberCourseStatsDTO.builder()
          .courseName(program.getCourseFullName()).percentComplete(BigDecimal.ZERO).build();

      final Map<Long, TeamMemberCompletionDTO> userCompletionMap = new HashMap<>();
      programShares.forEach(programShare -> {

        if (userCompletionMap.containsKey(program.getUserId())) {
          userCompletionMap.get(program.getUserId()).getCourseStats().add(statsDTO);
        } else {
          TeamMemberCompletionDTO completionDTO = TeamMemberCompletionDTO.builder()
              .teamMember(userIdTeamMembers.get(programShare.getPk().getSharedUserId()))
              .courseStats(new ArrayList<>(Arrays.asList((statsDTO)))).build();
          completionDTO.setDueDateFromProgram(program);
          completionDTO.setTimeEnrolledFromSharedProgram(programShare);

          if (NumberUtils.toInt(program.getProgramStatus()) == 1) {
            completionDTO.setPercentComplete(new BigDecimal(100));
            completionDTO.setCompletedDate(new DateTime(program.getCompletedDate() * 1000));
          } else {
            final CompletableFuture<LearningPathProgressionOverviewDTO> statisticFuture =
                CompletableFuture.supplyAsync(() -> personalPlanService.getProgramStatistics(
                    program.getProgramId(), programShare.getPk().getSharedUserId()));
            completionDTO.setStatisticFuture(statisticFuture);
          }
          userCompletionMap.put(programShare.getPk().getSharedUserId(), completionDTO);
        }
      });

      TeamMemberProgressOverviewDTO overview = TeamMemberProgressOverviewDTO.builder()
          .program(new ProgramItemDTO(program)).percentComplete(BigDecimal.ZERO)
          .teamMemberCompletionList(new ArrayList<>(userCompletionMap.values())).build();
      overview.getProgram().setShared(true);
      overview.getProgram().setTitle(StringUtils.trimToEmpty(overview.getProgram().getTitle()));
      retList.add(overview);
    }

    return retList.stream().peek(e -> {
      e.getTeamMemberCompletionList().stream().filter(t -> t.getStatisticFuture() != null)
          .forEach(TeamMemberCompletionDTO::resolveFutures);
      e.calculatePercent();
    }).sorted(Comparator.comparing(o -> o.getProgram().getTitle())).collect(Collectors.toList());
  }

  @Override
  public List<TeamMemberProgressOverviewDTO> findCourseCompletionsByUser(
      List<TeamMemberDTO> teamMembers) {
    Map<Long, TeamMemberDTO> userIdTeamMembers =
        teamMembers.stream().collect(Collectors.toMap(TeamMemberDTO::getUserId, t -> t));

    try {

      final CompletableFuture<List<TotaraTeamCourseDTO>> courseList = CompletableFuture
          .supplyAsync(() -> totaraTeamDAO.findTeamMembersCourses(userIdTeamMembers.keySet()));

      Map<String, List<TotaraTeamCourseDTO>> courseMap =
          courseList.get().stream().collect(Collectors.groupingBy(TotaraTeamCourseDTO::getShortName));


      final List<TeamMemberProgressOverviewDTO> retList = new ArrayList<>();

      for (List<TotaraTeamCourseDTO> courses : courseMap.values()) {
        TotaraTeamCourseDTO tp = courses.get(0);

        final Map<Long, TeamMemberCompletionDTO> userCompletionMap = new HashMap<>();
        for (TotaraTeamCourseDTO course : courses) {
          TeamMemberCourseStatsDTO statsDTO = TeamMemberCourseStatsDTO.builder()
              .courseName(course.getFullName()).percentComplete(BigDecimal.ZERO).build();

          if (userCompletionMap.containsKey(course.getUserId())) {
            userCompletionMap.get(course.getUserId()).getCourseStats().add(statsDTO);
          } else {
            TeamMemberCompletionDTO completionDTO = TeamMemberCompletionDTO.builder()
                .teamMember(userIdTeamMembers.get(course.getUserId()))
                .courseStats(new ArrayList<>(Arrays.asList((statsDTO)))).dueDate(null).build();
            completionDTO.setTimeEnrolledFromCourse(course);
            completionDTO.calculatePercentageFromCourse(course);

            userCompletionMap.put(course.getUserId(), completionDTO);
          }
        }

        TeamMemberProgressOverviewDTO overview = TeamMemberProgressOverviewDTO.builder()
            .program(new ProgramItemDTO(tp)).percentComplete(BigDecimal.ZERO)
            .teamMemberCompletionList(new ArrayList<>(userCompletionMap.values())).build();
        overview.getProgram().setTitle(StringUtils.trimToEmpty(overview.getProgram().getTitle()));
        retList.add(overview);
      }

      return retList.stream().peek(e -> {
        e.getTeamMemberCompletionList().forEach(TeamMemberCompletionDTO::resolveFutures);
        e.calculatePercent();
      }).sorted(Comparator.comparing(o -> o.getProgram().getTitle())).collect(Collectors.toList());
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }
}
