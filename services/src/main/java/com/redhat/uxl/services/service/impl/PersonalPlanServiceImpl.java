
package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.datalayer.dao.PersonalPlansDAO;
import com.redhat.uxl.datalayer.dao.TotaraCourseDAO;
import com.redhat.uxl.datalayer.repository.PersonRepository;
import com.redhat.uxl.datalayer.repository.PersonalPlanShareRepository;
import com.redhat.uxl.datalayer.solr.repository.CourseDocumentSolrRepository;
import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.PersonalPlanShare;
import com.redhat.uxl.dataobjects.domain.dto.PersonalLearningPathDTO;
import com.redhat.uxl.dataobjects.domain.dto.PersonalProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseSetDTO;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseStatusDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.types.ContentSourceType;
import com.redhat.uxl.dataobjects.domain.types.PersonalPlanShareType;
import com.redhat.uxl.dataobjects.domain.types.ProgramCourseType;
import com.redhat.uxl.services.constants.MessageConstants;
import com.redhat.uxl.services.service.CourseService;
import com.redhat.uxl.services.service.MessageService;
import com.redhat.uxl.services.service.PersonalPlanService;
import com.redhat.uxl.services.service.PersonalPlanShareService;
import com.redhat.uxl.services.service.ProgramStatisticsService;
import com.redhat.uxl.services.service.dto.CourseProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.LearningPathProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.PersonalProgramStatIndividualDTO;
import com.redhat.uxl.services.service.dto.PersonalProgramStatsDTO;
import com.redhat.uxl.services.service.dto.TeamMemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Personal plan service.
 */
@Service
@Slf4j
@Transactional
public class PersonalPlanServiceImpl implements PersonalPlanService {
    /**
     * The Plans dao.
     */
    @Inject
    PersonalPlansDAO plansDAO;
    /**
     * The Personal plan share repository.
     */
    @Inject
    PersonalPlanShareRepository personalPlanShareRepository;
    /**
     * The Personal plan share service.
     */
    @Inject
    PersonalPlanShareService personalPlanShareService;
    /**
     * The Person repository.
     */
    @Inject
    PersonRepository personRepository;
    /**
     * The Totara course dao.
     */
    @Inject
    TotaraCourseDAO totaraCourseDAO;
    /**
     * The Course document solr repository.
     */
    @Inject
    CourseDocumentSolrRepository courseDocumentSolrRepository;
    /**
     * The Course service.
     */
    @Inject
    CourseService courseService;
    /**
     * The Message service.
     */
    @Inject
    MessageService messageService;
    /**
     * The Program statistics service.
     */
    @Inject
    ProgramStatisticsService programStatisticsService;

    @Override
    public PersonalProgramStatsDTO getPersonalPlanStats(Long currentUserId, List<TeamMemberDTO> teamMembers) {

        return PersonalProgramStatsDTO.builder().individuals(teamMembers.stream().map(tm -> {
            int plans = plansDAO.countPlansForUser(tm.getUserId());
            int shares = plans > 0
                    ? personalPlanShareRepository.findByPkOwnerUserIdAndPkSharedUserIdAndType(tm.getUserId(),
                            currentUserId, PersonalPlanShareType.MANAGER).size()
                    : 0;
            return PersonalProgramStatIndividualDTO.builder().name(tm.getDisplayName()).avatar(tm.getAvatar())
                    .plans(plans).sharedWithYou(shares).build();
        }).collect(Collectors.toList())).build();
    }

    @Override
    public List<PersonalProgramDTO> getPersonalPlansForUser(Long userId) {
        return plansDAO.getPlansForUser(userId);
    }

    @Override
    public List<PersonalProgramDTO> getArchivedPersonalPlansForUser(Long userId) {
        return plansDAO.getArchivedPlansForUser(userId);
    }

    @Override
    public List<PersonalProgramDTO> getPersonalSharedPlansForUser(Long userId) {
        return plansDAO.getPersonalSharedPlansForUser(userId);
    }

    @Override
    public Long insertPersonalPlan(Long userId, PersonalLearningPathDTO path) {
        return plansDAO.insertPlanForUser(userId, path);
    }

    @Override
    public void updatePersonalPlan(Long userId, PersonalLearningPathDTO path) {
        plansDAO.updatePlanForUser(userId, path);
        if (path.isShared() && path.getDueDateUpdated() != null) {
            // send notification to each user.
            Person owner = personRepository.findOneByLoginIgnoreCaseAndDeletedIsFalse(String.valueOf(userId));
            List<PersonalPlanShare> shares = personalPlanShareService.findSharesWithDirectReports(path.getId());
            shares.forEach(share -> {
                String title = String.format(MessageConstants.S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_CHANGED_TITLE,
                        owner.getDisplayName());
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                String messageText = String.format(MessageConstants.S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_CHANGED,
                        path.getTitle(), formatter.format(path.getDueDateUpdated()));
                messageService.sendSharedPathNotificationToUser(title, messageText, share.getPk().getSharedUserId(),
                        path.getId());
            });
        }
    }

    @Override
    public void setPersonalPlanManualCompletion(Long itemId, Long status, Long userId) {
        plansDAO.setPersonalPlanManualCompletion(itemId, status, userId);
    }

    @Override
    public LearningPathProgressionOverviewDTO getProgramStatistics(Long programId, Long userId) {
        PersonalLearningPathDTO personalLearningPathDTO = getPersonalPlanForUser(userId, programId);
        return LearningPathProgressionOverviewDTO.builder().courseCount(personalLearningPathDTO.getTotalCourses())
                .programId(programId).programName(personalLearningPathDTO.getTitle())
                .totalCourseDuration(personalLearningPathDTO.getPercentComplete())
                .coursesComplete(personalLearningPathDTO.getTotalCompletedCourses())
                .percentComplete(personalLearningPathDTO.getPercentComplete())
                .build();
    }

    @Override
    public void archivePersonalProgram(Long userId, Long pathId) {
        plansDAO.archivePlanForUser(userId, pathId);
    }

    @Override
    public Long clonePersonalProgram(Long personTotaraId, Long pathId) {
        PersonalLearningPathDTO learningPath = getPersonalPlanForUser(personTotaraId, pathId);
        learningPath.setId(null);
        learningPath.setUserId(personTotaraId);
        learningPath.getCourseSets().forEach(cs -> {
            cs.setId(null);
            cs.getCourses().forEach(course -> {
                course.setId(null);
            });
        });
        // Remove ids before cloning
        return insertPersonalPlan(personTotaraId, learningPath);
    }

    @Override
    public PersonalLearningPathDTO getPersonalPlanForUser(Long userId, Long pathId) {
        PersonalLearningPathDTO plan = plansDAO.getPersonalPlanForUser(userId, pathId);
        // Find the manager that was used for share a plan
        PersonalPlanShare share = personalPlanShareRepository.findByPkPersonalPlanIdAndType(plan.getId(),
                PersonalPlanShareType.MANAGER);
        if (share != null) {
            plan.setSharedWithManager(true);
            plan.setSharedWithManagerOn(share.getCreatedDate());
        }
        List<PersonalPlanShare> shares = personalPlanShareRepository.findAllByPkPersonalPlanIdAndType(plan.getId(),
                PersonalPlanShareType.DIRECT_REPORTS);
        plan.setShares(shares);
        calculateProgressForPlan(userId, plan);
        final List<ProgramCourseDTO> courses = new ArrayList<>();
        if (!plan.getCourseSets().isEmpty()) {
            plan.getCourseSets().stream().filter(cs -> cs.getCourses() != null)
                    .forEach(cs -> courses.addAll(cs.getCourses()));
            List<Long> courseIds = courses.stream().filter(c -> ContentSourceType.LMS.equals(c.getCms()))
                    .filter(c -> ProgramCourseType.COURSE.equals(c.getType())).map(ProgramCourseDTO::getId)
                    .collect(Collectors.toList());
            List<Long> programIds = courses.stream().filter(c -> ContentSourceType.LMS.equals(c.getCms()))
                    .filter(c -> ProgramCourseType.PROGRAM_LINK.equals(c.getType())).map(ProgramCourseDTO::getId)
                    .collect(Collectors.toList());
            List<String> externalIds = courses.stream().filter(c -> !ContentSourceType.LMS.equals(c.getCms()))
                    .map(ProgramCourseDTO::getExternalId).collect(Collectors.toList());
            // Find totara programs
            if (!programIds.isEmpty()) {
                Page<TotaraProgramDTO> totaraPrograms = totaraCourseDAO.findActiveProgramsById(programIds, PageRequest.of(0,10));
                courses.stream().forEach(c -> {
                    totaraPrograms.getContent().stream().forEach(tc -> {
                        if (tc.getProgramId().equals(c.getId())) {
                            c.setFullName(tc.getProgramName());
                            c.setDescription(tc.getProgramSummary());
                        }
                    });
                });
            }
            // Find totara courses
            if (!courseIds.isEmpty()) {
                List<TotaraCourseDTO> totaraCoursDTOS = totaraCourseDAO.findCoursesById(courseIds);
                courses.stream().forEach(c -> {
                    totaraCoursDTOS.stream().forEach(tc -> {
                        if (tc.getId().equals(c.getId())) {
                            c.setFullName(tc.getFullName());
                            c.setDescription(tc.getSummary());
                        }
                    });
                });
            }
            // Find documents
            if (!externalIds.isEmpty()) {
                Iterable<CourseDocument> externalCourses = courseDocumentSolrRepository.findAll(externalIds);
                courses.stream().forEach(c -> {
                    for (CourseDocument ec : externalCourses) {
                        if (ec.getProgramId().equals(c.getId())) {
                            c.setFullName(ec.getFullName());
                            c.setDescription(ec.getDescription());
                        }
                    }
                    ;
                });
            }
        }

        return plan;

    }

    /**
     * Calculate progress for plan.
     *
     * @param userId the user id
     * @param plan   the plan
     */
    public void calculateProgressForPlan(Long userId, PersonalLearningPathDTO plan) {
        BigDecimal totalNumOfCourses = BigDecimal.ZERO;
        BigDecimal completedCourses = BigDecimal.ZERO;

        if (plan.getCourseSets() != null) {
            List<ProgramCourseSetDTO> courseSet = plan.getCourseSets();
            if (courseSet != null) {
                for (ProgramCourseSetDTO course : courseSet) {
                    if (course.getCourses() != null) {
                        for (ProgramCourseDTO specificCourse : course.getCourses()) {
                            setItemProgress(plan.getId(), specificCourse, userId);

                            totalNumOfCourses = totalNumOfCourses.add(BigDecimal.ONE);
                            if (specificCourse.getStatus() != null && specificCourse.getStatus().isCompleted()) {
                                completedCourses = completedCourses.add(BigDecimal.ONE);
                            }
                        }
                    }
                }
            }

        }
        plan.setTotalCompletedCourses(completedCourses);
        plan.setTotalCourses(totalNumOfCourses);

        if (totalNumOfCourses.compareTo(BigDecimal.ZERO) > 0 && completedCourses.compareTo(BigDecimal.ZERO) > 0) {
            plan.setPercentComplete(completedCourses.divide(totalNumOfCourses, 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100)));
        } else {
            plan.setPercentComplete(BigDecimal.ZERO);
        }

    }

    /**
     * Sets item progress.
     *
     * @param programId the program id
     * @param item      the item
     * @param userId    the user id
     */
    public void setItemProgress(Long programId, ProgramCourseDTO item, Long userId) {
        Validate.notNull(item);
        switch (item.getType()) {
        case COURSE:
        case PROGRAM_LINK:
        case OFFLINE_TASK:
        case WIKIPAGE:
            Long completeStatus = 0l;
            if (ContentSourceType.LMS.equals(item.getCms()) && !ProgramCourseType.WIKIPAGE.equals(item.getType())) {
                if (ProgramCourseType.PROGRAM_LINK.equals(item.getType())) {
                    LearningPathProgressionOverviewDTO overviewDTO = programStatisticsService
                            .getProgramStatistics(item.getId(), userId);
                    completeStatus = overviewDTO.getPercentComplete().longValue();
                } else if (ProgramCourseType.COURSE.equals(item.getType())) {
                    CourseProgressionOverviewDTO courseProgress = courseService
                            .getCourseProgressionOverview(item.getId(), userId);
                    completeStatus = new Long(courseProgress.getPercentComplete());
                }
            } else { // External Content - manual completion
                completeStatus = plansDAO.getPersonalPlanManualCompletion(item.getItemId(), userId);
            }
            ProgramCourseStatusDTO status = new ProgramCourseStatusDTO();
            item.setStatus(status);
            status.setEnrolled(true);
            status.setCompleted(completeStatus >= 100);
            if (item.isManualCompletable()) {
                status.setInProgress(completeStatus < 100);
            } else {
                // LMS COURSES
                status.setInProgress(completeStatus > 0 && completeStatus < 100);
            }
            break;
        }
    }

}
