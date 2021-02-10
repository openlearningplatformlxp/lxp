package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.Messaging;
import com.redhat.uxl.dataobjects.domain.types.DeliverTo;
import com.redhat.uxl.dataobjects.domain.types.MessageActionType;
import com.redhat.uxl.dataobjects.domain.types.MessageSubjectType;
import com.redhat.uxl.dataobjects.domain.types.MessageType;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Messaging repository.
 */
public interface MessagingRepository extends BaseJpaRepository<Messaging, Long>, JpaSpecificationExecutor<Messaging> {

    /**
     * Find by object type and parent id messaging.
     *
     * @param messageType the message type
     * @param parentId    the parent id
     * @return the messaging
     */
    Messaging findByObjectTypeAndParentId(MessageType messageType, Long parentId);

    /**
     * Find by parent id and deliver to in and active order by created date desc list.
     *
     * @param parentId      the parent id
     * @param deliverToList the deliver to list
     * @param active        the active
     * @return the list
     */
    List<Messaging> findByParentIdAndDeliverToInAndActiveOrderByCreatedDateDesc(Long parentId,
            List<DeliverTo> deliverToList, Boolean active);

    /**
     * Find by parent id and deliver to in and message action type and active list.
     *
     * @param parentId          the parent id
     * @param deliverToList     the deliver to list
     * @param messageActionType the message action type
     * @param active            the active
     * @return the list
     */
    List<Messaging> findByParentIdAndDeliverToInAndMessageActionTypeAndActive(Long parentId,
            List<DeliverTo> deliverToList, MessageActionType messageActionType, Boolean active);

    /**
     * Find by child id and subject type and subject id and active list.
     *
     * @param childId            the child id
     * @param messageSubjectType the message subject type
     * @param subjectId          the subject id
     * @param active             the active
     * @return the list
     */
    List<Messaging> findByChildIdAndSubjectTypeAndSubjectIdAndActive(Long childId,
            MessageSubjectType messageSubjectType, Long subjectId, Boolean active);

    /**
     * Find by parent id and deliver to in list.
     *
     * @param parentId      the parent id
     * @param deliverToList the deliver to list
     * @return the list
     */
    List<Messaging> findByParentIdAndDeliverToIn(Long parentId, List<DeliverTo> deliverToList);

    /**
     * Count by parent id and active long.
     *
     * @param personId the person id
     * @param active   the active
     * @return the long
     */
    Long countByParentIdAndActive(Long personId, Boolean active);

    /**
     * Delete by parent id.
     *
     * @param parentId the parent id
     */
    @Transactional
    @Modifying
    @Query(value = "delete from Messaging where parentId = ?1")
    void deleteByParentId(Long parentId);

    /**
     * Delete by totara notification id.
     *
     * @param parentId the parent id
     */
    @Transactional
    @Modifying
    @Query(value = "delete from Messaging where totaraNotificationId = ?1")
    void deleteByTotaraNotificationId(Long parentId);

    /**
     * Read all messages by user.
     *
     * @param currentUserId the current user id
     */
    @Transactional
    @Modifying
    @Query(value = "update Messaging set active = false where parentId = ?1 and active = true")
    void readAllMessagesByUser(Long currentUserId);
}
