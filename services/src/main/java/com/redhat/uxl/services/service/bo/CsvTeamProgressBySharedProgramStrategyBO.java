package com.redhat.uxl.services.service.bo;

import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.services.service.TeamService;
import com.redhat.uxl.services.service.dto.CourseSetProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.TeamMemberDTO;
import com.redhat.uxl.services.service.dto.TeamMemberProgressOverviewDTO;
import java.util.Arrays;
import java.util.List;

/**
 * The type Csv team progress by shared program strategy bo.
 */
public class CsvTeamProgressBySharedProgramStrategyBO extends BaseCsvTeamProgressStrategyBO {

    /**
     * Instantiates a new Csv team progress by shared program strategy bo.
     *
     * @param currentPerson the current person
     * @param teamService   the team service
     */
    public CsvTeamProgressBySharedProgramStrategyBO(TotaraUserDTO currentPerson, TeamService teamService) {
        super(currentPerson, teamService);
    }

    @Override
    protected List<TeamMemberProgressOverviewDTO> findCompletionsOfType(List<TeamMemberDTO> teamMembers) {
        List<TeamMemberProgressOverviewDTO> items = teamService
                .findSharedPersonalProgramsCompletionsByUser(currentPerson.getId(), teamMembers);
        return items;
    }

    @Override
    protected boolean includeCourseInfo() {
        return true;
    }

    @Override
    protected List<CourseSetProgressionOverviewDTO> findCourseCompletions(Long programId, Long teamMemberId) {
        return teamService.findSharedPersonalProgramsCompletionByUser(currentPerson.getId(), programId, teamMemberId);
    }

    @Override
    protected List<String> getColumnTitles() {
        return Arrays.asList("Program Title", "First Name", "Last Name", "Course Set", "Course Name",
                "Percent Complete");
    }

    @Override
    protected String getFileName() {
        return "team_process_by_shared_programs_" + currentPerson.getFirstName() + "_" + currentPerson.getLastName();
    }

}
