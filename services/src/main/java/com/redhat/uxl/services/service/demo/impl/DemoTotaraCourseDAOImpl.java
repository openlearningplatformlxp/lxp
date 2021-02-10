package com.redhat.uxl.services.service.demo.impl;

import com.redhat.uxl.datalayer.dao.TotaraCourseDAO;
import com.redhat.uxl.datalayer.dto.CoursePlayerCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraActivityDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraFaceToFaceSessionDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraHtmlBlockDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.types.CourseDocumentVisibilityType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.demo.dto.ActiveProgramDTO;
import com.redhat.uxl.services.service.demo.dto.ActivityInstanceDTO;
import com.redhat.uxl.services.service.demo.dto.CeCreditsUserDTO;
import com.redhat.uxl.services.service.demo.dto.CourseAudienceDTO;
import com.redhat.uxl.services.service.demo.dto.CourseListDTO;
import com.redhat.uxl.services.service.demo.dto.CourseTagsDTO;
import com.redhat.uxl.services.service.demo.dto.ProgramCourseDTO;
import com.redhat.uxl.services.service.demo.dto.ProgramTagDTO;
import com.redhat.uxl.services.service.demo.dto.UserActivityDTO;
import com.redhat.uxl.services.service.demo.dto.UserAudienceDTO;
import com.redhat.uxl.services.service.demo.dto.UserCourseDTO;
import com.redhat.uxl.services.service.demo.dto.UserProgramDTO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Demo totara course dao.
 */
@Service
@Slf4j
@ConditionalOnExpression("${demo.mode.enabled:true}")
public class DemoTotaraCourseDAOImpl implements TotaraCourseDAO {

    /**
     * The constant PUBLIC_VISIBILITY_ID.
     */
    public static final Long PUBLIC_VISIBILITY_ID = 2l;

    /**
     * The Demo utility service.
     */
    @Inject DemoUtilityService demoUtilityService;

    @Override
    public List<TotaraCourseDTO> findActiveCourses() {
        List<TotaraCourseDTO> courses = new ArrayList<>();
        List<TotaraCourseDTO> allCourses = demoUtilityService.getCourses();
        List<CourseListDTO> activeCourses = demoUtilityService.getActiveCourses();
        for (CourseListDTO single : activeCourses) {
            for (Long id : single.getCourseIds()) {
                for (TotaraCourseDTO course : allCourses) {
                    if (id.equals(course.getCourseId())) {
                        courses.add(course);
                    }
                }
            }
        }
        return courses;
    }

    @Override
    public CoursePlayerCourseDTO getEnrolledCourse(Long personTotaraId, Long courseTotaraId) {
        return null;
    }

    @Override
    public List<TotaraCourseDTO> findCoursesById(List<Long> courseIdList) {
        List<TotaraCourseDTO> courses = new ArrayList<>();
        List<TotaraCourseDTO> allCourses = demoUtilityService.getCourses();
        for (Long id : courseIdList) {
            for (TotaraCourseDTO course : allCourses) {
                if (course.getCourseId().equals(id)) {
                    courses.add(course);
                }
            }
        }
        return courses;
    }

    @Override
    public CourseDocumentVisibilityType visibilityTypeForCourse(Long courseId) {
        return visibilityTypeForProgram(courseId, ProgramType.COURSE);
    }

    @Override
    public CourseDocumentVisibilityType visibilityTypeForLearningPath(Long programId) {
        return visibilityTypeForProgram(programId, ProgramType.LEARNING_PATH);
    }

    /**
     * Visibility type for program course document visibility type.
     *
     * @param programId the program id
     * @param type      the type
     * @return the course document visibility type
     */
    protected CourseDocumentVisibilityType visibilityTypeForProgram(Long programId, ProgramType type) {
        return CourseDocumentVisibilityType.PUBLIC;
    }

    @Override
    public Long enrolledInForProgram(Long userId, Long programId) {
        Long assignmentId = null;
        List<UserProgramDTO> programUserList = demoUtilityService.getUserPrograms();
        for (UserProgramDTO dto : programUserList) {
            if (programId.equals(dto.getProgramId()) && userId.equals(dto.getUserId())) {
                assignmentId = dto.getProgramId();
            }
        }
        return assignmentId;
    }

    @Override
    public List<Long> findAllowedUserAudienceForCourse(Long courseId) {
        return findAllowedUserAudience(courseId, ProgramType.COURSE);
    }

    @Override
    public List<Long> findAllowedUserAudienceForProgram(Long programId) {
        return findAllowedUserAudience(programId, ProgramType.LEARNING_PATH);
    }

    /**
     * Find allowed user audience list.
     *
     * @param programId   the program id
     * @param programType the program type
     * @return the list
     */
    protected List<Long> findAllowedUserAudience(Long programId, ProgramType programType) {
        List<UserProgramDTO> programs = demoUtilityService.getUserPrograms();
        List<Long> ids = new ArrayList<>();
        for (UserProgramDTO map : programs) {
            if (map.getProgramId().equals(programId)) {
                ids.add(map.getUserId());
            }
        }
        return ids;
    }

    @Override
    public Long findAllowedUserAudienceByUserId(Long userId, Long programId) {
        List<UserAudienceDTO> audiences = demoUtilityService.getUserAudiences();
        Long audienceId = null;
        for (UserAudienceDTO aud : audiences) {
            if (aud.getAudienceId().equals(userId)) {
                audienceId = aud.getAudienceId();
            }
        }
        return audienceId;
    }

    @Override
    public List<TotaraProgramDTO> findActivePrograms() {
        List<TotaraProgramDTO> programs = new ArrayList<>();
        List<TotaraProgramDTO> allPrograms = demoUtilityService.getPrograms();
        List<ActiveProgramDTO> activePrograms = demoUtilityService.getActivePrograms();
        for (ActiveProgramDTO single : activePrograms) {
            for (Long id : single.getProgramIds()) {
                for (TotaraProgramDTO program : allPrograms) {
                    if (id.equals(program.getProgramId())) {
                        programs.add(program);
                    }
                }
            }
        }
        return programs;
    }

    @Override
    public Page<TotaraProgramDTO> findActivePrograms(Pageable pageable) {
        List<TotaraProgramDTO> elements = findActivePrograms();
        return new PageImpl<>(elements, pageable, elements.size());
    }

    @Override
    public Page<TotaraProgramDTO> findActiveProgramsById(List<Long> programIdList, Pageable pageable) {
        List<TotaraProgramDTO> programs = demoUtilityService.getPrograms();

        return new PageImpl<>(programs, pageable, programs.size());
    }

    @Override
    public Page<TotaraProgramDTO> findActiveProgramsByTag(Long tagId, Pageable pageable) {
        List<ProgramTagDTO> programTag = demoUtilityService.getProgramTags();
        List<TotaraProgramDTO> programs = demoUtilityService.getPrograms();
        List<Long> ids = new ArrayList<>();
        List<TotaraProgramDTO> result = new ArrayList<>();

        for (ProgramTagDTO pt : programTag) {
            if (pt.getTagId().equals(tagId)) {
                ids.add(pt.getProgramId());
            }
        }

        for (Long id : ids) {
            for (TotaraProgramDTO program : programs) {
                if (program.getProgramId().equals(id)) {
                    result.add(program);
                }
            }
        }

        return new PageImpl<>(programs, pageable, programs.size());
    }

    @Override
    public TotaraProgramDTO findProgramByCourseId(Long courseId) {
        List<ProgramCourseDTO> programCourses = demoUtilityService.getProgramCourses();
        List<TotaraProgramDTO> programs = demoUtilityService.getPrograms();
        Long id = null;
        for (ProgramCourseDTO pc : programCourses) {
            if (pc.getCourseId().equals(courseId)) {
                id = pc.getProgramId();
            }
        }

        if (id != null) {
            for (TotaraProgramDTO program : programs) {
                if (program.getProgramId().equals(id)) {
                    return program;
                }
            }
        }
        return null;
    }

    @Override
    public TotaraProgramDTO findProgramByProgramId(Long programId) {
        List<TotaraProgramDTO> programs = demoUtilityService.getPrograms();
        TotaraProgramDTO program = new TotaraProgramDTO();
        for (TotaraProgramDTO p : programs) {
            if (p.getProgramId().equals(programId)) {
                program = p;
            }
        }
        return program;
    }

    @Override
    public TotaraCourseDTO findCourseByCourseId(Long courseId) {
        List<TotaraCourseDTO> courses = demoUtilityService.getCourses();

        for (TotaraCourseDTO c : courses) {
            if (c.getCourseId().equals(courseId)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Page<TotaraCourseDTO> findActiveCourses(Pageable pageable) {
        List<TotaraCourseDTO> courses = findActiveCourses();

        return new PageImpl<>(courses, pageable, courses.size());
    }

    @Override
    public Page<TotaraCourseDTO> findActiveCoursesByTag(Long tagId, Pageable pageable) {
        List<CourseTagsDTO> courseTags = demoUtilityService.getCourseTags();
        List<Long> ids = new ArrayList<>();
        List<TotaraCourseDTO> all = demoUtilityService.getCourses();
        List<TotaraCourseDTO> result = new ArrayList<>();
        for (CourseTagsDTO ct : courseTags) {
            if (ct.getTagId().equals(tagId)) {
                ids.add(ct.getCourseId());
            }
        }

        for (TotaraCourseDTO course : all) {
            for (Long id : ids) {
                if (course.getCourseId().equals(id)) {
                    result.add(course);
                }
            }
        }
        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    public Page<TotaraCourseDTO> findActiveCoursesByAudience(Long audienceId, Pageable pageable) {
        List<TotaraCourseDTO> courses = demoUtilityService.getCourses();
        List<TotaraCourseDTO> result = new ArrayList<>();
        List<CourseAudienceDTO> list = demoUtilityService.getActiveCourseAudience();
        List<Long> courseIds = new ArrayList<>();

        for (CourseAudienceDTO ca : list) {
            if (ca.getAudienceId().equals(audienceId)) {
                courseIds.add(ca.getCourseId());
            }
        }

        for (TotaraCourseDTO c : courses) {
            for (Long id : courseIds) {
                if (c.getCourseId().equals(id)) {
                    result.add(c);
                }
            }
        }

        return new PageImpl<>(result, pageable, result.size());

    }

    @Override
    public List<TotaraProgramDTO> findByUserIdAndProgramId(Long userId, Long programId) {
        List<UserProgramDTO> map = demoUtilityService.getUserPrograms();
        List<TotaraProgramDTO> all = demoUtilityService.getPrograms();
        List<TotaraProgramDTO> result = new ArrayList<>();

        for (UserProgramDTO item : map) {
            if (item.getProgramId().equals(programId) && item.getUserId().equals(userId)) {
                for (TotaraProgramDTO p : all) {
                    if (p.getProgramId().equals(item.getProgramId())) {
                        result.add(p);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public List<TotaraProgramDTO> findProgramsByUserId(Long userId) {
        List<UserProgramDTO> map = demoUtilityService.getUserPrograms();
        List<TotaraProgramDTO> all = demoUtilityService.getPrograms();
        List<TotaraProgramDTO> result = new ArrayList<>();

        for (UserProgramDTO item : map) {
            if (item.getUserId().equals(userId)) {
                for (TotaraProgramDTO p : all) {
                    if (p.getProgramId().equals(item.getProgramId())) {
                        p.setUserId(userId);
                        p.setProgramStatus(item.getStatus());
                        result.add(p);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Page<TotaraProgramDTO> findInProgressProgramsByUserId(Long userId, Pageable pageable) {
        List<TotaraProgramDTO> programs = findProgramsByUserId(userId);
        List<TotaraProgramDTO> result = new ArrayList<>();
        for (TotaraProgramDTO program : programs) {
            if (!"COMPLETE".equals(program.getProgramStatus())) {
                result.add(program);
            }
        }
        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    public List<TotaraCourseDTO> findCoursesByUserId(Long userId) {
        List<TotaraCourseDTO> all = demoUtilityService.getCourses();
        List<UserCourseDTO> pairs = demoUtilityService.getUserCourses();
        List<TotaraCourseDTO> result = new ArrayList<>();
        List<Long> ids = new ArrayList<>();

        for (UserCourseDTO uc : pairs) {
            if (userId.equals(uc.getUserId())) {
                ids.add((uc.getCourseId()));
            }
        }

        for (TotaraCourseDTO course : all) {
            for (Long id : ids) {
                if (course.getCourseId().equals(id)) {
                    result.add(course);
                }
            }
        }

        return result;
    }

    @Override
    public int getCECreditsForUser(Long userId) {
        List<CeCreditsUserDTO> credits = demoUtilityService.getUserCeCredits();

        for (CeCreditsUserDTO credit : credits) {
            if (credit.getUserId().equals(userId)) {
                return credit.getCeCredits();
            }
        }
        return 0;
    }

    @Override
    public Page<TotaraCourseDTO> findInProgressCoursesByUserId(Long userId, Pageable pageable) {
        List<UserCourseDTO> list = demoUtilityService.getUserCourses();
        List<TotaraCourseDTO> all = demoUtilityService.getCourses();
        List<TotaraCourseDTO> result = new ArrayList<>();

        for (UserCourseDTO cl : list) {
            if (cl.getUserId().equals(userId)) {
                for (TotaraCourseDTO course : all) {
                    if (course.getCourseId().equals(cl.getCourseId())) {
                        result.add(course);
                    }
                }
            }
        }
        return new PageImpl<>(result, pageable, result.size());

    }

    @Override
    public TotaraActivityDTO findActivityByInstanceId(Long instanceId) {
        List<TotaraActivityDTO> all = demoUtilityService.getActivities();
        List<ActivityInstanceDTO> instances = demoUtilityService.getActivityInstances();
        List<Long> filtered = new ArrayList<>();

        for (ActivityInstanceDTO ins : instances) {
            if (ins.getInstanceId().equals(instanceId)) {
                filtered.add(ins.getActivityId());
            }
        }

        for (TotaraActivityDTO act : all) {
            for (Long id : filtered) {
                if (act.getId().equals(id)) {
                    return act;
                }
            }
        }
        return new TotaraActivityDTO();
    }

    @Override
    public List<TotaraActivityDTO> findActivitiesByUserId(Long userId) {
        List<UserActivityDTO> userActivities = demoUtilityService.getUserActivities();
        List<TotaraActivityDTO> activities = demoUtilityService.getActivities();
        List<TotaraActivityDTO> result = new ArrayList<>();
        for (UserActivityDTO act : userActivities) {
            if (act.getUserId().equals(userId)) {
                for (TotaraActivityDTO ac : activities) {
                    if (ac.getId().equals(act.getActivityId())) {
                        result.add(ac);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<TotaraActivityDTO> findCompletedActivitiesByUserId(Long userId, int max) {

        List<UserActivityDTO> userActivities = demoUtilityService.getUserActivities();
        List<TotaraActivityDTO> activities = demoUtilityService.getActivities();
        List<TotaraActivityDTO> result = new ArrayList<>();
        int count = 0;
        for (UserActivityDTO act : userActivities) {
            if (act.getStatus()) {
                for (TotaraActivityDTO ac : activities) {
                    if (ac.getId().equals(act.getActivityId()) && count < max) {
                        result.add(ac);
                        count++;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<TotaraActivityDTO> findActivityForUserCourses(Long userId) {
        List<TotaraCourseDTO> courses = findCoursesByUserId(userId);
        List<TotaraActivityDTO> activities = demoUtilityService.getActivities();
        List<TotaraActivityDTO> result = new ArrayList<>();

        for (TotaraCourseDTO c : courses) {
            for (TotaraActivityDTO act : activities) {
                if (act.getCourseId().equals(c.getCourseId())) {
                    result.add(act);
                }
            }
        }
        return result;
    }

    @Override
    public void inertActivityText(Long activityId, Long userId, String message) {

    }

    @Override
    public Long findActivityId(Long activityId, Long course) {
        return null;
    }

    @Override
    public void completeTotaraCourseActivityDB(Long activityId, Long userId) {

    }

    @Override
    public List<TotaraFaceToFaceSessionDTO> findFaceToFaceSessions(Long courseId) {
        return null;
    }

    @Override
    public TotaraFaceToFaceSessionDTO findFaceToFaceSessionById(Long sessionId) {
        return null;
    }

    @Override
    public TotaraHtmlBlockDTO findProgramHtmlBlockByProgramId(Long programId) {
        return null;
    }

    @Override
    public int addFaceToFaceSignup(Long sessionId, Long userId) {
        return 0;
    }

    @Override
    public int addFaceToFaceSignupStatus(Long signupId) {
        return 0;
    }

    @Override
    public int enrollInCourse(Long courseId, Long userId) {
        return 0;
    }

    @Override
    public List<TotaraProgramDTO> findProgramEnrollments(DateTime fromDateTime) {
        List<TotaraProgramDTO> all = demoUtilityService.getPrograms();
        List<UserProgramDTO> enrolledPrograms = demoUtilityService.getUserPrograms();
        List<TotaraProgramDTO> result = new ArrayList<>();

        for (UserProgramDTO up : enrolledPrograms) {
            for (TotaraProgramDTO program : all) {
                result.add(program);
            }
        }

        return result;
    }

    @Override
    public List<TotaraProgramDTO> findOverdueProgramEnrollments(DateTime fromDateTime) {

        List<TotaraProgramDTO> all = demoUtilityService.getPrograms();
        List<UserProgramDTO> enrolledPrograms = demoUtilityService.getUserPrograms();
        List<TotaraProgramDTO> result = new ArrayList<>();

        for (UserProgramDTO up : enrolledPrograms) {
            if (fromDateTime.isAfter(up.getEnrollmentDate())) {
                for (TotaraProgramDTO program : all) {
                    result.add(program);
                }
            }
        }

        return result;
    }

    @Override
    public List<TotaraProgramDTO> findCompletedProgramsByUser(Long userId) {
        List<UserProgramDTO> list = demoUtilityService.getUserPrograms();
        List<TotaraProgramDTO> all = demoUtilityService.getPrograms();
        Set<Long> filteredByUser = new HashSet<>();
        List<TotaraProgramDTO> result = new ArrayList<>();

        for (UserProgramDTO cl : list) {
            if (cl.getUserId().equals(userId) && cl.getComplete()) {
                filteredByUser.add(cl.getProgramId());
            }
        }

        for (TotaraProgramDTO program : all) {
            if (filteredByUser.contains(program.getCourseId())) {
                result.add(program);
            }
        }

        return result;

    }

    @Override
    public List<TotaraCourseDTO> findCompletedCoursesByUser(Long userId) {
        List<UserCourseDTO> list = demoUtilityService.getUserCourses();
        List<TotaraCourseDTO> all = demoUtilityService.getCourses();
        Set<Long> filteredByUser = new HashSet<>();
        List<TotaraCourseDTO> result = new ArrayList<>();

        for (UserCourseDTO cl : list) {
            if (cl.getUserId().equals(userId) && cl.getComplete()) {
                filteredByUser.add(cl.getCourseId());
            }
        }

        for (TotaraCourseDTO course : all) {
            if (filteredByUser.contains(course.getCourseId())) {
                result.add(course);
            }
        }

        return result;
    }
}
