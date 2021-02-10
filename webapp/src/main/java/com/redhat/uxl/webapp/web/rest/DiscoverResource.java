package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.DatabaseSettingsService;
import com.redhat.uxl.services.service.ProfileService;
import com.redhat.uxl.services.service.ProgramItemService;
import com.redhat.uxl.services.service.dto.ProgramItemDTO;
import com.redhat.uxl.services.service.dto.TagDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.dto.DiscoverInterestDTO;
import com.redhat.uxl.webapp.web.rest.dto.DiscoverWrapperDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Discover resource.
 */
@RestController
@RequestMapping(value = "/api/discover", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DiscoverResource extends BaseResource {

    /**
     * The Database settings service.
     */
    @Inject
    DatabaseSettingsService databaseSettingsService;
    /**
     * The Program item service.
     */
    @Inject
    ProgramItemService programItemService;
    /**
     * The Profile service.
     */
    @Inject
    ProfileService profileService;

    /**
     * Gets discover data.
     *
     * @return the discover data
     */
    @ApiOperation(value = "Get discover data", notes = "<p>Get info for discover page.</p>")
    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public DiscoverWrapperDTO getDiscoverData() {
        log.debug("REST request to get discover data");
        Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
        DiscoverWrapperDTO wrapperDTO = new DiscoverWrapperDTO();

        // Get carousel
        int maxDiscoverSettings = databaseSettingsService
                .findIntValue(DatabaseSettingsService.Constants.DISCOVER_ELEMENTS_KEY);
        List<ProgramItemDTO> discoverPrograms = programItemService.getDiscoverProgramItems(ProgramType.LEARNING_PATH, 0,
                maxDiscoverSettings);
        wrapperDTO.setDiscoverPrograms(discoverPrograms);

        // Get because you are interested in tags stuff...
        wrapperDTO.getInterests().addAll(profileService.getLoggedUserInterests(currentUserId).stream()
                .map(new Function<TagDTO, DiscoverInterestDTO>() {
                    @Override
                    public DiscoverInterestDTO apply(TagDTO tagDTO) {
                        DiscoverInterestDTO interestDTO = new DiscoverInterestDTO();

                        interestDTO.setTag(tagDTO);
                        interestDTO.setPrograms(programItemService.findCoursesByTag(currentUserId, tagDTO.getId()));

                        return interestDTO;
                    }
                }).collect(Collectors.toList()));

        // Get because you have the roles stuff...
        wrapperDTO.getRoles().addAll(profileService.getLoggedUserRoles(currentUserId).stream()
                .map(new Function<TagDTO, DiscoverInterestDTO>() {
                    @Override
                    public DiscoverInterestDTO apply(TagDTO tagDTO) {
                        DiscoverInterestDTO interestDTO = new DiscoverInterestDTO();

                        interestDTO.setTag(tagDTO);
                        interestDTO.setPrograms(programItemService.findCoursesByTag(currentUserId, tagDTO.getId()));

                        return interestDTO;
                    }
                }).collect(Collectors.toList()));

        return wrapperDTO;
    }

}
