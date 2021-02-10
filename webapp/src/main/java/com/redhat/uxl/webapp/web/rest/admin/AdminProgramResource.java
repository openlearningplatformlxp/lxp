package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.ProgramItemService;
import com.redhat.uxl.services.service.dto.ProgramItemDTO;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Admin program resource.
 */
@RestController
@RequestMapping(value = "/api/admin/programs", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminProgramResource extends BaseResource {
    @Inject
    private ProgramItemService programItemService;

    /**
     * Gets programs.
     *
     * @return the programs
     */
    @ApiOperation(value = "Get Programs.", notes = "<p>Get Programs.</p>")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    List<ProgramItemDTO> getPrograms() {
        return programItemService.findActivePrograms();
    }

}
