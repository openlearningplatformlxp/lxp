package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraProgramDAO;
import com.redhat.uxl.datalayer.dto.LearningPathProgressionDTO;
import com.redhat.uxl.datalayer.dto.TotaraCourseContentDTO;
import com.redhat.uxl.datalayer.dto.TotaraCourseSetDTO;
import com.redhat.uxl.datalayer.dto.TotaraProgramDTO;
import com.redhat.uxl.datalayer.dto.TotaraTextActivityDTO;
import com.redhat.uxl.datalayer.sql.totara.TotaraCourseSQL;
import com.redhat.uxl.datalayer.sql.totara.TotaraProgramSQL;
import com.redhat.uxl.datalayer.utils.JodaBeanPropertyRowMapper;
import com.redhat.uxl.dataobjects.domain.types.ProgramCourseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Totara program dao.
 */
@Slf4j
@Service
public class TotaraProgramDAOImpl implements TotaraProgramDAO {

    @Inject
    private JdbcTemplate totaraJdbcTemplate;

    @Override
    public TotaraProgramDTO getProgramById(Long programId) {
        // get Program Info
        String programQuery =
            TotaraProgramSQL.getProgramInfoSQL.replace(":programId", programId.toString());
        log.debug(programQuery);

        Map<String, Object> programMap = totaraJdbcTemplate.queryForMap(programQuery);

        return TotaraProgramDTO.valueOf(programMap);
    }

    @Override
    public List<TotaraCourseSetDTO> getCourseSetsInProgram(Long personTotaraId, Long programId,
        Boolean withStatus) {
        String courseSetsQuery;
        if (!withStatus) {
            courseSetsQuery =
                TotaraCourseSQL.getCoursesForProgram.replace(":programId", programId.toString());
        } else {
            courseSetsQuery =
                TotaraCourseSQL.getCoursesForProgramWithStatus.replace(":programId", programId.toString())
                    .replace(":userId", personTotaraId.toString());
        }
        log.debug(courseSetsQuery);
        List<Map<String, Object>> courseSetMapList = totaraJdbcTemplate.queryForList(courseSetsQuery);

        Map<Long, TotaraCourseSetDTO> courseSetsOut = new HashMap<>();
        TotaraCourseContentDTO courseBo;
        for (Map<String, Object> courseSetMap : courseSetMapList) {
            if (!courseSetsOut.containsKey((Long) courseSetMap.get("coursesetid"))) {
                courseSetsOut.put((Long) courseSetMap.get("coursesetid"),
                    TotaraCourseSetDTO.valueOf(courseSetMap));
            }

            // create new courseBO
            courseBo = new TotaraCourseContentDTO();
            courseBo.setCourseID((Long) courseSetMap.get("courseid"));
            courseBo.setCourseFullName((String) courseSetMap.get("coursefullname"));
            courseBo.setDescription((String) courseSetMap.get("coursedescription"));

            Long activityCount = (Long) courseSetMap.get("mycount");
            if (activityCount == 1) {
                courseBo.setCourseType(ProgramCourseType.SINGLE_ACTIVITY_COURSE);
            } else {
                courseBo.setCourseType(ProgramCourseType.COURSE);
            }

            courseSetsOut.get((Long) courseSetMap.get("coursesetid")).getCourses().add(courseBo);
        }

        return new ArrayList<>(courseSetsOut.values());

    }

    @Override
    public Boolean isUserEnrolledInProgram(Long personTotaraId, Long programId) {
        String query = TotaraProgramSQL.getUserProgramEnrollment
            .replace(":programId", programId.toString()).replace(":userId", personTotaraId.toString());
        log.debug(query);

        List<Map<String, Object>> responseMap = totaraJdbcTemplate.queryForList(query);

        return !(responseMap == null || responseMap.isEmpty());
    }

    @Override public List<LearningPathProgressionDTO> findLearningPathProgression(Long programId,
        Long userId) {
        String query = TotaraProgramSQL.getProgramProgressionStats;
        return totaraJdbcTemplate.query(query, new Object[] {userId, userId, programId, userId},
            new JodaBeanPropertyRowMapper<>(LearningPathProgressionDTO.class));
    }

    @Override
    public LearningPathProgressionDTO findLearningPathTotalDuration(Long programId) {
        String query = TotaraProgramSQL.getProgramTotalDuration;
        return totaraJdbcTemplate.queryForObject(query, new Object[] {programId},
            new JodaBeanPropertyRowMapper<>(LearningPathProgressionDTO.class));
    }

    @Override
    public Long isCourseANestedProgram(Long courseId) {
        String query = TotaraProgramSQL.isCourseANestedProgram;
        try {
            return totaraJdbcTemplate.queryForObject(query, new Object[] {courseId}, Long.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TotaraTextActivityDTO isCourseATextEntry(Long userId, Long courseId) {
        String query = TotaraProgramSQL.isCourseATextEntry;
        try {
            return totaraJdbcTemplate.queryForObject(query,
                new Object[] {userId, courseId, userId, courseId},
                new JodaBeanPropertyRowMapper<>(TotaraTextActivityDTO.class));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LearningPathProgressionDTO> findLearningPathProgressionNonEnrolled(Long programId) {
        String query = TotaraProgramSQL.getNonEnrolledProgression;
        return totaraJdbcTemplate.query(query, new Object[] {programId},
            new JodaBeanPropertyRowMapper<>(LearningPathProgressionDTO.class));
    }

    @Override
    public void dropUser(Long programId, Long userId) {
        totaraJdbcTemplate.update(TotaraProgramSQL.deleteProgramAssignment, new Object[] {userId, programId});
        totaraJdbcTemplate.update(TotaraProgramSQL.deleteProgram, new Object[] {programId, userId});
    }
}
