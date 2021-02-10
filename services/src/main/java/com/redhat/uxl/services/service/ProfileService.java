package com.redhat.uxl.services.service;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.services.service.dto.AchievementDTO;
import com.redhat.uxl.services.service.dto.ProfileFullDTO;
import com.redhat.uxl.services.service.dto.ProfilePreferencesDTO;
import com.redhat.uxl.services.service.dto.ProfileProgressInfoDTO;
import com.redhat.uxl.services.service.dto.TagDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * The interface Profile service.
 */
public interface ProfileService {
    /**
     * Gets totara user profile.
     *
     * @param email the email
     * @return the totara user profile
     */
    TotaraUserDTO getTotaraUserProfile(String email);

    /**
     * Gets first manager.
     *
     * @param totaraId the totara id
     * @return the first manager
     */
    @Timed
    @Transactional(readOnly = true) TotaraUserDTO getFirstManager(Long totaraId);

    /**
     * Gets logged user profile.
     *
     * @param totaraId the totara id
     * @return the logged user profile
     */
    @Timed
    @Transactional(readOnly = true)
    ProfileFullDTO getLoggedUserProfile(Long totaraId);

    /**
     * Gets logged user profile progress info.
     *
     * @param totaraId the totara id
     * @return the logged user profile progress info
     */
    @Timed
    @Transactional(readOnly = true)
    ProfileProgressInfoDTO getLoggedUserProfileProgressInfo(Long totaraId);

    /**
     * Gets logged user profile preferences.
     *
     * @param totaraId the totara id
     * @return the logged user profile preferences
     */
    @Timed
    @Transactional(readOnly = true)
    ProfilePreferencesDTO getLoggedUserProfilePreferences(Long totaraId);

    /**
     * Gets logged user interests.
     *
     * @param totaraId the totara id
     * @return the logged user interests
     */
    @Timed
    @Transactional(readOnly = true)
    List<TagDTO> getLoggedUserInterests(Long totaraId);

    /**
     * Gets logged user roles.
     *
     * @param totaraId the totara id
     * @return the logged user roles
     */
    @Timed
    @Transactional(readOnly = true)
    List<TagDTO> getLoggedUserRoles(Long totaraId);

    /**
     * Gets profile roles.
     *
     * @param totaraId the totara id
     * @return the profile roles
     */
    @Timed
    @Transactional(readOnly = true)
    List<TagDTO> getProfileRoles(Long totaraId);

    /**
     * Add profile role list.
     *
     * @param totaraId the totara id
     * @param tagDTO   the tag dto
     * @return the list
     */
    List<TagDTO> addProfileRole(long totaraId, TagDTO tagDTO);

    /**
     * Gets logged user unmatched profile interests.
     *
     * @param totaraId   the totara id
     * @param searchTerm the search term
     * @return the logged user unmatched profile interests
     */
    @Timed
    @Transactional(readOnly = true)
    List<TagDTO> getLoggedUserUnmatchedProfileInterests(Long totaraId, String searchTerm);

    /**
     * Gets all profile interests.
     *
     * @param totaraId the totara id
     * @return the all profile interests
     */
    @Timed
    @Transactional(readOnly = true)
    List<TagDTO> getAllProfileInterests(Long totaraId);

    /**
     * Add profile interest list.
     *
     * @param totaraId the totara id
     * @param tagDTO   the tag dto
     * @return the list
     */
    @Timed
    @Transactional()
    List<TagDTO> addProfileInterest(long totaraId, TagDTO tagDTO);

    /**
     * Remove profile interest.
     *
     * @param totaraId the totara id
     * @param tagId    the tag id
     */
    @Timed
    @Transactional()
    void removeProfileInterest(long totaraId, long tagId);

    /**
     * Remove profile role.
     *
     * @param totaraId the totara id
     * @param tagId    the tag id
     */
    @Timed
    @Transactional()
    void removeProfileRole(long totaraId, long tagId);

    /**
     * Upload file.
     *
     * @param currentUserId the current user id
     * @param file          the file
     */
    @Timed
    @Transactional()
    void uploadFile(Long currentUserId, MultipartFile file);
}
