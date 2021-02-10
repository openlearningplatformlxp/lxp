package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.dataobjects.domain.dto.TotaraEvidenceTypeDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraSubmittedEvidenceDTO;
import java.util.List;

/**
 * The interface Totara evidence dao.
 */
public interface TotaraEvidenceDAO {

    /**
     * Gets evidence types.
     *
     * @return the evidence types
     */
    List<TotaraEvidenceTypeDTO> getEvidenceTypes();

    /**
     * Submit message.
     *
     * @param userId the user id
     */
    void submitMessage(Long userId);

    /**
     * Insert evidence info data long.
     *
     * @param shortname  the shortname
     * @param input      the input
     * @param evidenceId the evidence id
     * @param isDate     the is date
     * @return the long
     */
    Long insertEvidenceInfoData(String shortname, String input, int evidenceId, boolean isDate);

    /**
     * Update evidence info data.
     *
     * @param input      the input
     * @param evidenceId the evidence id
     */
    void updateEvidenceInfoData(String input, Long evidenceId);

    /**
     * Add evidence int.
     *
     * @param name                        the name
     * @param evidenceTypeId              the evidence type id
     * @param userId                      the user id
     * @param description                 the description
     * @param externalActivityUrl         the external activity url
     * @param externalActivityInstitution the external activity institution
     * @param date                        the date
     * @return the int
     */
    int addEvidence(String name, Long evidenceTypeId, Long userId, String description, String externalActivityUrl,
            String externalActivityInstitution, String date);

    /**
     * Gets submitted evidence.
     *
     * @param userid the userid
     * @return the submitted evidence
     */
    List<TotaraSubmittedEvidenceDTO> getSubmittedEvidence(Long userid);
}
