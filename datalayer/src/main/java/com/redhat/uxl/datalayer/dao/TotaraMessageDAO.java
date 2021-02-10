package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.dataobjects.domain.dto.TotaraMessageDTO;
import java.util.List;

/**
 * The interface Totara message dao.
 */
public interface TotaraMessageDAO {

    /**
     * Find totara notification messages list.
     *
     * @param userId the user id
     * @return the list
     */
    List<TotaraMessageDTO> findTotaraNotificationMessages(Long userId);

    /**
     * Find totara notification messages for team list.
     *
     * @param userId the user id
     * @return the list
     */
    List<TotaraMessageDTO> findTotaraNotificationMessagesForTeam(Long userId);

    /**
     * Find totara notification messages for message list.
     *
     * @param userId the user id
     * @return the list
     */
    List<TotaraMessageDTO> findTotaraNotificationMessagesForMessage(Long userId);

    /**
     * Delete totara message.
     *
     * @param messageId the message id
     */
    void deleteTotaraMessage(Long messageId);

    /**
     * Delete all totara message by user.
     *
     * @param currentUserId the current user id
     */
    void deleteAllTotaraMessageByUser(Long currentUserId);
}
