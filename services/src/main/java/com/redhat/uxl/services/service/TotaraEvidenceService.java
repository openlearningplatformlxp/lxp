package com.redhat.uxl.services.service;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEvidenceTypeDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * The interface Totara evidence service.
 */
public interface TotaraEvidenceService {
    /**
     * Gets evidence types.
     *
     * @return the evidence types
     */
    List<TotaraEvidenceTypeDTO> getEvidenceTypes();

    /**
     * Add evidence.
     *
     * @param name                        the name
     * @param evidenceTypeId              the evidence type id
     * @param userId                      the user id
     * @param description                 the description
     * @param externalActivityUrl         the external activity url
     * @param externalActivityInstitution the external activity institution
     * @param date                        the date
     * @param file                        the file
     */
    @Timed
    @Transactional(readOnly = true)
    void addEvidence(String name, Long evidenceTypeId, Long userId, String description, String externalActivityUrl,
            String externalActivityInstitution, String date, MultipartFile file);
}
