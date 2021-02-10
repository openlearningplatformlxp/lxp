package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraSectionDAO;
import com.redhat.uxl.datalayer.dto.CoursePlayerSectionDTO;
import com.redhat.uxl.datalayer.sql.totara.TotaraSectionSQL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * The type Totara section dao.
 */
@Slf4j
@Service
public class TotaraSectionDAOImpl implements TotaraSectionDAO {
    @Inject
    private JdbcTemplate totaraJdbcTemplate;

    @Override
    public List<Map<String, Object>> getSectionsHaveActivitiesForCourse(String query) {
        return totaraJdbcTemplate.queryForList(query);
    }

    @Override
    public CoursePlayerSectionDTO getSectionForCourse(Long courseId, Long sectionId) {
        String query = TotaraSectionSQL.courseSectionSQL.replace(":courseId", courseId.toString());
        query = query.replace(":sectionId", sectionId.toString());
        log.debug(query);
        Map<String, Object> section = totaraJdbcTemplate.queryForMap(query);
        return new CoursePlayerSectionDTO(section);
    }
}
