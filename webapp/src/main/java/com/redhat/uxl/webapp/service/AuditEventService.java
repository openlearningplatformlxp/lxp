package com.redhat.uxl.webapp.service;

import java.util.List;
import org.joda.time.LocalDateTime;
import org.springframework.boot.actuate.audit.AuditEvent;

/**
 * The interface Audit event service.
 */
public interface AuditEventService {
    /**
     * Find audit event.
     *
     * @param id the id
     * @return the audit event
     */
    AuditEvent find(Long id);

    /**
     * Find all list.
     *
     * @return the list
     */
    List<AuditEvent> findAll();

    /**
     * Find by dates list.
     *
     * @param fromDate the from date
     * @param toDate   the to date
     * @return the list
     */
    List<AuditEvent> findByDates(LocalDateTime fromDate, LocalDateTime toDate);
}
