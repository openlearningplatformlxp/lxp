package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraMessageDAO;
import com.redhat.uxl.datalayer.sql.totara.TotaraMessageSQL;
import com.redhat.uxl.datalayer.utils.JodaBeanPropertyRowMapper;
import com.redhat.uxl.dataobjects.domain.dto.TotaraMessageDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * The type Totara message dao.
 */
@Service
@ConditionalOnExpression("!${demo.mode.enabled:false}")
public class TotaraMessageDAOImpl implements TotaraMessageDAO {

  @Inject
  private JdbcTemplate totaraJdbcTemplate;

  @Override
  public List<TotaraMessageDTO> findTotaraNotificationMessagesForTeam(Long userId) {
    return totaraJdbcTemplate.query(TotaraMessageSQL.S_SELECT_NOTIFICATIONS_FOR_TEAM, new Object[] {userId},
        new JodaBeanPropertyRowMapper<>(TotaraMessageDTO.class));
  }

  @Override
  public List<TotaraMessageDTO> findTotaraNotificationMessagesForMessage(Long userId) {
    return totaraJdbcTemplate.query(TotaraMessageSQL.S_SELECT_MESSAGE_NOTIFACTIONS, new Object[] {userId},
        new JodaBeanPropertyRowMapper<>(TotaraMessageDTO.class));
  }

  @Override
  public List<TotaraMessageDTO> findTotaraNotificationMessages(Long userId) {
    return totaraJdbcTemplate.query(TotaraMessageSQL.S_SELECT_USER_NOTIFICATIONS, new Object[] {userId},
        new JodaBeanPropertyRowMapper<>(TotaraMessageDTO.class));
  }

    @Override
  @Transactional
  public void deleteTotaraMessage(Long userId) {
    totaraJdbcTemplate.update(TotaraMessageSQL.DELETE_NOTIFICATIONS, new Object[] {userId});

  }

  @Override
  public void deleteAllTotaraMessageByUser(Long currentUserId) {
    totaraJdbcTemplate.update(TotaraMessageSQL.DELETE_NOTIFICATIONS_BY_USER_ID, new Object[] {currentUserId});
  }
}
