package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraFileDAO;
import com.redhat.uxl.datalayer.sql.totara.TotaraFileSQL;
import com.redhat.uxl.datalayer.utils.JodaBeanPropertyRowMapper;
import com.redhat.uxl.dataobjects.domain.dto.TotaraFileDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * The type Totara file dao.
 */
@Service
public class TotaraFileDAOImpl implements TotaraFileDAO {

  @Inject
  private JdbcTemplate totaraJdbcTemplate;

  @Override
  public TotaraFileDTO getCourseFileBySectionAndName(Long sectionId, String filename) {
    return totaraJdbcTemplate.queryForObject(TotaraFileSQL.S_SELECT_COURSE_FILE_BY_SECTION_AND_NAME,
        new Object[] {sectionId, filename}, new JodaBeanPropertyRowMapper<>(TotaraFileDTO.class));
  }
}
