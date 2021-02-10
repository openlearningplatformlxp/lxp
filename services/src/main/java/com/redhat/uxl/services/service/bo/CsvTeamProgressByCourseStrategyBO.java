package com.redhat.uxl.services.service.bo;

import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.services.service.TeamService;
import com.redhat.uxl.services.service.dto.TeamMemberDTO;
import com.redhat.uxl.services.service.dto.TeamMemberProgressOverviewDTO;
import java.util.Arrays;
import java.util.List;

/**
 * The type Csv team progress by course strategy bo.
 */
public class CsvTeamProgressByCourseStrategyBO extends BaseCsvTeamProgressStrategyBO {

    /**
     * Instantiates a new Csv team progress by course strategy bo.
     *
     * @param currentPerson the current person
     * @param teamService   the team service
     */
    public CsvTeamProgressByCourseStrategyBO(TotaraUserDTO currentPerson, TeamService teamService) {
        super(currentPerson, teamService);
    }

    @Override
    protected List<String> getColumnTitles() {
        return Arrays.asList("Course Title", "First Name", "Last Name", "Percent Complete");
    }

    @Override
    protected List<TeamMemberProgressOverviewDTO> findCompletionsOfType(List<TeamMemberDTO> teamMembers) {
        return teamService.findCourseCompletionsByUser(teamMembers);
    }

    @Override
    protected String getFileName() {
        return "team_process_by_course_programs_" + currentPerson.getFirstName() + "_" + currentPerson.getLastName();
    }
}
