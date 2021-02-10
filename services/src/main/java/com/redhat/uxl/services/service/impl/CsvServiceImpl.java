package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.datalayer.dao.TotaraUserDAO;
import com.redhat.uxl.datalayer.repository.PersonRepository;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.services.service.CsvService;
import com.redhat.uxl.services.service.FeedbackService;
import com.redhat.uxl.services.service.PersonSearchService;
import com.redhat.uxl.services.service.ProgramItemService;
import com.redhat.uxl.services.service.TeamService;
import com.redhat.uxl.services.service.bo.CsvAllUsersStrategyBO;
import com.redhat.uxl.services.service.bo.CsvAuditSearchesStrategyBO;
import com.redhat.uxl.services.service.bo.CsvFeedbackStrategyBO;
import com.redhat.uxl.services.service.bo.CsvTeamProgressByCourseStrategyBO;
import com.redhat.uxl.services.service.bo.CsvTeamProgressByLearningPathStrategyBO;
import com.redhat.uxl.services.service.bo.CsvTeamProgressByProgramStrategyBO;
import com.redhat.uxl.services.service.bo.CsvTeamProgressBySharedProgramStrategyBO;
import com.redhat.uxl.services.service.dto.TeamMemberCompletionDTO;
import com.redhat.uxl.services.service.dto.TeamMemberDTO;
import com.redhat.uxl.services.service.dto.TeamMemberProgressOverviewDTO;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Csv service.
 */
@Service
public class CsvServiceImpl implements CsvService {
    /**
     * The Team service.
     */
    @Inject
    TeamService teamService;

    /**
     * The Totara user dao.
     */
    @Inject
    TotaraUserDAO totaraUserDAO;
    /**
     * The Program item service.
     */
    @Inject
    ProgramItemService programItemService;
    /**
     * The Feedback service.
     */
    @Inject
    FeedbackService feedbackService;
    /**
     * The Person search service.
     */
    @Inject
    PersonSearchService personSearchService;
    @Inject
    private PersonRepository personRepository;


    /**
     * The type Program title completion name pair.
     */
    class ProgramTitleCompletionNamePair {
        private String programTitle;
        private BigDecimal percentComplete;
        private String firstName;
        private String lastName;

        /**
         * Instantiates a new Program title completion name pair.
         *
         * @param programTitle    the program title
         * @param percentComplete the percent complete
         * @param firstName       the first name
         * @param lastName        the last name
         */
        public ProgramTitleCompletionNamePair(String programTitle, BigDecimal percentComplete, String firstName,
                String lastName) {
            this.programTitle = programTitle;
            this.percentComplete = percentComplete;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    @Override
    public void getTeamProgressCSVByProgram(Long userId, HttpServletRequest request, HttpServletResponse response) {
        TotaraUserDTO currentPerson = totaraUserDAO.findUserById(userId);
        CsvTeamProgressByProgramStrategyBO strategy = new CsvTeamProgressByProgramStrategyBO(currentPerson,
                teamService);
        strategy.buildCsv(response);
    }

    @Override
    public void getTeamProgressCSVBySharedPrograms(Long userId, HttpServletResponse response, String search,
            DateTime startDate, DateTime endDate) throws ServletException, IOException {
        TotaraUserDTO currentPerson = totaraUserDAO.findUserById(userId);
        CsvTeamProgressBySharedProgramStrategyBO strategy = new CsvTeamProgressBySharedProgramStrategyBO(currentPerson,
                teamService);
        strategy.setSearch(search);
        strategy.setStartDate(startDate);
        strategy.setEndDate(endDate);
        strategy.buildCsv(response);
    }

    @Override
    public void getTeamProgressCSVByTeamMember(Long userId, HttpServletRequest request, HttpServletResponse response,
            String search, DateTime startDate, DateTime endDate) throws ServletException, IOException {
        DateTimeFormatter fileNameDateFormatter = DateTimeFormat.forPattern("yyyyMMddhhmma");

        TotaraUserDTO currentPerson = totaraUserDAO.findUserById(userId);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"team_process_by_team_members_" + currentPerson.getFirstName() + "_"
                        + currentPerson.getLastName() + "_" + DateTime.now().toString(fileNameDateFormatter)
                        + ".csv\"");
        try {
            OutputStream outputStream = response.getOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            // get header
            DateTimeFormatter dateStringFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
            String header = ",Team Progress" + "\n" + ",," + DateTime.now().toString(dateStringFormatter) + "\n";
            outputStream.write(header.getBytes());

            StringBuilder sb = new StringBuilder();

            // columns title
            sb.append(",");
            sb.append("First Name");
            sb.append(",");
            sb.append("Last Name");
            sb.append(",");
            sb.append("Course or Program Title");
            sb.append(",");
            sb.append("Percent Complete");

            sb.append("\n");

            // get team members
            List<TeamMemberDTO> teamMembers = teamService.findTeamMembersByManager(userId);

            if (StringUtils.isNotEmpty(search)) {
                teamMembers = teamMembers.stream()
                        .filter(tm -> StringUtils.containsIgnoreCase(tm.getFirstName(), search)
                                || StringUtils.containsIgnoreCase(tm.getLastName(), search))
                        .collect(Collectors.toList());
            }

            if (!teamMembers.isEmpty()) {

                List<TeamMemberProgressOverviewDTO> progressOverviewDTOs = teamService
                        .findCompletionsByUser(teamMembers);

                if (startDate != null) {
                    progressOverviewDTOs.forEach(i -> {
                        i.setTeamMemberCompletionList(i.getTeamMemberCompletionList().stream()
                                .filter(tm -> tm.getCompletedDate() != null)
                                .filter(tm -> startDate.isBefore(tm.getCompletedDate())).collect(Collectors.toList()));
                    });
                }
                if (endDate != null) {
                    progressOverviewDTOs.forEach(i -> {
                        i.setTeamMemberCompletionList(i.getTeamMemberCompletionList().stream()
                                .filter(tm -> tm.getCompletedDate() != null)
                                .filter(tm -> endDate.isAfter(tm.getCompletedDate())).collect(Collectors.toList()));
                    });
                }

                if (!progressOverviewDTOs.isEmpty()) {
                    // store the progress of the teams under you
                    Map<Long, List<ProgramTitleCompletionNamePair>> managersProgramCompletionMap = new HashMap<>();

                    // store the progress of your direct reports
                    Map<Long, List<ProgramTitleCompletionNamePair>> directReportsProgramCompletionMap = new HashMap<>();

                    for (TeamMemberProgressOverviewDTO progressOverviewDTO : progressOverviewDTOs) {
                        if (!progressOverviewDTO.getTeamMemberCompletionList().isEmpty()) {
                            String currentProgramTitle = progressOverviewDTO.getProgram().getTitle();

                            for (TeamMemberCompletionDTO teamMemberCompletionDTO : progressOverviewDTO
                                    .getTeamMemberCompletionList()) {

                                List<ProgramTitleCompletionNamePair> currentManagerProgramTitleCompletionPairList = new ArrayList<>();
                                List<ProgramTitleCompletionNamePair> currentDirectReportProgramTitleCompletionPairList = new ArrayList<>();

                                TeamMemberDTO currentTeamMemberDTO = teamMemberCompletionDTO.getTeamMember();

                                // check this team member is a manager
                                if (currentTeamMemberDTO.isManager()) {
                                    if (managersProgramCompletionMap.containsKey(currentTeamMemberDTO.getUserId())) {
                                        currentManagerProgramTitleCompletionPairList = managersProgramCompletionMap
                                                .get(currentTeamMemberDTO.getUserId());
                                    }
                                    currentManagerProgramTitleCompletionPairList.add(new ProgramTitleCompletionNamePair(
                                            currentProgramTitle, teamMemberCompletionDTO.getPercentComplete(),
                                            currentTeamMemberDTO.getFirstName(), currentTeamMemberDTO.getLastName()));
                                    managersProgramCompletionMap.put(currentTeamMemberDTO.getUserId(),
                                            currentManagerProgramTitleCompletionPairList);
                                } else {
                                    if (directReportsProgramCompletionMap
                                            .containsKey(currentTeamMemberDTO.getUserId())) {
                                        currentDirectReportProgramTitleCompletionPairList = directReportsProgramCompletionMap
                                                .get(currentTeamMemberDTO.getUserId());
                                    }
                                    currentDirectReportProgramTitleCompletionPairList
                                            .add(new ProgramTitleCompletionNamePair(currentProgramTitle,
                                                    teamMemberCompletionDTO.getPercentComplete(),
                                                    currentTeamMemberDTO.getFirstName(),
                                                    currentTeamMemberDTO.getLastName()));
                                    directReportsProgramCompletionMap.put(currentTeamMemberDTO.getUserId(),
                                            currentDirectReportProgramTitleCompletionPairList);
                                }
                            }
                        }
                    }

                    // value input
                    if (!managersProgramCompletionMap.isEmpty()) {
                        sb.append("Progress of Teams");
                        sb.append(",,,,");
                        sb.append("\n");

                        setNameAndProgramTitleAndPercentCompletionColumns(managersProgramCompletionMap, sb);
                    }

                    if (!directReportsProgramCompletionMap.isEmpty()) {
                        sb.append("Progress of Direct Reports");
                        sb.append(",,,,");
                        sb.append("\n");

                        setNameAndProgramTitleAndPercentCompletionColumns(directReportsProgramCompletionMap, sb);
                    }
                }
            }

            pw.write(sb.toString());
            pw.close();
        } catch (Exception e) {
        }
    }

    private void setNameAndProgramTitleAndPercentCompletionColumns(Map<Long, List<ProgramTitleCompletionNamePair>> map,
            StringBuilder sb) {
        for (Map.Entry<Long, List<ProgramTitleCompletionNamePair>> entry : map.entrySet()) {
            boolean needName = true;
            for (ProgramTitleCompletionNamePair programTitleCompletionNamePair : entry.getValue()) {
                if (needName) {
                    sb.append(",");
                    sb.append(programTitleCompletionNamePair.firstName);
                    sb.append(",");
                    sb.append(programTitleCompletionNamePair.lastName);
                    sb.append(",,");
                    sb.append("\n");

                    needName = false;
                }

                sb.append(",,,");
                sb.append("\"" + programTitleCompletionNamePair.programTitle + "\"");
                sb.append(",");
                sb.append(programTitleCompletionNamePair.percentComplete + "%");
                sb.append("\n");
            }
        }
    }

    @Override
    public void getTeamProgressCSVByCoursePrograms(Long userId, String search, DateTime startDate, DateTime endDate,
            HttpServletResponse response) throws ServletException, IOException {
        TotaraUserDTO currentPerson = totaraUserDAO.findUserById(userId);
        CsvTeamProgressByCourseStrategyBO strategy = new CsvTeamProgressByCourseStrategyBO(currentPerson, teamService);
        strategy.setSearch(search);
        strategy.setStartDate(startDate);
        strategy.setEndDate(endDate);
        strategy.buildCsv(response);
    }

    @Override
    public void getTeamProgressCSVByLearningPathPrograms(Long userId, String search, DateTime startDate,
            DateTime endDate, HttpServletResponse response) throws ServletException, IOException {
        TotaraUserDTO currentPerson = totaraUserDAO.findUserById(userId);
        CsvTeamProgressByLearningPathStrategyBO strategy = new CsvTeamProgressByLearningPathStrategyBO(currentPerson,
                teamService, programItemService);
        strategy.setSearch(search);
        strategy.setStartDate(startDate);
        strategy.setEndDate(endDate);
        strategy.buildCsv(response);
    }

    @Override
    public void getAllFeedBack(HttpServletRequest request, HttpServletResponse response) {
        TotaraUserDTO currentPerson = null;
        CsvFeedbackStrategyBO strategy = new CsvFeedbackStrategyBO(currentPerson, feedbackService);
        strategy.buildCsv(response);
    }

    @Override
    public void getAllUsers(HttpServletRequest request, HttpServletResponse response) {
        TotaraUserDTO currentPerson = null;
        CsvAllUsersStrategyBO strategy = new CsvAllUsersStrategyBO(currentPerson, personRepository);
        strategy.buildCsv(response);
    }

    @Override
    public void getAllAuditSearches(HttpServletRequest request, HttpServletResponse response) {
        TotaraUserDTO currentPerson = null;
        CsvAuditSearchesStrategyBO strategy = new CsvAuditSearchesStrategyBO(currentPerson, personSearchService);
        strategy.buildCsv(response);
    }

}
