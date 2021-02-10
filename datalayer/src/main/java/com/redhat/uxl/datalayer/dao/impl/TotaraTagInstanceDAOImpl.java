package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraTagInstanceDAO;
import com.redhat.uxl.datalayer.sql.totara.TotaraTagInstanceSQL;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagInstanceDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * The type Totara tag instance dao.
 */
@Service
public class TotaraTagInstanceDAOImpl implements TotaraTagInstanceDAO {

  @Inject
  private JdbcTemplate totaraJdbcTemplate;

  @Override
  public void save(TotaraTagInstanceDTO totaraTagInstanceDTO) {
    Long timemillis = System.currentTimeMillis();
    totaraJdbcTemplate.update(TotaraTagInstanceSQL.INSERT_INTO,
        totaraTagInstanceDTO.getTagId(),
        totaraTagInstanceDTO.getComponent(),
        totaraTagInstanceDTO.getItemType(),
        totaraTagInstanceDTO.getItemid(),
        totaraTagInstanceDTO.getContextid(),
        totaraTagInstanceDTO.getTiuserid(),
        totaraTagInstanceDTO.getOrdering(),
        timemillis,
        timemillis);
  }

  @Override
  public void delete(long totaraId, long tagId) {
    totaraJdbcTemplate.update(TotaraTagInstanceSQL.DELETE, tagId, totaraId);
  }

  @Override
  public void deleteWikiId(long wikiId, long tagId) {
    totaraJdbcTemplate.update(TotaraTagInstanceSQL.DELETE_WIKI, wikiId, tagId);
  }
}
