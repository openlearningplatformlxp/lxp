package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.DiscoveryProgram;
import com.redhat.uxl.dataobjects.domain.types.DiscoveryProgramType;
import com.redhat.uxl.services.service.ProgramItemService;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import com.redhat.uxl.webapp.web.rest.dto.DiscoverProgramDTO;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * The type Admin discovery resource.
 */
@RestController
@RequestMapping(value = "/api/admin/discovery", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDiscoveryResource extends BaseResource {
    @Inject
    private ProgramItemService programItemService;

    /**
     * Gets discovery programs.
     *
     * @return the discovery programs
     */
    @ApiOperation(value = "Get DiscoveryPrograms.", notes = "<p>Get DiscoveryPrograms.</p>")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    List<DiscoverProgramDTO> getDiscoveryPrograms() {
        List<DiscoveryProgram> discoveryProgramList = programItemService.getDiscoveryPrograms();
        List<DiscoverProgramDTO> dto = new ArrayList<>();
        for (DiscoveryProgram dp : discoveryProgramList) {
            dto.add(DiscoverProgramDTO.valueOf(dp));
        }
        return dto;
    }

    /**
     * Upsert discovery program discover program dto.
     *
     * @param dto the dto
     * @return the discover program dto
     */
    @ApiOperation(value = "Upsert DiscoveryProgram.", notes = "<p>Upsert DiscoveryProgram.</p>")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    DiscoverProgramDTO upsertDiscoveryProgram(@RequestBody DiscoverProgramDTO dto) {
        DiscoveryProgram dp = programItemService.upsertDiscoveryProgram(dto.getId(), dto.getProgramId(),
                dto.getActive(), dto.getType(), dto.getDiscoveryProgramText());
        return DiscoverProgramDTO.valueOf(dp);

    }

    /**
     * Delete discovery program.
     *
     * @param discoveryProgramId the discovery program id
     */
    @ApiOperation(value = "Delete the given DiscoveryProgram.", notes = "<p>Delete the given DiscoveryProgram.</p>")
    @RequestMapping(value = "/{discoveryProgramId}", method = RequestMethod.DELETE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public void deleteDiscoveryProgram(@PathVariable Long discoveryProgramId) {
        programItemService.deleteDiscoveryProgram(discoveryProgramId);
    }

    /**
     * Gets discover type list.
     *
     * @return the discover type list
     */
    @ApiOperation(value = "Get discover types", notes = "<p>Get list of discover types.</p>")
    @RequestMapping(value = "/types", method = RequestMethod.GET)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public List<DiscoveryProgramType> getDiscoverTypeList() {
        return DiscoveryProgramType.getAllTypes();
    }
}
