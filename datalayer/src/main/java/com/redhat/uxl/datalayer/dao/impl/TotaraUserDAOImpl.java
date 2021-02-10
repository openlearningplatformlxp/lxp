package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraUserDAO;
import com.redhat.uxl.datalayer.sql.totara.TotaraUserSQL;
import com.redhat.uxl.datalayer.utils.JodaBeanPropertyRowMapper;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * The type Totara user dao.
 */
@Service
@ConditionalOnExpression("!${demo.mode.enabled:false}")
public class TotaraUserDAOImpl implements TotaraUserDAO {

  @Inject
  private JdbcTemplate totaraJdbcTemplate;

  @Override
  public List<TotaraUserDTO> findAllActiveUsers() {
    return totaraJdbcTemplate.query(TotaraUserSQL.S_SELECT_ACTIVE_USERS, new Object[] {},
        new JodaBeanPropertyRowMapper<>(TotaraUserDTO.class));
  }

  @Override
  public List<TotaraUserDTO> findAllActiveUsersInProgram(Long programId) {
    return totaraJdbcTemplate.query(TotaraUserSQL.S_SELECT_ACIVE_USERS_FOR_PROG, new Object[] {programId},
        new JodaBeanPropertyRowMapper<>(TotaraUserDTO.class));
  }

  @Override
  public List<TotaraUserDTO> findUsersByIds(Set<Long> userIdList) {
    NamedParameterJdbcTemplate namedJDBCTemplate =
        new NamedParameterJdbcTemplate(totaraJdbcTemplate);
    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue("userIds", userIdList);

    return namedJDBCTemplate.query(TotaraUserSQL.S_SELECT_USERS_BY_IDS, parameters,
        new JodaBeanPropertyRowMapper<>(TotaraUserDTO.class));
  }

  @Override
  public TotaraUserDTO findUserById(Long userId) {
    return totaraJdbcTemplate.queryForObject(TotaraUserSQL.S_SELECT_USER_BY_ID, new Object[] {userId},
        new JodaBeanPropertyRowMapper<>(TotaraUserDTO.class));
  }
}
