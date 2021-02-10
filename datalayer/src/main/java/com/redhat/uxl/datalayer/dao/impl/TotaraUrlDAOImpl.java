package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraUrlDAO;
import com.redhat.uxl.datalayer.sql.totara.TotaraUrlSQL;
import com.redhat.uxl.datalayer.utils.JodaBeanPropertyRowMapper;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUrlDTO;
import com.redhat.uxl.dataobjects.domain.types.LearningLockerJobExecutionType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * The type Totara url dao.
 */
@Service
@ConditionalOnExpression("!${demo.mode.enabled:false}")
public class TotaraUrlDAOImpl implements TotaraUrlDAO {

  @Inject
  private JdbcTemplate totaraJdbcTemplate;

    @Override
  public List<TotaraUrlDTO> findByExternalUrlPathId(
      LearningLockerJobExecutionType learningLockerJobExecutionType, String externalUrlPath) {
    List<TotaraUrlDTO> returnVar = null;
    try {
      switch (learningLockerJobExecutionType) {
        case KALTURA:
          externalUrlPath = "%" + externalUrlPath + "%";

          returnVar = totaraJdbcTemplate.query(TotaraUrlSQL.SQL_SELECT_MDL_URL_BY_EXTERNAL_URL_PATH_KALTURA,
              new Object[] {externalUrlPath}, new JodaBeanPropertyRowMapper<>(TotaraUrlDTO.class));
          break;
        case LYNDA:
        case ALLEGO:
          returnVar = totaraJdbcTemplate.query(TotaraUrlSQL.SQL_SELECT_MDL_URL_BY_EXTERNAL_URL_PATH,
              new Object[] {learningLockerJobExecutionType.name().toLowerCase(), externalUrlPath},
              new JodaBeanPropertyRowMapper<>(TotaraUrlDTO.class));

          break;
      }

    } catch (EmptyResultDataAccessException e) {
      return null;
    }
    return returnVar;
  }
}
