package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraEnrollmentDAO;
import com.redhat.uxl.datalayer.dto.EnrolledTotaraCourseDTO;
import com.redhat.uxl.datalayer.dto.TotaraServiceResponseDTO;
import com.redhat.uxl.datalayer.sql.totara.TotaraCourseEnrollmentSQL;
import com.redhat.uxl.datalayer.sql.totara.TotaraCourseSQL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Totara enrollment dao.
 */
@Slf4j
@Service
public class TotaraEnrollmentDAOImpl extends AbstractBaseTotaraDAOImpl implements TotaraEnrollmentDAO {

    @Inject
    private JdbcTemplate totaraJdbcTemplate;

    @Override public Boolean isUserEnrolledInCourse(Long courseId, Long personTotaraId) {
        String query = TotaraCourseSQL.isUserEnrolledInCourseSQL.replace(":totaraUserId",
            personTotaraId.toString());
        query = query.replace(":totaraCourseId", courseId.toString());

        log.debug(query);

        List<Map<String, Object>> enrollments = totaraJdbcTemplate.queryForList(query);
        return !(enrollments == null || enrollments.isEmpty());
    }

    @Override public Map<Long, EnrolledTotaraCourseDTO> getEnrolledCoursesById(Long personTotaraId,
        List<Long> courseIds) {
        // create set for courseIds

        // get ids from list of activities
        String set = toStringLongList(courseIds);
        String query = TotaraCourseEnrollmentSQL.getEnrollmentsByCourseId
            .replace(":totaraUserId", personTotaraId.toString()).replace(":courseIdSet", set);
        log.debug(query);

        List<Map<String, Object>> enrolledLearning = totaraJdbcTemplate.queryForList(query);
        Map<Long, EnrolledTotaraCourseDTO> enrolledTotaraCourseBOs = new HashMap<>();
        for (Map<String, Object> row : enrolledLearning) {
            EnrolledTotaraCourseDTO enrolledCourse = new EnrolledTotaraCourseDTO(row);
            enrolledTotaraCourseBOs.put(enrolledCourse.getCourseID(), enrolledCourse);
        }
        return enrolledTotaraCourseBOs;
    }

    @Override
    public TotaraServiceResponseDTO enrollUserToProgram(Long programId, Long personTotaraId) {
        String function = "&wsfunction=ws_enroll_user_program";
        String params = "&userid=" + personTotaraId + "&programid=" + programId;
        TotaraServiceResponseDTO response = (restTemplate
            .getForObject(generateURL(COURSE_TOKEN, function, params), TotaraServiceResponseDTO.class));
        return response;
    }

    @Override
    public TotaraServiceResponseDTO enrollUserToCourse(Long courseid, Long personTotaraId) {
        String function = "&wsfunction=ws_enroll_user";
        String params = "&userid=" + personTotaraId + "&courseid=" + courseid;
        TotaraServiceResponseDTO response = (restTemplate
            .getForObject(generateURL(COURSE_TOKEN, function, params), TotaraServiceResponseDTO.class));
        return response;
    }

    @Override
    public TotaraServiceResponseDTO enrollUserToProgramCourse(Long courseid, Long personTotaraId,
        Long programId) {
        String function = "&wsfunction=ws_enroll_user_program_course";
        String params =
            "&userid=" + personTotaraId + "&courseid=" + courseid + "&programid=" + programId;
        TotaraServiceResponseDTO response = (restTemplate
            .getForObject(generateURL(COURSE_TOKEN, function, params), TotaraServiceResponseDTO.class));
        return response;
    }

}
