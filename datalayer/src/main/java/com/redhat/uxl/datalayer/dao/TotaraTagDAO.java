package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import java.util.List;

/**
 * The interface Totara tag dao.
 */
public interface TotaraTagDAO {

    /**
     * Find unmatched wiki role tags list.
     *
     * @param wikiId     the wiki id
     * @param searchTerm the search term
     * @param max        the max
     * @return the list
     */
    List<TotaraTagDTO> findUnmatchedWikiRoleTags(Long wikiId, String searchTerm, int max);

    /**
     * Find wiki tags list.
     *
     * @param id the id
     * @return the list
     */
    List<TotaraTagDTO> findWikiTags(Long id);

    /**
     * Find role tags list.
     *
     * @return the list
     */
    List<TotaraTagDTO> findRoleTags();

    /**
     * Find unmatched user tags list.
     *
     * @param profileId  the profile id
     * @param searchTerm the search term
     * @param max        the max
     * @return the list
     */
    List<TotaraTagDTO> findUnmatchedUserTags(Long profileId, String searchTerm, int max);

    /**
     * Find all user tags list.
     *
     * @param profileId the profile id
     * @return the list
     */
    List<TotaraTagDTO> findAllUserTags(Long profileId);

    /**
     * Find topic tags list.
     *
     * @return the list
     */
    List<TotaraTagDTO> findTopicTags();

    /**
     * Find skill level tags list.
     *
     * @return the list
     */
    List<TotaraTagDTO> findSkillLevelTags();

    /**
     * Find language tags list.
     *
     * @return the list
     */
    List<TotaraTagDTO> findLanguageTags();

    /**
     * Find tags for course list.
     *
     * @param courseId the course id
     * @return the list
     */
    List<TotaraTagDTO> findTagsForCourse(Long courseId);

    /**
     * Find tags for program list.
     *
     * @param programId the program id
     * @return the list
     */
    List<TotaraTagDTO> findTagsForProgram(Long programId);

    /**
     * Find tags for program with parent list.
     *
     * @param programId the program id
     * @return the list
     */
    List<TotaraTagDTO> findTagsForProgramWithParent(Long programId);

    /**
     * Find course skill level totara tag dto.
     *
     * @param courseId the course id
     * @return the totara tag dto
     */
    TotaraTagDTO findCourseSkillLevel(Long courseId);

    /**
     * Find learning path skill level totara tag dto.
     *
     * @param learningPathId the learning path id
     * @return the totara tag dto
     */
    TotaraTagDTO findLearningPathSkillLevel(Long learningPathId);

    /**
     * Find tags for course with parent list.
     *
     * @param programId the program id
     * @return the list
     */
    List<TotaraTagDTO> findTagsForCourseWithParent(Long programId);

    /**
     * Find course language totara tag dto.
     *
     * @param courseId the course id
     * @return the totara tag dto
     */
    TotaraTagDTO findCourseLanguage(Long courseId);

    /**
     * Find learning path language totara tag dto.
     *
     * @param learningPathId the learning path id
     * @return the totara tag dto
     */
    TotaraTagDTO findLearningPathLanguage(Long learningPathId);

    /**
     * Find course first topic totara tag dto.
     *
     * @param courseId the course id
     * @return the totara tag dto
     */
    TotaraTagDTO findCourseFirstTopic(Long courseId);

    /**
     * Find course first topic list.
     *
     * @param courseId the course id
     * @return the list
     */
    List<TotaraTagDTO> findCourseFirstTopic(List<Long> courseId);

    /**
     * Find learning path first topic totara tag dto.
     *
     * @param programId the program id
     * @return the totara tag dto
     */
    TotaraTagDTO findLearningPathFirstTopic(Long programId);

    /**
     * Find tag totara tag dto.
     *
     * @param tagId the tag id
     * @return the totara tag dto
     */
    TotaraTagDTO findTag(Long tagId);

    /**
     * Find parent tags list.
     *
     * @return the list
     */
    List<TotaraTagDTO> findParentTags();

    /**
     * Find child tags list.
     *
     * @param parentTagId the parent tag id
     * @return the list
     */
    List<TotaraTagDTO> findChildTags(Long parentTagId);

    /**
     * Find parent tag totara tag dto.
     *
     * @param tagId the tag id
     * @return the totara tag dto
     */
    TotaraTagDTO findParentTag(Long tagId);

}
