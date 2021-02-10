package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraCourseDAO;
import com.redhat.uxl.datalayer.dto.CoursePlayerCourseDTO;
import com.redhat.uxl.datalayer.sql.totara.TotaraCourseSQL;
import com.redhat.uxl.datalayer.utils.JodaBeanPropertyRowMapper;
import com.redhat.uxl.dataobjects.domain.dto.TotaraActivityDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraFaceToFaceSessionDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraHtmlBlockDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.types.CourseDocumentVisibilityType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import de.ailis.pherialize.Mixed;
import de.ailis.pherialize.MixedArray;
import de.ailis.pherialize.Pherialize;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * The type Totara course dao.
 */
@Service
@Slf4j
@ConditionalOnExpression("!${demo.mode.enabled:false}")
public class TotaraCourseDAOImpl implements TotaraCourseDAO {

    /**
     * The constant PUBLIC_VISIBILITY_ID.
     */
    public static final Long PUBLIC_VISIBILITY_ID = 2l;
    @Inject
    private JdbcTemplate totaraJdbcTemplate;

    @Override
    public List<TotaraCourseDTO> findActiveCourses() {
        return totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_MDL_COURSE, new Object[] {},
                new JodaBeanPropertyRowMapper<>(TotaraCourseDTO.class));
    }

    @Override
    public CoursePlayerCourseDTO getEnrolledCourse(Long personTotaraId, Long courseTotaraId) {
        String query =
            TotaraCourseSQL.enrolledUserCourseSQL.replace(":totaraUserId", personTotaraId.toString());
        query = query.replace(":totaraCourseId", courseTotaraId.toString());
        log.debug(query);
        Map<String, Object> course = totaraJdbcTemplate.queryForMap(query);
        return new CoursePlayerCourseDTO(course);
    }

    @Override
    public List<TotaraCourseDTO> findCoursesById(List<Long> courseIdList) {
        String idsJoined = StringUtils.join(courseIdList, ",");
        return totaraJdbcTemplate.query(StringUtils.replace(TotaraCourseSQL.SQL_SELECT_COURSES_BY_ID, ":ids", idsJoined),
                new Object[] {}, new JodaBeanPropertyRowMapper<>(TotaraCourseDTO.class));
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
     * @param programId   the program id
     * @param programType the program type
     * @return the course document visibility type
     */
    protected CourseDocumentVisibilityType visibilityTypeForProgram(Long programId, ProgramType programType) {
        String query = TotaraCourseSQL.SQL_SELECT_COURSE_VISIBLE;
        if (ProgramType.LEARNING_PATH.equals(programType)) {
            query = TotaraCourseSQL.SQL_SELECT_PROGRAM_VISIBLE;
        }
        try {
            Long visibility = totaraJdbcTemplate.queryForObject(query, new Object[] { programId }, Long.class);
            if (PUBLIC_VISIBILITY_ID.equals(visibility)) {
                return CourseDocumentVisibilityType.PUBLIC;
            } else {
                return CourseDocumentVisibilityType.RESTRICTED_AUDIENCE;
            }
        } catch (Exception e) {
            return CourseDocumentVisibilityType.RESTRICTED_AUDIENCE;
        }

    }

    @Override
    public Long enrolledInForProgram(Long userId, Long programId) {
        try {
            return totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_SELECT_USER_ENROLLED, new Object[] { userId, programId },
                    Long.class);
        } catch (Exception e) {
            return null;
        }
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
        String query = TotaraCourseSQL.SQL_SELECT_AUDIENCE_VISIBLE_COHORT;
        TreeSet<Long> userIds = new TreeSet<>();
        try {
            // Find all audience visible users
            List<Long> audienceUserIds = totaraJdbcTemplate.queryForList(TotaraCourseSQL.SQL_SELECT_AUDIENCE_VISIBLE_COHORT,
                    new Object[] { programId }, Long.class);
            userIds.addAll(audienceUserIds);
            // Find all enroll completed users
            List<Long> enrolledUserIds = totaraJdbcTemplate.queryForList(TotaraCourseSQL.SQL_COURSE_ENROLLED_COMPLETED,
                    new Object[] { programId }, Long.class);
            userIds.addAll(enrolledUserIds);
            return new ArrayList<>(userIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Long findAllowedUserAudienceByUserId(Long userId, Long programId) {
        String query = TotaraCourseSQL.SQL_SELECT_AUDIENCE_VISIBLE_COHORT_BY_USER;
        try {
            return totaraJdbcTemplate.queryForObject(query, new Object[] { programId, userId }, Long.class);
        } catch (Exception e) {
            e.printStackTrace();
            return -1l;
        }
    }

    @Override
    public List<TotaraProgramDTO> findActivePrograms() {
        return totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_ACTIVE_MDL_PROGRAM, new Object[] {},
                new JodaBeanPropertyRowMapper<>(TotaraProgramDTO.class));
    }

    @Override
    public Page<TotaraProgramDTO> findActivePrograms(Pageable pageable) {
        int total = totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_SELECT_COUNT_PROGRAM, new Object[] {}, Integer.class);
        List<TotaraProgramDTO> elements = new ArrayList<>();
        if (total > 0) {
            elements = totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_ACTIVE_MDL_PROGRAM_PAGED,
                    new Object[] { pageable.getPageSize(), pageable.getOffset() },
                    new JodaBeanPropertyRowMapper<>(TotaraProgramDTO.class));
        }
        return new PageImpl<>(elements, pageable, total);
    }

    @Override
    public Page<TotaraProgramDTO> findActiveProgramsById(List<Long> programIdList, Pageable pageable) {
        int total = programIdList.size();
        List<TotaraProgramDTO> elements = new ArrayList<>();
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
                totaraJdbcTemplate.getDataSource());
        if (total > 0) {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("programIds", programIdList);
            parameters.addValue("offset", pageable.getOffset());
            parameters.addValue("limit", pageable.getPageSize());

            elements = namedParameterJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_ACTIVE_MDL_PROGRAM_BY_ID_PAGED, parameters,
                    new JodaBeanPropertyRowMapper<>(TotaraProgramDTO.class));
        }
        return new PageImpl<>(elements, pageable, total);
    }

    @Override
    public Page<TotaraProgramDTO> findActiveProgramsByTag(Long tagId, Pageable pageable) {
        int total = totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_SELECT_COUNT_PROGRAM_BY_TAG, new Object[] { tagId },
                Integer.class);
        List<TotaraProgramDTO> elements = new ArrayList<>();
        if (total > 0) {
            elements = totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_PROGRAM_BY_TAG,
                    new Object[] { tagId, pageable.getPageSize() },
                    new JodaBeanPropertyRowMapper<>(TotaraProgramDTO.class));
        }
        return new PageImpl<>(elements, pageable, total);
    }

    @Override
    public TotaraProgramDTO findProgramByCourseId(Long courseId) {
        try {
            return totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_SELECT_MDL_PROGRAM_BY_COURSE_ID, new Object[] { courseId },
                    new JodaBeanPropertyRowMapper<>(TotaraProgramDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public TotaraProgramDTO findProgramByProgramId(Long programId) {
        try {
            return totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_SELECT_MDL_PROGRAM_BY_PROGRAM_ID, new Object[] { programId },
                    new JodaBeanPropertyRowMapper<>(TotaraProgramDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public TotaraHtmlBlockDTO findProgramHtmlBlockByProgramId(Long programId) {
        try {
            String configDataString = totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_SELECT_MDL_PROGRAM_HTML_BLOCK,
                    new Object[] { programId }, String.class);
            TotaraHtmlBlockDTO htmlBlock = null;

            if (configDataString != null && configDataString.length() > 0) {
                byte[] decodedBytes = Base64.getDecoder().decode(configDataString);
                String decodedDataString = new String(decodedBytes);

                // NOTE: The data string is stored as a PHP object in string format. The following is used to parse out
                // the required portions(s).

                if (decodedDataString != null && decodedDataString.length() > 0) {
                    // NOTE: The deserializer can't handle PHP Object ("O") fields, so convert to Array ("a"), which it
                    // can
                    String arrayDataString = decodedDataString.replace("O:8:\"stdClass\"", "a");

                    Mixed mixed = Pherialize.unserialize(arrayDataString);
                    if (mixed.isArray()) {
                        MixedArray mixedArray = mixed.toArray();

                        String title = mixedArray.getString("title");
                        String htmlContent = mixedArray.getString("text");

                        if (htmlContent != null && htmlContent.length() > 0) {
                            htmlContent = htmlContent.replaceAll("\\r\\n|\\r|\\n", ""); // Replace line endings

                            htmlBlock = new TotaraHtmlBlockDTO();
                            htmlBlock.setTitle(title);
                            htmlBlock.setHtmlContent(htmlContent);
                        }
                    }
                }
            }

            return htmlBlock;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public TotaraCourseDTO findCourseByCourseId(Long courseId) {
        try {
            return totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_SELECT_MDL_COURSE_BY_COURSE_ID, new Object[] { courseId },
                    new JodaBeanPropertyRowMapper<>(TotaraCourseDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Page<TotaraCourseDTO> findActiveCourses(Pageable pageable) {
        int total = totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_SELECT_COUNT_COURSE, new Object[] {}, Integer.class);
        List<TotaraCourseDTO> elements = new ArrayList<>();
        if (total > 0) {
            elements = totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_MDL_COURSE_PAGED,
                    new Object[] { pageable.getPageSize(), pageable.getOffset() },
                    new JodaBeanPropertyRowMapper<>(TotaraCourseDTO.class));
        }
        return new PageImpl<>(elements, pageable, total);
    }

    @Override
    public Page<TotaraCourseDTO> findActiveCoursesByTag(Long tagId, Pageable pageable) {
        List<Integer> tagDupeIds = totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_DUPE_TAGS_IDS, new Object[] { tagId },
                new RowMapper<Integer>() {
                    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getInt(1);
                    }
                });
        String tagDupeIdsJoined = StringUtils.join(tagDupeIds, ",");
        int total = totaraJdbcTemplate.queryForObject(
                StringUtils.replace(TotaraCourseSQL.SQL_SELECT_COUNT_COURSE_BY_TAG, ":ids", tagDupeIdsJoined), new Object[] {},
                Integer.class);
        List<TotaraCourseDTO> elements = new ArrayList<>();
        if (total > 0) {
            elements = totaraJdbcTemplate.query(StringUtils.replace(TotaraCourseSQL.SQL_SELECT_COURSE_BY_TAG, ":ids", tagDupeIdsJoined),
                    new Object[] { pageable.getPageSize(), pageable.getOffset() },
                    new JodaBeanPropertyRowMapper<>(TotaraCourseDTO.class));
        }
        return new PageImpl<>(elements, pageable, total);
    }

    @Override
    public Page<TotaraCourseDTO> findActiveCoursesByAudience(Long audienceId, Pageable pageable) {
        int total = totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_SELECT_COUNT_COURSE_BY_AUDIENCE, new Object[] { audienceId },
                Integer.class);
        List<TotaraCourseDTO> elements = new ArrayList<>();
        if (total > 0) {
            elements = totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_COURSE_BY_AUDIENCE,
                    new Object[] { audienceId, pageable.getPageSize() },
                    new JodaBeanPropertyRowMapper<>(TotaraCourseDTO.class));
        }
        return new PageImpl<>(elements, pageable, total);
    }

    @Override
    public List<TotaraProgramDTO> findByUserIdAndProgramId(Long userId, Long programId) {
        return totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_MDL_PROGRAM, new Object[] { programId, userId },
                new JodaBeanPropertyRowMapper<>(TotaraProgramDTO.class));
    }

    @Override
    public List<TotaraProgramDTO> findProgramsByUserId(Long userId) {
        return totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_MDL_PROGRAM_BY_USER, new Object[] { userId },
                new JodaBeanPropertyRowMapper<>(TotaraProgramDTO.class));
    }

    @Override
    public Page<TotaraProgramDTO> findInProgressProgramsByUserId(Long userId, Pageable pageable) {

        int total = totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_SELECT_COUNT_MDL_PROGRAMS_BY_USERS_IN_PROGRESS,
                new Object[] { userId, userId, 10000, 0 }, Integer.class);
        log.debug("Total program found: " + total);

        List<TotaraProgramDTO> elements = new ArrayList<>();
        if (total > 0) {
            elements = totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_MDL_PROGRAMS_BY_USERS_IN_PROGRESS,
                    new Object[] { userId, userId, pageable.getPageSize(), pageable.getOffset() },
                    new JodaBeanPropertyRowMapper<>(TotaraProgramDTO.class));
        }
        return new PageImpl<>(elements, pageable, total);
    }

    @Override
    public List<TotaraCourseDTO> findCoursesByUserId(Long userId) {
        return totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_COURSE_ENROLLMENTS, new Object[] { userId },
                new JodaBeanPropertyRowMapper<>(TotaraCourseDTO.class));
    }

    @Override
    public Page<TotaraCourseDTO> findInProgressCoursesByUserId(Long userId, Pageable pageable) {
        int total = totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_SELECT_COUNT_COURSE_ENROLLMENTS_IN_PROGRESS,
                new Object[] { userId, 10000, 0 }, Integer.class);
        List<TotaraCourseDTO> elements = new ArrayList<>();
        if (total > 0) {
            elements = totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_COURSE_ENROLLMENTS_IN_PROGRESS,
                    new Object[] { userId, pageable.getPageSize(), pageable.getOffset() },
                    new JodaBeanPropertyRowMapper<>(TotaraCourseDTO.class));
        }
        return new PageImpl<>(elements, pageable, total);
    }

    @Override
    public TotaraActivityDTO findActivityByInstanceId(Long instanceId) {
        try {
            return totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_SELECT_ACTIVITY_ID_BY_INSTANCE, new Object[] { instanceId },
                    new JodaBeanPropertyRowMapper<>(TotaraActivityDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<TotaraActivityDTO> findActivitiesByUserId(Long userId) {
        return totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_ACTIVITY_ENROLLMENTS, new Object[] { userId },
                new JodaBeanPropertyRowMapper<>(TotaraActivityDTO.class));
    }

    @Override
    public List<TotaraActivityDTO> findCompletedActivitiesByUserId(Long userId, int max) {
        return totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_COMPLETED_ACTIVITY_ENROLLMENTS, new Object[] { userId },
                new JodaBeanPropertyRowMapper<>(TotaraActivityDTO.class));
    }

    @Override
    public List<TotaraActivityDTO> findActivityForUserCourses(Long userId) {
        return totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_ACTIVTIES_FOR_USER_COURSES, new Object[] { userId },
                new JodaBeanPropertyRowMapper<>(TotaraActivityDTO.class));
    }

    @Override
    public List<TotaraFaceToFaceSessionDTO> findFaceToFaceSessions(Long courseId) {
        return totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_FACE_TO_FACE_SESSIONS_BY_COURSE_ID, new Object[] { courseId },
                new JodaBeanPropertyRowMapper<>(TotaraFaceToFaceSessionDTO.class));
    }

    @Override
    public TotaraFaceToFaceSessionDTO findFaceToFaceSessionById(Long sessionId) {
        return totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_SELECT_FACE_TO_FACE_SESSION_BY_ID, new Object[] { sessionId },
                new JodaBeanPropertyRowMapper<>(TotaraFaceToFaceSessionDTO.class));
    }

    @Override
    public int addFaceToFaceSignup(Long sessionId, Long userId) {
        return totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_INSERT_FACE_TO_FACE_SIGNUP, new Object[] { sessionId, userId },
                Integer.class);
    }

    @Override
    public int getCECreditsForUser(Long userId) {
        String SQL = TotaraCourseSQL.SQL_SELECT_CECREDITS;
        try {
            LocalDateTime todayDate = LocalDateTime.now();
            String endDate;
            String startDate;
            if (todayDate.getMonthValue() < 4) {
                startDate = (todayDate.getYear() - 1) + "-04-01";
                endDate = todayDate.getYear() + "-03-31";
            } else {
                startDate = todayDate.getYear() + "-04-01";
                endDate = (todayDate.getYear() + 1) + "-03-31";
            }
            SQL = SQL.replace(":startDate", startDate);
            SQL = SQL.replace(":endDate", endDate);

            return totaraJdbcTemplate.queryForObject(SQL, new Object[] { userId }, Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int addFaceToFaceSignupStatus(Long signupId) {
        return totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_INSERT_FACE_TO_FACE_SIGNUP_STATUS, new Object[] { signupId },
                Integer.class);
    }

    @Override
    public int enrollInCourse(Long courseId, Long userId) {
        return totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_INSERT_COURSE_COMPLETIONS, new Object[] { userId, courseId },
                Integer.class);

    }

    @Override
    public void inertActivityText(Long activityId, Long userId, String message) {
        java.util.Date date = new Date();
        java.sql.Timestamp saveTime = new java.sql.Timestamp(date.getTime());
        totaraJdbcTemplate.update(TotaraCourseSQL.SQL_INSERT_ACTIVITY_MESSAGE,
                new Object[] { activityId, userId, message, saveTime.getTime(), saveTime.getTime() });

    }

    @Override
    public Long findActivityId(Long activityId, Long course) {
        return totaraJdbcTemplate.queryForObject(TotaraCourseSQL.SQL_FIND_ACTIVITY_ID, new Object[] { activityId, course }, Long.class);

    }

    @Override
    public void completeTotaraCourseActivityDB(Long activityId, Long userId) {
        totaraJdbcTemplate.update(TotaraCourseSQL.SQL_INSERT_COMPLETED_ACTIVITY, new Object[] { activityId, userId });

    }

    @Override
    public List<TotaraProgramDTO> findProgramEnrollments(DateTime fromDateTime) {
        return totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_MDL_PROGRAM_ENROLMENTS,
                new Object[] { fromDateTime.toDate().getTime() / 1000 },
                new JodaBeanPropertyRowMapper<>(TotaraProgramDTO.class));
    }

    @Override
    public List<TotaraProgramDTO> findOverdueProgramEnrollments(DateTime fromDateTime) {
        return totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_MDL_PROGRAM_OVERDUE,
                new Object[] { fromDateTime.toDate().getTime() / 1000 },
                new JodaBeanPropertyRowMapper<>(TotaraProgramDTO.class));
    }

    @Override
    public List<TotaraProgramDTO> findCompletedProgramsByUser(Long userId) {
        return totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_MDL_PROGRAM_COMPLETIONS_BY_USER, new Object[] { userId },
                new JodaBeanPropertyRowMapper<>(TotaraProgramDTO.class));
    }

    @Override
    public List<TotaraCourseDTO> findCompletedCoursesByUser(Long userId) {
        return totaraJdbcTemplate.query(TotaraCourseSQL.SQL_SELECT_COURSE_ENROLLMENTS_COMPLETED, new Object[] { userId },
                new JodaBeanPropertyRowMapper<>(TotaraCourseDTO.class));
    }
}
