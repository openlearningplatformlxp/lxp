package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraTeamDAO;
import com.redhat.uxl.datalayer.sql.totara.TotaraTeamSQL;
import com.redhat.uxl.datalayer.utils.JodaBeanPropertyRowMapper;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTeamCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserWithCountsDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * The type Totara team dao.
 */
@Service
@ConditionalOnExpression("!${demo.mode.enabled:false}")
public class TotaraTeamDAOImpl implements TotaraTeamDAO {


  @Inject
  private JdbcTemplate totaraJdbcTemplate;

  @Override
  public Boolean isManager(Long userId) {
    int total = totaraJdbcTemplate.queryForObject(TotaraTeamSQL.SQL_SELECT_COUNT_TEAM_MEMBERS,
        new Object[] {userId}, Integer.class);
    return total > 0;
  }

  @Override
  public List<TotaraUserWithCountsDTO> findTeamMembersWithCountsByManager(Long managerId) {
    return totaraJdbcTemplate.query(TotaraTeamSQL.SQL_SELECT_TEAM_MANGERS_WITH_COUNTS, new Object[] {managerId},
        new JodaBeanPropertyRowMapper<>(TotaraUserWithCountsDTO.class));
  }

  @Override
  public List<TotaraProgramDTO> findTeamMembersPrograms(Set<Long> userIds) {

    String valuesClause = "ANY (values";
    if (userIds == null || userIds.isEmpty()) {
      valuesClause = valuesClause + "())";
    } else {
      for (Long id : userIds) {
        valuesClause = valuesClause + " (" + id + "),";
      }
      valuesClause = valuesClause.substring(0, valuesClause.length() - 1) + ")";

    }

    return totaraJdbcTemplate.query(
        TotaraTeamSQL.SQL_SELECT_MDL_PROGRAM_BY_USERS.replace(":replaceme", valuesClause),
        new JodaBeanPropertyRowMapper<>(TotaraProgramDTO.class));
  }

  @Override
  public List<TotaraTeamCourseDTO> findTeamMembersCourses(Set<Long> userId) {

    String valuesClause = "ANY (values";
    if (userId == null || userId.isEmpty()) {
      valuesClause = valuesClause + "())";
    } else {
      for (Long id : userId) {
        valuesClause = valuesClause + " (" + id + "),";
      }
      valuesClause = valuesClause.substring(0, valuesClause.length() - 1) + ")";

    }
    StopWatch watch = new StopWatch();
    watch.start();

    List<TotaraTeamCourseDTO> courses =
        totaraJdbcTemplate.query(TotaraTeamSQL.SQL_SELECT_MDL_COURSE_BY_USERS.replace(":replaceme", valuesClause),
            new JodaBeanPropertyRowMapper<>(TotaraTeamCourseDTO.class));
    watch.stop();
    return courses;

  }
}
