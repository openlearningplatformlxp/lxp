package com.redhat.uxl.webapp.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.repository.PersistenceAuditEventRepository;
import com.redhat.uxl.dataobjects.domain.PersistentAuditEvent;
import com.redhat.uxl.webapp.config.audit.AuditEventConverter;
import com.redhat.uxl.webapp.service.AuditEventService;
import java.util.List;
import javax.inject.Inject;
import org.joda.time.LocalDateTime;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Audit event service.
 */
@Service
@Transactional
public class AuditEventServiceImpl implements AuditEventService {

    private PersistenceAuditEventRepository persistenceAuditEventRepository;
    private AuditEventConverter auditEventConverter;

    /**
     * Instantiates a new Audit event service.
     *
     * @param persistenceAuditEventRepository the persistence audit event repository
     * @param auditEventConverter             the audit event converter
     */
    @Inject
    public AuditEventServiceImpl(PersistenceAuditEventRepository persistenceAuditEventRepository,
            AuditEventConverter auditEventConverter) {
        this.persistenceAuditEventRepository = persistenceAuditEventRepository;
        this.auditEventConverter = auditEventConverter;
    }

    @Override
    @Timed
    @Transactional(readOnly = true)
    public List<AuditEvent> findAll() {
        return auditEventConverter.convertToAuditEvent(persistenceAuditEventRepository.findAll());
    }

    @Override
    @Timed
    @Transactional(readOnly = true)
    public List<AuditEvent> findByDates(LocalDateTime fromDate, LocalDateTime toDate) {
        List<PersistentAuditEvent> persistentAuditEvents = persistenceAuditEventRepository
                .findAllByAuditEventDateBetween(fromDate, toDate);

        return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
    }

    @Override
    @Timed
    @Transactional(readOnly = true)
    public AuditEvent find(Long id) {
        PersistentAuditEvent event = persistenceAuditEventRepository.findOne(id);
        AuditEvent auditEvent = null;

        if (event != null) {
            auditEvent = auditEventConverter.convertToAuditEvent(event);
        }

        return auditEvent;
    }
}
