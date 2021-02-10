package com.redhat.uxl.services.service;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.utils.solr.StringSolrBuffer;
import com.redhat.uxl.dataobjects.domain.DiscoveryProgram;
import com.redhat.uxl.dataobjects.domain.dto.PersonalLearningPathDTO;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseSetDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.dataobjects.domain.types.DiscoveryProgramType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.dto.AppointmentItemDTO;
import com.redhat.uxl.services.service.dto.CourseSetProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.HtmlBlockDTO;
import com.redhat.uxl.services.service.dto.ProgramItemDTO;
import com.redhat.uxl.services.service.dto.ProgramItemWrapperDTO;
import com.redhat.uxl.services.service.dto.SearchDTO;
import com.redhat.uxl.services.service.dto.SearchInputFiltersDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The interface Program item service.
 */
public interface ProgramItemService {

    /**
     * Gets in progress program items.
     *
     * @param type    the type
     * @param page    the page
     * @param maxSize the max size
     * @param userId  the user id
     * @return the in progress program items
     */
    @Timed
    @Transactional(readOnly = true)
    Page<ProgramItemDTO> getInProgressProgramItems(ProgramType type, int page, int maxSize, Long userId);

    /**
     * Gets discover program items.
     *
     * @param type    the type
     * @param page    the page
     * @param maxSize the max size
     * @return the discover program items
     */
    @Timed
    @Transactional(readOnly = true)
    List<ProgramItemDTO> getDiscoverProgramItems(ProgramType type, int page, int maxSize);

    /**
     * Gets in progress program items wrapper.
     *
     * @param type    the type
     * @param page    the page
     * @param userId  the user id
     * @param maxSize the max size
     * @return the in progress program items wrapper
     */
    @Timed
    @Transactional(readOnly = true)
    ProgramItemWrapperDTO getInProgressProgramItemsWrapper(ProgramType type, int page, Long userId, int maxSize);

    /**
     * Gets personal programs.
     *
     * @param userId  the user id
     * @param manager the manager
     * @return the personal programs
     */
    @Timed
    @Transactional(readOnly = true)
    ProgramItemWrapperDTO getPersonalPrograms(Long userId, TotaraUserDTO manager);

    /**
     * Search items search dto.
     *
     * @param searchTerm   the search term
     * @param inputFilters the input filters
     * @param page         the page
     * @param maxSize      the max size
     * @param userId       the user id
     * @return the search dto
     */
    @Timed
    @Transactional(readOnly = true)
    SearchDTO searchItems(StringSolrBuffer searchTerm, SearchInputFiltersDTO inputFilters, Integer page, int maxSize,
            Long userId);

    /**
     * Find by program id program item dto.
     *
     * @param totaraId  the totara id
     * @param programId the program id
     * @return the program item dto
     */
    ProgramItemDTO findByProgramId(Long totaraId, Long programId);

    /**
     * Find by program id program item dto.
     *
     * @param totaraId       the totara id
     * @param programId      the program id
     * @param registerAccess the register access
     * @return the program item dto
     */
    ProgramItemDTO findByProgramId(Long totaraId, Long programId, boolean registerAccess);

    /**
     * Find html block by program id html block dto.
     *
     * @param programId the program id
     * @return the html block dto
     */
    HtmlBlockDTO findHtmlBlockByProgramId(Long programId);

    /**
     * Find courses by tag page.
     *
     * @param totaraId the totara id
     * @param tagId    the tag id
     * @return the page
     */
    @Timed
    @Transactional()
    Page<ProgramItemDTO> findCoursesByTag(Long totaraId, Long tagId);

    /**
     * Find courses by tag page.
     *
     * @param totaraId the totara id
     * @param tagId    the tag id
     * @param page     the page
     * @param maxSize  the max size
     * @return the page
     */
    @Timed
    @Transactional()
    Page<ProgramItemDTO> findCoursesByTag(Long totaraId, Long tagId, int page, int maxSize);

    /**
     * Find programs by tag page.
     *
     * @param currentUserId the current user id
     * @param tagId         the tag id
     * @return the page
     */
    @Timed
    @Transactional()
    Page<ProgramItemDTO> findProgramsByTag(Long currentUserId, Long tagId);

    /**
     * Find active programs list.
     *
     * @return the list
     */
    @Timed
    @Transactional()
    List<ProgramItemDTO> findActivePrograms();

    /**
     * Determine appointments list.
     *
     * @param personTotaraId the person totara id
     * @param programId      the program id
     * @return the list
     */
    @Timed
    @Transactional()
    List<AppointmentItemDTO> determineAppointments(Long personTotaraId, Long programId);

    /**
     * Determine appointments list.
     *
     * @param programCourseSets the program course sets
     * @return the list
     */
    @Timed
    @Transactional()
    List<AppointmentItemDTO> determineAppointments(List<ProgramCourseSetDTO> programCourseSets);

    /**
     * Gets program progression overview.
     *
     * @param programId the program id
     * @param userId    the user id
     * @return the program progression overview
     */
    @Timed
    @Transactional(readOnly = true)
    List<CourseSetProgressionOverviewDTO> getProgramProgressionOverview(Long programId, Long userId);

    /**
     * Gets discovery programs.
     *
     * @return the discovery programs
     */
    @Timed
    @Transactional(readOnly = true)
    List<DiscoveryProgram> getDiscoveryPrograms();

    /**
     * Upsert discovery program discovery program.
     *
     * @param id                   the id
     * @param programId            the program id
     * @param active               the active
     * @param type                 the type
     * @param discoveryProgramText the discovery program text
     * @return the discovery program
     */
    @Timed
    @Transactional()
    DiscoveryProgram upsertDiscoveryProgram(Long id, Long programId, Boolean active, DiscoveryProgramType type,
            String discoveryProgramText);

    /**
     * Delete discovery program.
     *
     * @param discoveryProgramId the discovery program id
     */
    @Timed
    @Transactional()
    void deleteDiscoveryProgram(Long discoveryProgramId);

    /**
     * Find allowed user audience by user id long.
     *
     * @param personTotaraId the person totara id
     * @param programId      the program id
     * @return the long
     */
    @Timed
    @Transactional(readOnly = true)
    Long findAllowedUserAudienceByUserId(Long personTotaraId, Long programId);

    /**
     * Create personal program personal learning path dto.
     *
     * @param personTotaraId       the person totara id
     * @param personalLearningPath the personal learning path
     * @return the personal learning path dto
     */
    @Timed
    @Transactional()
    PersonalLearningPathDTO createPersonalProgram(Long personTotaraId, PersonalLearningPathDTO personalLearningPath);

    /**
     * Update personal program personal learning path dto.
     *
     * @param personTotaraId       the person totara id
     * @param personalLearningPath the personal learning path
     * @return the personal learning path dto
     */
    @Timed
    @Transactional()
    PersonalLearningPathDTO updatePersonalProgram(Long personTotaraId, PersonalLearningPathDTO personalLearningPath);

    /**
     * Gets in progress personal program items wrapper.
     *
     * @param userId  the user id
     * @param manager the manager
     * @param maxSize the max size
     * @return the in progress personal program items wrapper
     */
    @Timed
    @Transactional(readOnly = true)
    ProgramItemWrapperDTO getInProgressPersonalProgramItemsWrapper(Long userId, TotaraUserDTO manager, int maxSize);

    /**
     * Update personal plan manual completion.
     *
     * @param itemId         the item id
     * @param status         the status
     * @param personTotaraId the person totara id
     */
    @Timed
    @Transactional()
    void updatePersonalPlanManualCompletion(Long itemId, Long status, Long personTotaraId);
}
