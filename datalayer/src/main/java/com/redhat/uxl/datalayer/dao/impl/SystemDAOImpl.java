package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.SystemDAO;
import javax.inject.Inject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type System dao.
 */
@Service
public class SystemDAOImpl implements SystemDAO {
  @Inject
  private JdbcTemplate jdbcTemplate;

  private static final String HEALTH_CHECK = "SELECT 1";

  @Override
  @Transactional(readOnly = true)
  public void healthCheck() {
    jdbcTemplate.execute(HEALTH_CHECK);
  }
}
