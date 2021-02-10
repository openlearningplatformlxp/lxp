package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.dataobjects.domain.dto.TotaraAudienceDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraFeedbackDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import java.util.List;

/**
 * The interface Totara profile dao.
 */
public interface TotaraProfileDAO {

    /**
     * Gets user profile.
     *
     * @param totaraUserId the totara user id
     * @return the user profile
     */
    TotaraUserDTO getUserProfile(Long totaraUserId);

    /**
     * Gets user managers.
     *
     * @param totaraUserId the totara user id
     * @return the user managers
     */
    List<TotaraUserDTO> getUserManagers(Long totaraUserId);

    /**
     * Gets user audiences.
     *
     * @param totaraUserId the totara user id
     * @return the user audiences
     */
    List<TotaraAudienceDTO> getUserAudiences(Long totaraUserId);

    /**
     * Gets user interests.
     *
     * @param totaraUserId the totara user id
     * @return the user interests
     */
    List<TotaraTagDTO> getUserInterests(Long totaraUserId);

    /**
     * Gets user profile by email.
     *
     * @param email the email
     * @return the user profile by email
     */
    TotaraUserDTO getUserProfileByEmail(String email);

    /**
     * Gets user roles.
     *
     * @param totaraUserId the totara user id
     * @return the user roles
     */
    List<TotaraTagDTO> getUserRoles(Long totaraUserId);

    /**
     * Gets user profile field for feedback.
     *
     * @param totaraUserId the totara user id
     * @return the user profile field for feedback
     */
    TotaraFeedbackDTO getUserProfileFieldForFeedback(Long totaraUserId);
}
