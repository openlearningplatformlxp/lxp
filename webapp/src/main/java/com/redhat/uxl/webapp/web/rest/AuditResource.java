package com.redhat.uxl.webapp.web.rest;

import com.redhat.uxl.webapp.service.AuditEventService;
import com.redhat.uxl.webapp.web.propertyeditors.LocaleDateTimeEditor;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.inject.Inject;
import org.joda.time.LocalDateTime;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Audit resource.
 */
@RestController
@RequestMapping(value = "/api/audits", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditResource extends BaseResource {

    private AuditEventService auditEventService;

    /**
     * Instantiates a new Audit resource.
     *
     * @param auditEventService the audit event service
     */
    @Inject
    public AuditResource(AuditEventService auditEventService) {
        this.auditEventService = auditEventService;
    }

    /**
     * Init binder.
     *
     * @param binder the binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new LocaleDateTimeEditor("yyyy-MM-dd", false));
    }

    /**
     * Gets all.
     *
     * @return the all
     */
    @ApiOperation(value = "Get list of all Audit events.", notes = "<p>Get list of all Audit events.</p>")
    @RequestMapping(method = RequestMethod.GET)
    public List<AuditEvent> getAll() {
        return auditEventService.findAll();
    }

    /**
     * Gets by dates.
     *
     * @param fromDate the from date
     * @param toDate   the to date
     * @return the by dates
     */
    @ApiOperation(value = "Get list of Audit events between the given dates.", notes = "<p>Get list of Audit events between the given dates.</p>")
    @RequestMapping(method = RequestMethod.GET, params = { "fromDate", "toDate" })
    public List<AuditEvent> getByDates(@RequestParam(value = "fromDate") LocalDateTime fromDate,
            @RequestParam(value = "toDate") LocalDateTime toDate) {
        return auditEventService.findByDates(fromDate, toDate);
    }

    /**
     * Get response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    public ResponseEntity<AuditEvent> get(@PathVariable Long id) {
        AuditEvent event = auditEventService.find(id);
        if (event != null) {
            return new ResponseEntity<AuditEvent>(event, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
