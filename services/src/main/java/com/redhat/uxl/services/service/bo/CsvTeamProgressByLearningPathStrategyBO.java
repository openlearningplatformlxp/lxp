package com.redhat.uxl.services.service.bo;

import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.services.service.ProgramItemService;
import com.redhat.uxl.services.service.TeamService;
import com.redhat.uxl.services.service.dto.CourseSetProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.TeamMemberDTO;
import com.redhat.uxl.services.service.dto.TeamMemberProgressOverviewDTO;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Csv team progress by learning path strategy bo.
 */
@Slf4j
public class CsvTeamProgressByLearningPathStrategyBO extends BaseCsvTeamProgressStrategyBO {

    private final ProgramItemService programItemService;

    /**
     * Instantiates a new Csv team progress by learning path strategy bo.
     *
     * @param currentPerson      the current person
     * @param teamService        the team service
     * @param programItemService the program item service
     */
    public CsvTeamProgressByLearningPathStrategyBO(TotaraUserDTO currentPerson, TeamService teamService,
            ProgramItemService programItemService) {
        super(currentPerson, teamService);
        this.programItemService = programItemService;
    }

    @Override
    protected List<TeamMemberProgressOverviewDTO> findCompletionsOfType(List<TeamMemberDTO> teamMembers) {
        List<TeamMemberProgressOverviewDTO> items = teamService.findProgramsCompletionsByUser(teamMembers);
        return items;
    }

    @Override
    protected boolean includeCourseInfo() {
        return true; // this is taking so long
    }

    @Override
    protected List<CourseSetProgressionOverviewDTO> findCourseCompletions(Long programId, Long teamMemberId) {
        log.info("Processing program overview for csv with id: " + programId + " for member: " + teamMemberId);
        return programItemService.getProgramProgressionOverview(programId, teamMemberId);
    }

    @Override
    protected List<String> getColumnTitles() {
        return Arrays.asList("Program Title", "First Name", "Last Name", "Course Set", "Course Name",
                "Percent Complete");
    }

    @Override
    protected String getFileName() {
        return "team_process_by_programs_" + currentPerson.getFirstName() + "_" + currentPerson.getLastName();
    }

}
