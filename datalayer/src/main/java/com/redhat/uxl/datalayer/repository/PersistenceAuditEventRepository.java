package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.PersistentAuditEvent;
import java.util.List;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Persistence audit event repository.
 */
public interface PersistenceAuditEventRepository extends BaseJpaRepository<PersistentAuditEvent, Long> {

    /**
     * Find by principal list.
     *
     * @param principal the principal
     * @return the list
     */
    List<PersistentAuditEvent> findByPrincipal(String principal);

    /**
     * Find top by principal and audit event type order by audit event date desc persistent audit event.
     *
     * @param principal the principal
     * @param eventType the event type
     * @return the persistent audit event
     */
    PersistentAuditEvent findTopByPrincipalAndAuditEventTypeOrderByAuditEventDateDesc(String principal,
            String eventType);

    /**
     * Find by principal and audit event date after list.
     *
     * @param principal     the principal
     * @param afterDateTime the after date time
     * @return the list
     */
    List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(String principal, LocalDateTime afterDateTime);

    /**
     * Find all by audit event date between list.
     *
     * @param fromDateTime the from date time
     * @param toDateTime   the to date time
     * @return the list
     */
    List<PersistentAuditEvent> findAllByAuditEventDateBetween(LocalDateTime fromDateTime, LocalDateTime toDateTime);
}
