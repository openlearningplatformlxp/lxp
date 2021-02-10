package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraEvidenceDAO;
import com.redhat.uxl.datalayer.sql.totara.TotaraEvidenceSQL;
import com.redhat.uxl.datalayer.utils.JodaBeanPropertyRowMapper;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEvidenceTypeDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraSubmittedEvidenceDTO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Totara evidence dao.
 */
@Service
@Slf4j
public class TotaraEvidenceDAOImpl implements TotaraEvidenceDAO {

  @Inject
  private JdbcTemplate totaraJdbcTemplate;

  @Override
  public void submitMessage(Long userId) {
    totaraJdbcTemplate.update(TotaraEvidenceSQL.SQL_SUBMIT_MESSAGE, userId, userId, userId, userId);
  }

  @Override
  public List<TotaraSubmittedEvidenceDTO> getSubmittedEvidence(Long userid) {
    return new ArrayList<>(); // TODO: Implement Me
    // return totaraJdbcTemplate.query(SQL_GET_PENDING_EVIDENCE, new Object[]{userid}, new
    // JodaBeanPropertyRowMapper<>(TotaraSubmittedEvidence.class));
  }

  @Override
  public List<TotaraEvidenceTypeDTO> getEvidenceTypes() {
    return totaraJdbcTemplate.query(TotaraEvidenceSQL.S_SELECT_EVIDENCE_TYPES, new Object[] {},
        new JodaBeanPropertyRowMapper<>(TotaraEvidenceTypeDTO.class));
  }

  @Override
  public int addEvidence(String name, Long evidenceTypeId, Long userId, String description,
      String externalActivityUrl, String externalActivityInstitution, String date) {
    int evidenceId = totaraJdbcTemplate.queryForObject(TotaraEvidenceSQL.SQL_INSERT_EVIDENCE,
        new Object[] {description, evidenceTypeId, userId}, Integer.class);

    insertEvidenceInfoData("evidencedescription", name, evidenceId, false);

    if (externalActivityUrl != null && !externalActivityUrl.isEmpty()) {
      insertEvidenceInfoData("evidencelink", externalActivityUrl, evidenceId, false);
    }

    insertEvidenceInfoData("evidenceinstitution", externalActivityInstitution, evidenceId, false);

    if (date != null && !date.isEmpty()) {
      insertEvidenceInfoData("evidencedatecompleted", date, evidenceId, true);
    }

    // We need to update custom table
    DateTimeFormatter format = DateTimeFormat.forPattern("MMddyyyy");
    DateTime dateTime = DateTime.parse(date, format);
    totaraJdbcTemplate.update(TotaraEvidenceSQL.SQL_INSERT_CECREDITS, userId, evidenceId, 50, 50, evidenceTypeId,
        dateTime.getMillis() / 1000, new DateTime().getMillis() / 1000);

    return evidenceId;
  }

  @Override
  public Long insertEvidenceInfoData(String shortname, String input, int evidenceId,
      boolean isDate) {
    String query = TotaraEvidenceSQL.SQL_SELECT_EVIDENCE_INFO_FIELD.replace(":shortname", shortname);
    log.debug(query);
    Long returnId = null;

    try {
      Map<String, Object> field = totaraJdbcTemplate.queryForMap(query);

      if (!isDate) {
        returnId = totaraJdbcTemplate.queryForObject(TotaraEvidenceSQL.SQL_INSERT_EVIDENCE_INFO_DATA,
            new Object[] {field.get("id"), evidenceId, input}, Long.class);
      } else {
        DateTimeFormatter format = DateTimeFormat.forPattern("MMddyyyy");
        DateTime dateTime = DateTime.parse(input, format);

        returnId = totaraJdbcTemplate.queryForObject(TotaraEvidenceSQL.SQL_INSERT_EVIDENCE_INFO_DATA,
            new Object[] {field.get("id"), evidenceId, dateTime.getMillis() / 1000}, Long.class);
      }
    } catch (EmptyResultDataAccessException erdae) {
      log.error("Cannot find field with a short name " + shortname);
    }

    return returnId;
  }

  @Override
  public void updateEvidenceInfoData(String input, Long evidenceId) {

    totaraJdbcTemplate.update(TotaraEvidenceSQL.SQL_UPDATE_EVIDENCE_INFO_DATA, input, evidenceId);

  }

}
