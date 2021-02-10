package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.dataobjects.domain.AbstractAuditingEntity;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * The type Auditing dto.
 */
@Data
public class AuditingDTO {
    private String createdBy;
    private DateTime createdDate;
    private String lastModifiedBy;
    private DateTime lastModifiedDate;

    /**
     * Sets auditing.
     *
     * @param auditingEntity the auditing entity
     */
    public void setAuditing(AbstractAuditingEntity auditingEntity) {
        setCreatedBy(auditingEntity.getCreatedBy());
        setCreatedDate(auditingEntity.getCreatedDate());
        setLastModifiedBy(auditingEntity.getLastModifiedBy());
        setLastModifiedDate(auditingEntity.getLastModifiedDate());
    }
}
