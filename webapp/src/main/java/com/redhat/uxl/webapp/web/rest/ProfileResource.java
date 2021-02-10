package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.ProfileService;
import com.redhat.uxl.services.service.dto.ProfileFullDTO;
import com.redhat.uxl.services.service.dto.ProfilePreferencesDTO;
import com.redhat.uxl.services.service.dto.ProfileProgressInfoDTO;
import com.redhat.uxl.services.service.dto.TagDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import java.net.URISyntaxException;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Profile resource.
 */
@RestController
@RequestMapping(value = "/api/profile", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProfileResource extends BaseResource {

    @Inject
    private ProfileService profile;

    /**
     * Gets user profile.
     *
     * @return the user profile
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Get user profile", notes = "<p>Get current logged user profile.</p>")
    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public ProfileFullDTO getUserProfile() throws URISyntaxException {
        log.debug("REST request to get user profile:");
        log.debug("......user..." + SecurityUtils.getCurrentLogin());
        return profile.getLoggedUserProfile(SecurityUtils.getCurrentLoginAsLong());
    }

    /**
     * Gets user profile progress info.
     *
     * @return the user profile progress info
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Get user profile progress info", notes = "<p>Get current logged user profile progress info.</p>")
    @RequestMapping(value = "/progress", method = RequestMethod.GET)
    @Timed
    public ProfileProgressInfoDTO getUserProfileProgressInfo() throws URISyntaxException {
        log.debug("REST request to get user profile progress info:");

        return profile.getLoggedUserProfileProgressInfo(SecurityUtils.getCurrentLoginAsLong());
    }

    /**
     * Gets user profile preferences.
     *
     * @return the user profile preferences
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Get user profile preferences", notes = "<p>Get current logged user profile preferences.</p>")
    @RequestMapping(value = "/preferences", method = RequestMethod.GET)
    @Timed
    public ProfilePreferencesDTO getUserProfilePreferences() throws URISyntaxException {
        log.debug("REST request to get user preferences:");

        return profile.getLoggedUserProfilePreferences(SecurityUtils.getCurrentLoginAsLong());
    }

    /**
     * Add profile role list.
     *
     * @param tagDTO the tag dto
     * @return the list
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Add profile role", notes = "<p>Add profile role.</p>")
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    @Timed
    public List<TagDTO> addProfileRole(@RequestBody TagDTO tagDTO) throws URISyntaxException {
        log.debug("REST request to add user profile role:");

        return profile.addProfileRole(SecurityUtils.getCurrentLoginAsLong(), tagDTO);
    }

    /**
     * Gets user unmatched profile interests.
     *
     * @param searchTerm the search term
     * @return the user unmatched profile interests
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Get unmatched profile interests", notes = "<p>Get unmatched profile interests.</p>")
    @RequestMapping(value = "/interests", method = RequestMethod.GET)
    @Timed
    public List<TagDTO> getUserUnmatchedProfileInterests(@RequestParam(required = false, value = "q") String searchTerm)
            throws URISyntaxException {
        log.debug("REST request to get user unmatched user profile interests:");
        if (searchTerm == null) {
            return profile.getAllProfileInterests(SecurityUtils.getCurrentLoginAsLong());
        }
        return profile.getLoggedUserUnmatchedProfileInterests(SecurityUtils.getCurrentLoginAsLong(), searchTerm);
    }

    /**
     * Add profile interests list.
     *
     * @param tagDTO the tag dto
     * @return the list
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Add profile interest", notes = "<p>Add profile interests.</p>")
    @RequestMapping(value = "/interests", method = RequestMethod.POST)
    @Timed
    public List<TagDTO> addProfileInterests(@RequestBody TagDTO tagDTO) throws URISyntaxException {
        log.debug("REST request to add user profile interest:");

        return profile.addProfileInterest(SecurityUtils.getCurrentLoginAsLong(), tagDTO);
    }

    /**
     * Remove profile interests.
     *
     * @param tagId the tag id
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Delete profile interest", notes = "<p>Delete profile interests.</p>")
    @RequestMapping(value = "/interests/{id}", method = RequestMethod.DELETE)
    @Timed
    public void removeProfileInterests(@PathVariable("id") Long tagId) throws URISyntaxException {
        log.debug("REST request to remove user profile interest:" + tagId);

        profile.removeProfileInterest(SecurityUtils.getCurrentLoginAsLong(), tagId);
    }

    /**
     * Remove profile role.
     *
     * @param tagId the tag id
     * @throws URISyntaxException the uri syntax exception
     */
    @ApiOperation(value = "Delete profile role", notes = "<p>Delete profile role.</p>")
    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
    @Timed
    public void removeProfileRole(@PathVariable("id") Long tagId) throws URISyntaxException {
        log.debug("REST request to remove user profile role:" + tagId);

        profile.removeProfileRole(SecurityUtils.getCurrentLoginAsLong(), tagId);
    }

    /**
     * Upload profile image.
     *
     * @param file     the file
     * @param response the response
     */
    @ApiOperation(value = "Upload profile image", notes = "<p>Upload Profile Image.</p>")
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    @Timed
    public void uploadProfileImage(@RequestParam(required = false) MultipartFile file, HttpServletResponse response) {
        profile.uploadFile(SecurityUtils.getCurrentLoginAsLong(), file);
    }

}
