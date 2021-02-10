package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.dao.TotaraEvidenceDAO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEvidenceTypeDTO;
import com.redhat.uxl.services.service.FileService;
import com.redhat.uxl.services.service.TotaraEvidenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.util.List;

/**
 * The type Totara evidence service.
 */
@Service
public class TotaraEvidenceServiceImpl implements TotaraEvidenceService {

    /**
     * The Totara evidence dao.
     */
    @Inject
  TotaraEvidenceDAO totaraEvidenceDAO;

    /**
     * The File service.
     */
    @Inject
  FileService fileService;

  @Timed
  @Override
  @Transactional(readOnly = true)
  public List<TotaraEvidenceTypeDTO> getEvidenceTypes() {
    return totaraEvidenceDAO.getEvidenceTypes();
  }

  @Timed
  @Override
  @Transactional(readOnly = true)
  public void addEvidence(String name, Long evidenceTypeId, Long userId, String description,
      String externalActivityUrl, String externalActivityInstitution, String date,
      MultipartFile file) {
    // Gonna flip these to match ticket
    int evidenceId = totaraEvidenceDAO.addEvidence(name, evidenceTypeId, userId, description,
        externalActivityUrl, externalActivityInstitution, date);

    if (file != null) {
      try {

        // Add to list
        Long id = totaraEvidenceDAO.insertEvidenceInfoData("evidencefileattachments",
            "" + evidenceId, evidenceId, false);

        // Update the totara evidence
        totaraEvidenceDAO.updateEvidenceInfoData("" + id, id);

        fileService.upload(userId, id, file, "evidence%5ffilemgr", "totara%5fcustomfield");

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    totaraEvidenceDAO.submitMessage(userId);

    // Need to add the uploaded file to the evidence list
  }
}
