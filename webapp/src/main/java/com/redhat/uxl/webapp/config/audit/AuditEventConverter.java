package com.redhat.uxl.webapp.config.audit;

import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.dataobjects.domain.PersistentAuditEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * The type Audit event converter.
 */
@Component
public class AuditEventConverter {
    /**
     * Convert to audit event list.
     *
     * @param persistentAuditEvents the persistent audit events
     * @return the list
     */
    public List<AuditEvent> convertToAuditEvent(Iterable<PersistentAuditEvent> persistentAuditEvents) {
        if (persistentAuditEvents == null) {
            return Collections.emptyList();
        }

        List<AuditEvent> auditEvents = new ArrayList<>();

        for (PersistentAuditEvent persistentAuditEvent : persistentAuditEvents) {
            auditEvents.add(convertToAuditEvent(persistentAuditEvent));
        }

        return auditEvents;
    }

    /**
     * Convert to audit event audit event.
     *
     * @param persistentAuditEvent the persistent audit event
     * @return the audit event
     */
    public AuditEvent convertToAuditEvent(PersistentAuditEvent persistentAuditEvent) {
        return new AuditEvent(persistentAuditEvent.getAuditEventDate().toDate().toInstant(), persistentAuditEvent.getPrincipal(), persistentAuditEvent.getAuditEventType(), convertDataToObjects(persistentAuditEvent.getData()));
    }

    /**
     * Convert data to objects map.
     *
     * @param data the data
     * @return the map
     */
    public Map<String, Object> convertDataToObjects(Map<String, String> data) {
        Map<String, Object> results = new HashMap<>();

        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                results.put(entry.getKey(), entry.getValue());
            }
        }

        return results;
    }

    /**
     * Convert data to strings map.
     *
     * @param data the data
     * @return the map
     */
    public Map<String, String> convertDataToStrings(Map<String, Object> data) {
        Map<String, String> results = new HashMap<>();

        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                Object object = entry.getValue();

                // Extract the data that will be saved.
                if (object instanceof WebAuthenticationDetails) {
                    WebAuthenticationDetails authenticationDetails = (WebAuthenticationDetails) object;
                    results.put("remoteAddress", authenticationDetails.getRemoteAddress());
                    results.put("sessionId", authenticationDetails.getSessionId());
                } else if (object != null) {
                    results.put(key, StrUtils.abbreviate(object.toString(), 1000)); // truncated to max size
                                                                                    // of
                                                                                    // persistent_audit_event_data.value
                } else {
                    results.put(key, "null");
                }
            }
        }

        return results;
    }
}
