package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraSolrDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * The type Totara solr dao.
 */
@Service
public class TotaraSolrDAOImpl implements TotaraSolrDAO {

  @Inject
  private JdbcTemplate totaraJdbcTemplate;

    @Override
  public void clearTable() {
    // totaraJdbcTemplate.update(TotaraSolrSQL.CLEAR_TABLE);
  }
}
