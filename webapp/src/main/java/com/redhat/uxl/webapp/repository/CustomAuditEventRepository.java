package com.redhat.uxl.webapp.repository;

import com.redhat.uxl.datalayer.repository.PersistenceAuditEventRepository;
import com.redhat.uxl.dataobjects.domain.PersistentAuditEvent;
import com.redhat.uxl.webapp.config.audit.AuditEventConverter;
import org.joda.time.LocalDateTime;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * The type Custom audit event repository.
 */
@Repository
public class CustomAuditEventRepository {

    @Inject
    private PersistenceAuditEventRepository persistenceAuditEventRepository;

    /**
     * Audit event repository audit event repository.
     *
     * @return the audit event repository
     */
    @Bean
    public AuditEventRepository auditEventRepository() {
        return new AuditEventRepository() {

            private static final String AUTHORIZATION_FAILURE = "AUTHORIZATION_FAILURE";

            private static final String ANONYMOUS_USER = "anonymousUser";

            @Inject
            private AuditEventConverter auditEventConverter;

            @Override
            public List<AuditEvent> find(String principal, Instant after, String type) {
                Iterable<PersistentAuditEvent> persistentAuditEvents;
                if (principal == null && after == null) {
                    persistentAuditEvents = persistenceAuditEventRepository.findAll();
                } else if (after == null) {
                    persistentAuditEvents = persistenceAuditEventRepository.findByPrincipal(principal);
                } else {
                    persistentAuditEvents = persistenceAuditEventRepository
                            .findByPrincipalAndAuditEventDateAfter(principal, new LocalDateTime(after));
                }
                return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
            }

            @Override
            @Transactional(propagation = Propagation.REQUIRES_NEW)
            public void add(AuditEvent event) {
                if (!AUTHORIZATION_FAILURE.equals(event.getType())
                        && !ANONYMOUS_USER.equals(event.getPrincipal().toString())) {
                    PersistentAuditEvent persistentAuditEvent = new PersistentAuditEvent();
                    persistentAuditEvent.setPrincipal(event.getPrincipal());
                    persistentAuditEvent.setAuditEventType(event.getType());
                    persistentAuditEvent.setAuditEventDate(new LocalDateTime(event.getTimestamp()));
                    persistentAuditEvent.setData(auditEventConverter.convertDataToStrings(event.getData()));

                    persistenceAuditEventRepository.save(persistentAuditEvent);
                }
            }
        };
    }
}
