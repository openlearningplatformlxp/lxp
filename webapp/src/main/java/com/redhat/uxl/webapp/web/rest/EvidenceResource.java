package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEvidenceTypeDTO;
import com.redhat.uxl.services.service.TotaraEvidenceService;
import com.redhat.uxl.webapp.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import java.net.URISyntaxException;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Evidence resource.
 */
@RestController
@RequestMapping(value = "/api/evidence", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class EvidenceResource extends BaseResource {

    @Inject
    private TotaraEvidenceService totaraEvidenceService;

    /**
     * Gets evidence types.
     *
     * @return the evidence types
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Get evidence types", notes = "<p>Get evidence types.</p>")
    @RequestMapping(value = "/types", method = RequestMethod.GET)
    @Timed
    public List<TotaraEvidenceTypeDTO> getEvidenceTypes() throws URISyntaxException {
        log.debug("REST request to get user unmatched user profile interests:");

        return totaraEvidenceService.getEvidenceTypes();
    }

    /**
     * Submit evidence.
     *
     * @param creditId                    the credit id
     * @param name                        the name
     * @param description                 the description
     * @param externalActivityUrl         the external activity url
     * @param externalActivityText        the external activity text
     * @param externalActivityInstitution the external activity institution
     * @param note                        the note
     * @param openInNewWindow             the open in new window
     * @param date                        the date
     * @param file                        the file
     */
    @ApiOperation(value = "Submit evidence", notes = "<p>Submit evidence.</p>")
    @RequestMapping(method = RequestMethod.POST)
    @Timed
    public void submitEvidence(@RequestParam() Long creditId, @RequestParam() String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String externalActivityUrl,
            @RequestParam(required = false) String externalActivityText,
            @RequestParam(required = false) String externalActivityInstitution,
            @RequestParam(required = false) String note, @RequestParam(required = false) boolean openInNewWindow,
            @RequestParam() String date, @RequestParam(required = false, value = "file") MultipartFile file) {

        Long userId = Long.valueOf(SecurityUtils.getCurrentLogin());
        totaraEvidenceService.addEvidence(name, creditId, userId, description, externalActivityUrl,
                externalActivityInstitution, date, file);
    }

}
