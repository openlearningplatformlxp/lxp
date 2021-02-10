package com.redhat.uxl.services.service.bo;

import com.redhat.uxl.commonjava.utils.ListUtils;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.services.service.TeamService;
import com.redhat.uxl.services.service.dto.CourseSetProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.TeamMemberCompletionDTO;
import com.redhat.uxl.services.service.dto.TeamMemberDTO;
import com.redhat.uxl.services.service.dto.TeamMemberProgressOverviewDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * The type Base csv team progress strategy bo.
 */
public abstract class BaseCsvTeamProgressStrategyBO extends BaseCsvStrategyBO {

    /**
     * The Team service.
     */
    protected final TeamService teamService;

    /**
     * Instantiates a new Base csv team progress strategy bo.
     *
     * @param currentPerson the current person
     * @param teamService   the team service
     */
    public BaseCsvTeamProgressStrategyBO(TotaraUserDTO currentPerson, TeamService teamService) {
        super(currentPerson);
        this.teamService = teamService;
    }

    @Override
    protected List<List<String>> getRowData() {
        List<List<String>> rowData = new ArrayList<>();
        List<TeamMemberDTO> teamMembers = teamService.findTeamMembersByManager(currentPerson.getId());
        if (!teamMembers.isEmpty()) {

            List<TeamMemberProgressOverviewDTO> items = findCompletionsOfType(teamMembers);

            if (StringUtils.isNotEmpty(search)) {
                items = items.stream().filter(po -> StringUtils.containsIgnoreCase(po.getProgram().getTitle(), search))
                        .collect(Collectors.toList());
            }
            if (startDate != null) {
                items.forEach(i -> {
                    i.setTeamMemberCompletionList(i.getTeamMemberCompletionList().stream()
                            .filter(tm -> tm.getCompletedDate() != null)
                            .filter(tm -> startDate.isBefore(tm.getCompletedDate())).collect(Collectors.toList()));
                });
            }
            if (endDate != null) {
                items.forEach(i -> {
                    i.setTeamMemberCompletionList(
                            i.getTeamMemberCompletionList().stream().filter(tm -> tm.getCompletedDate() != null)
                                    .filter(tm -> endDate.isAfter(tm.getCompletedDate())).collect(Collectors.toList()));
                });
            }
            final Map<Long, Map<Long, CompletableFuture<List<CourseSetProgressionOverviewDTO>>>> futureCourseProgressions = new TreeMap<>();
            if (includeCourseInfo()) {
                List<List<TeamMemberProgressOverviewDTO>> itemsLists = ListUtils.split(items, 10);
                itemsLists.forEach(i -> {
                    i.forEach(po -> {

                        Map<Long, CompletableFuture<List<CourseSetProgressionOverviewDTO>>> teamMemberProgressions = new TreeMap<>();
                        for (TeamMemberCompletionDTO completion : po.getTeamMemberCompletionList()) {
                            teamMemberProgressions.put(completion.getTeamMember().getUserId(),
                                    CompletableFuture.supplyAsync(() -> findCourseCompletions(po.getProgram().getId(),
                                            completion.getTeamMember().getUserId())));
                        }
                        futureCourseProgressions.put(po.getProgram().getId(), teamMemberProgressions);
                    });
                    // For each list of parallel I wait for all to finish to get some free space
                    i.forEach(po -> {
                        futureCourseProgressions.get(po.getProgram().getId()).values().forEach(f -> {
                            try {
                                f.get();
                            } catch (InterruptedException | ExecutionException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    });
                });
            }
            items.forEach(po -> {
                List<String> row = new ArrayList<>();
                if (!po.getTeamMemberCompletionList().isEmpty()) {
                    row.add("\"" + po.getProgram().getTitle() + "\"");
                    row.add("");
                    row.add("");
                    if (includeCourseInfo()) {
                        row.add("");
                        row.add("");
                    }
                    row.add(po.getPercentComplete() + "%");
                    rowData.add(row);
                    for (TeamMemberCompletionDTO completionDTO : po.getTeamMemberCompletionList()) {
                        List<String> innerRow = new ArrayList<>();
                        innerRow.add("");
                        innerRow.add(completionDTO.getTeamMember().getFirstName());
                        innerRow.add(completionDTO.getTeamMember().getLastName());
                        if (includeCourseInfo()) {
                            innerRow.add("");
                            innerRow.add("");
                        }
                        innerRow.add(completionDTO.getPercentComplete() + "%");
                        rowData.add(innerRow);
                        if (includeCourseInfo()) {
                            try {
                                List<CourseSetProgressionOverviewDTO> courseProgressions = futureCourseProgressions
                                        .get(po.getProgram().getId()).get(completionDTO.getTeamMember().getUserId())
                                        .get();
                                courseProgressions.forEach(cp -> {
                                    List<String> setProgressionRow = new ArrayList<>();
                                    setProgressionRow.add("");
                                    setProgressionRow.add("");
                                    setProgressionRow.add("");
                                    setProgressionRow.add(cp.getCourseSetName());
                                    setProgressionRow.add("");
                                    setProgressionRow.add("");
                                    rowData.add(setProgressionRow);
                                    cp.getProgressionOverviews().forEach(c -> {
                                        List<String> innerProgressionRow = new ArrayList<>();
                                        innerProgressionRow.add("");
                                        innerProgressionRow.add("");
                                        innerProgressionRow.add("");
                                        innerProgressionRow.add("");
                                        innerProgressionRow.add(c.getCourseName());
                                        innerProgressionRow.add(c.getPercentComplete() + "%");
                                        rowData.add(innerProgressionRow);
                                    });
                                });
                            } catch (InterruptedException | ExecutionException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                } else {
                    rowData.add(row);
                }
            });

        }
        return rowData;
    }

    /**
     * Include course info boolean.
     *
     * @return the boolean
     */
    protected boolean includeCourseInfo() {
        return false;
    }

    /**
     * Find completions of type list.
     *
     * @param teamMembers the team members
     * @return the list
     */
    protected abstract List<TeamMemberProgressOverviewDTO> findCompletionsOfType(List<TeamMemberDTO> teamMembers);

    /**
     * Find course completions list.
     *
     * @param programId    the program id
     * @param teamMemberId the team member id
     * @return the list
     */
    protected List<CourseSetProgressionOverviewDTO> findCourseCompletions(Long programId, Long teamMemberId) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getHeaderName() {
        return "Teams Progress";
    }
}
