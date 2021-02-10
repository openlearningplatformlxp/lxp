package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.datalayer.dao.TotaraTagDAO;
import com.redhat.uxl.datalayer.sql.totara.TotaraTagSQL;
import com.redhat.uxl.datalayer.utils.JodaBeanPropertyRowMapper;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * The type Totara tag dao.
 */
@Service
@ConditionalOnExpression("!${demo.mode.enabled:false}")
public class TotaraTagDAOImpl implements TotaraTagDAO {

    @Inject
    private JdbcTemplate totaraJdbcTemplate;

    private final static String FIRST_TOPIC_PARENT_TAG_NAME = "Topic";
    private final static String SKILL_LEVEL_PARENT_TAG_NAME = "Skill level";
    private final static String LANGUAGE_PARENT_TAG_NAME = "Language";
    private final static String ROLE_PARENT_TAG_NAME = "Role";


    @Override
    public List<TotaraTagDTO> findUnmatchedWikiRoleTags(Long wikiId, String searchTerm, int max) {
        if (wikiId == null) {
            wikiId = -1l;
        }
        if (searchTerm == null) {
            searchTerm = "%";
        } else {
            searchTerm = StringUtils.lowerCase(searchTerm) + "%";
        }
        return totaraJdbcTemplate.query(TotaraTagSQL.UNMATCHED_WIKI_TAGS, new Object[] { wikiId, searchTerm, max },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public List<TotaraTagDTO> findWikiTags(Long wikiId) {
        return totaraJdbcTemplate.query(TotaraTagSQL.ALL_WIKI_TAGS, new Object[] { wikiId },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public List<TotaraTagDTO> findRoleTags() {
        // return new ArrayList<>();//TODO: Implement Me
        return totaraJdbcTemplate.query(TotaraTagSQL.TAGS_ROLES, new Object[] { ROLE_PARENT_TAG_NAME },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public List<TotaraTagDTO> findUnmatchedUserTags(Long profileId, String searchTerm, int max) {
        if (searchTerm == null) {
            searchTerm = "%";
        } else {
            searchTerm = StringUtils.lowerCase(searchTerm) + "%";
        }
        return totaraJdbcTemplate.query(TotaraTagSQL.UNMATCHED_USER_TAGS, new Object[] { profileId, searchTerm, max },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
        // return new ArrayList<>(); //TODO: Implement Me
    }

    @Override
    public List<TotaraTagDTO> findAllUserTags(Long profileId) {
        // return new ArrayList<>(); //TODO: Implement Me
        return totaraJdbcTemplate.query(TotaraTagSQL.ALL_USER_TAGS, new Object[] { profileId },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public List<TotaraTagDTO> findTopicTags() {
        return totaraJdbcTemplate.query(TotaraTagSQL.SQL_SELECT_MDL_TAGS_ORDERED, new Object[] { FIRST_TOPIC_PARENT_TAG_NAME },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public List<TotaraTagDTO> findSkillLevelTags() {
        // TODO: Implement Me
        // return new ArrayList<>();
        return totaraJdbcTemplate.query(TotaraTagSQL.SQL_SELECT_MDL_TAGS, new Object[] { SKILL_LEVEL_PARENT_TAG_NAME },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public List<TotaraTagDTO> findLanguageTags() {
        // TODO: Implement Me
        // return new ArrayList<>();
        return totaraJdbcTemplate.query(TotaraTagSQL.SQL_SELECT_MDL_TAGS, new Object[] { LANGUAGE_PARENT_TAG_NAME },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public List<TotaraTagDTO> findTagsForCourse(Long courseId) {
        String query = TotaraTagSQL.SQL_SELECT_MDL_TAGS_OF_ITEM.replace(":itemType", "course");
        return totaraJdbcTemplate.query(query, new Object[] { courseId },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public List<TotaraTagDTO> findTagsForProgram(Long programId) {
        String query = TotaraTagSQL.SQL_SELECT_MDL_TAGS_OF_ITEM.replace(":itemType", "program");
        return totaraJdbcTemplate.query(query, new Object[] { programId },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public List<TotaraTagDTO> findTagsForProgramWithParent(Long programId) {
        return totaraJdbcTemplate.query(TotaraTagSQL.SQL_SELECT_PROGRAM_PARENT_TAGS, new Object[] { programId },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public List<TotaraTagDTO> findTagsForCourseWithParent(Long programId) {
        return totaraJdbcTemplate.query(TotaraTagSQL.SQL_SELECT_COURSE_PARENT_TAGS, new Object[] { programId },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public TotaraTagDTO findCourseSkillLevel(Long courseId) {
        try {
            return totaraJdbcTemplate.queryForObject(TotaraTagSQL.SQL_SELECT_TAG_OF_TYPE,
                    new Object[] { SKILL_LEVEL_PARENT_TAG_NAME, courseId, "course" },
                    new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public TotaraTagDTO findLearningPathSkillLevel(Long learningPathId) {
        try {
            return totaraJdbcTemplate.queryForObject(TotaraTagSQL.SQL_SELECT_TAG_OF_TYPE,
                    new Object[] { SKILL_LEVEL_PARENT_TAG_NAME, learningPathId, "program" },
                    new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public TotaraTagDTO findCourseLanguage(Long courseId) {
        try {
            return totaraJdbcTemplate.queryForObject(TotaraTagSQL.SQL_SELECT_TAG_OF_TYPE,
                    new Object[] { LANGUAGE_PARENT_TAG_NAME, courseId, "course" },
                    new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public TotaraTagDTO findLearningPathLanguage(Long learningPathId) {
        try {
            return totaraJdbcTemplate.queryForObject(TotaraTagSQL.SQL_SELECT_TAG_OF_TYPE,
                    new Object[] { LANGUAGE_PARENT_TAG_NAME, learningPathId, "program" },
                    new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public TotaraTagDTO findCourseFirstTopic(Long courseId) {
        try {
            return totaraJdbcTemplate.queryForObject(TotaraTagSQL.SQL_SELECT_TAG_OF_TYPE,
                    new Object[] { FIRST_TOPIC_PARENT_TAG_NAME, courseId, "course" },
                    new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<TotaraTagDTO> findCourseFirstTopic(List<Long> itemIdList) {
        NamedParameterJdbcTemplate namedJDBCTemplate = new NamedParameterJdbcTemplate(totaraJdbcTemplate);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("parentTag", FIRST_TOPIC_PARENT_TAG_NAME);
        parameters.addValue("itemId", itemIdList);
        parameters.addValue("itemType", "course");

        return namedJDBCTemplate.query(TotaraTagSQL.SQL_SELECT_TAG_OF_TYPE_FROM_LIST, parameters,
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));

    }

    @Override
    public TotaraTagDTO findLearningPathFirstTopic(Long learningPathId) {
        try {
            return totaraJdbcTemplate.queryForObject(TotaraTagSQL.SQL_SELECT_TAG_OF_TYPE,
                    new Object[] { FIRST_TOPIC_PARENT_TAG_NAME, learningPathId, "program" },
                    new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public TotaraTagDTO findTag(Long tagId) {
        try {
            return totaraJdbcTemplate.queryForObject(TotaraTagSQL.SQL_SELECT_MDL_TAG_BY_ID, new Object[] { tagId },
                    new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<TotaraTagDTO> findParentTags() {
        return totaraJdbcTemplate.query(TotaraTagSQL.SQL_SELECT_MDL_TAG_PARENT, new Object[] {},
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public List<TotaraTagDTO> findChildTags(Long parentTagId) {
        return totaraJdbcTemplate.query(TotaraTagSQL.SQL_SELECT_MDL_TAG_CHILD, new Object[] { parentTagId },
                new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
    }

    @Override
    public TotaraTagDTO findParentTag(Long tagId) {
        try {
            return totaraJdbcTemplate.queryForObject(TotaraTagSQL.SQL_SELECT_MDL_TAG_PARENT_BY_ID, new Object[] { tagId },
                    new JodaBeanPropertyRowMapper<>(TotaraTagDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
