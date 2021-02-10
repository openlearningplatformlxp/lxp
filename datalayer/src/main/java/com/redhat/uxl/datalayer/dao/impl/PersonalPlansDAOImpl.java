package com.redhat.uxl.datalayer.dao.impl;

import com.redhat.uxl.commonjava.errors.exception.GeneralErrorCode;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.datalayer.dao.PersonalPlansDAO;
import com.redhat.uxl.datalayer.sql.PersonalPlansSQL;
import com.redhat.uxl.datalayer.utils.JodaBeanPropertyRowMapper;
import com.redhat.uxl.dataobjects.domain.ProgramSectionCourse;
import com.redhat.uxl.dataobjects.domain.dto.PersonalLearningPathDTO;
import com.redhat.uxl.dataobjects.domain.dto.PersonalProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseSetDTO;
import com.redhat.uxl.dataobjects.domain.types.TotaraCourseSetNextSetOperatorType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * The type Personal plans dao.
 */
@Service
public class PersonalPlansDAOImpl implements PersonalPlansDAO {

  @Inject
  private JdbcTemplate jdbcTemplate;

  @Override
  public PersonalLearningPathDTO getPersonalPlanForUser(Long userId, Long pathId) {
    try {
      PersonalLearningPathDTO program = jdbcTemplate.queryForObject(PersonalPlansSQL.SQL_SELECT_PLAN_FOR_USER,
          new Object[] {userId, userId, userId, pathId},
          new JodaBeanPropertyRowMapper<>(PersonalLearningPathDTO.class));
      // Check for course sets
      program.setCourseSets(getCourseSetsForPersonalPlan(pathId));
      return program;
    } catch (EmptyResultDataAccessException e) {
      throw new GeneralException(GeneralErrorCode.ENTITY_NOT_FOUND);
    }
  }

  @Override
  public void setPersonalPlanManualCompletion(Long itemId, Long status, Long userId) {
    // First try update
    Long manualId = jdbcTemplate.queryForObject(PersonalPlansSQL.SQL_SELECT_COMPLETION_EXISTS, new Object[] {itemId},
        Long.class);
    if (manualId != null && manualId > 0) {
      jdbcTemplate.update(PersonalPlansSQL.SQL_UPDATE_COMPLETION_MANUAL, new Object[] {status, itemId});
    } else {
      // Then try insert
      jdbcTemplate.queryForObject(PersonalPlansSQL.SQL_INSERT_COMPLETION_MANUAL,
          new Object[] {status, userId, itemId}, Long.class);
    }
  }

  @Override
  public Long getPersonalPlanManualCompletion(Long itemId, Long userId) {
    try {
      return jdbcTemplate.queryForObject(PersonalPlansSQL.SQL_SELECT_COMPLETION_MANUAL,
          new Object[] {itemId, userId}, Long.class);
    } catch (Exception e) {
      return new Long(0);
    }
  }

  @Override
  public Long findPlanSectionCourseId(Long userId, Long programId, Long sectionId, Long courseId) {
    try {
      return jdbcTemplate.queryForObject(PersonalPlansSQL.SQL_SELECT_PLAN_SECTION_COURSE_ID,
          new Object[] {sectionId, programId, userId, courseId}, Long.class);
    } catch (Exception e) {
      return new Long(0);
    }
  }

  @Override
  public List<ProgramCourseSetDTO> getCourseSetsForPersonalPlan(Long pathId) {
    List<ProgramCourseSetDTO> sets = jdbcTemplate.query(PersonalPlansSQL.SQL_SELECT_SECTION_FOR_PLAN,
        new Object[] {pathId}, new JodaBeanPropertyRowMapper<>(ProgramCourseSetDTO.class));
    sets.stream().forEach(
        cs -> cs.setNextSetOperator(TotaraCourseSetNextSetOperatorType.NEXTSETOPERATOR_AND));
    List<Long> sectionIds = sets.stream().map(cs -> cs.getId()).collect(Collectors.toList());
    NamedParameterJdbcTemplate namedJDBCTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue("ids", sectionIds);
    List<ProgramSectionCourse> courses =
        namedJDBCTemplate.query(PersonalPlansSQL.SQL_SELECT_COURSES_FOR_SECTION_FOR_PLAN, parameters,
            new JodaBeanPropertyRowMapper<>(ProgramSectionCourse.class));
    sets.stream().forEach(cs -> {
      courses.stream().filter(c -> c.getSectionId().equals(cs.getId())).forEach(c -> {
        if (cs.getCourses() == null) {
          cs.setCourses(new ArrayList<>());
        }
        cs.getCourses().add(new ProgramCourseDTO(c));
      });
    });
    if (!sets.isEmpty()) {
      sets.get(sets.size() - 1).setNextSetOperator(null);
    }
    return sets;
  }

  @Override
  public void updatePlanForUser(Long userId, PersonalLearningPathDTO personalLearningPathDTO) {

    jdbcTemplate.update(PersonalPlansSQL.SQL_UPDATE_PLAN_FOR_USER,
        new Object[] {personalLearningPathDTO.getTitle(), personalLearningPathDTO.getDescription(),
            personalLearningPathDTO.getDueDate(), userId, personalLearningPathDTO.getId()});
    List<ProgramCourseSetDTO> sets = personalLearningPathDTO.getCourseSets();

    if (sets != null) {
      for (ProgramCourseSetDTO set : sets) {
        // This is where we would insert the courses
        if (set.getId() == null) {
          Long sectionId = jdbcTemplate.queryForObject(
              PersonalPlansSQL.SQL_INSERT_PLAN_SECTION_FOR_USER, new Object[] {personalLearningPathDTO.getId(),
                  set.getName(), set.getSummary(), set.getDueDate(), set.getSortOrder()},
              Long.class);
          set.setId(sectionId);
        } else {
          jdbcTemplate.update(PersonalPlansSQL.SQL_UPDATE_PLAN_SECTION_FOR_USER, set.getName(), set.getSummary(),
              set.getDueDate(), set.getSortOrder(), set.getId(), personalLearningPathDTO.getId());
        }
        Set<Long> courseIds =
            set.getCourses() != null
                ? set.getCourses().stream().filter(c -> c.getItemId() != null)
                    .map(c -> c.getItemId()).collect(Collectors.toSet())
                : new TreeSet<>();

        List<Long> sectionIds = Arrays.asList(set.getId());
        NamedParameterJdbcTemplate namedJDBCTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", sectionIds);
        List<ProgramSectionCourse> courses =
            namedJDBCTemplate.query(PersonalPlansSQL.SQL_SELECT_COURSES_FOR_SECTION_FOR_PLAN, parameters,
                new JodaBeanPropertyRowMapper<>(ProgramSectionCourse.class));
        // Delete all courses that are not found
        Set<Long> idsToDelete = courses.stream().filter(c -> !courseIds.contains(c.getItemId()))
            .map(c -> c.getItemId()).collect(Collectors.toSet());
        if (!idsToDelete.isEmpty()) {
          parameters = new MapSqlParameterSource();
          parameters.addValue("setId", set.getId());
          parameters.addValue("ids", idsToDelete);
          namedJDBCTemplate.update(PersonalPlansSQL.SQL_DELETE_PLAN_SECTION_COURSES_FOR_USER, parameters);
        }
        // Insert and update new ones.
        if (set.getCourses() != null) {
          for (ProgramCourseDTO course : set.getCourses()) {
            if (course.getItemId() == null) {
              // This is where we would insert the courses
              jdbcTemplate.queryForObject(PersonalPlansSQL.SQL_INSERT_PLAN_SECTION_COURSE_FOR_USER,
                  new Object[] {set.getId(), course.getId(), course.getType().name(),
                      course.getCms().name(), course.getFullName(), course.getDescription(),
                      course.getActivityValue(), course.getDueDate(), course.getSortOrder()},
                  Long.class);
            } else {
              jdbcTemplate.update(PersonalPlansSQL.SQL_UPDATE_PLAN_SECTION_COURSE_FOR_USER, course.getType().name(),
                  course.getCms().name(), course.getFullName(), course.getDescription(),
                  course.getActivityValue(), course.getDueDate(), course.getSortOrder(),
                  course.getItemId());
            }
          }
        }
      }
    }

  }

  @Override
  public Long insertPlanForUser(Long userId, PersonalLearningPathDTO personalLearningPathDTO) {

    Long planId = jdbcTemplate.queryForObject(
        PersonalPlansSQL.SQL_INSERT_PLAN_FOR_USER, new Object[] {personalLearningPathDTO.getTitle(),
            personalLearningPathDTO.getDescription(), personalLearningPathDTO.getDueDate(), userId},
        Long.class);
    List<ProgramCourseSetDTO> sets = personalLearningPathDTO.getCourseSets();

    if (sets != null) {
      for (ProgramCourseSetDTO set : sets) {
        Long sectionId =
            jdbcTemplate.queryForObject(PersonalPlansSQL.SQL_INSERT_PLAN_SECTION_FOR_USER, new Object[] {planId,
                set.getName(), set.getSummary(), set.getDueDate(), set.getSortOrder()}, Long.class);

        for (ProgramCourseDTO course : set.getCourses()) {
          // This is where we would insert the courses
          jdbcTemplate.queryForObject(PersonalPlansSQL.SQL_INSERT_PLAN_SECTION_COURSE_FOR_USER,
              new Object[] {sectionId, course.getId(), course.getType().name(),
                  course.getCms().name(), course.getFullName(), course.getDescription(),
                  course.getActivityValue(), course.getDueDate(), course.getSortOrder()},
              Long.class);
        }
      }
    }

    return planId;
  }

  @Override
  public List<PersonalProgramDTO> getPlansForUser(Long userId) {
    boolean archived = false;
    return jdbcTemplate.query(PersonalPlansSQL.SQL_SELECT_PLANS_FOR_USER, new Object[] {userId, archived},
        new JodaBeanPropertyRowMapper<>(PersonalProgramDTO.class));
  }

  @Override
  public List<PersonalProgramDTO> getPlansWithDueDatePassed() {
    boolean archived = false;
    return jdbcTemplate.query(PersonalPlansSQL.SQL_SELECT_PLANS_WITH_DUE_DATE_PASSED, new Object[] {},
        new JodaBeanPropertyRowMapper<>(PersonalProgramDTO.class));
  }

  @Override
  public List<PersonalProgramDTO> getArchivedPlansForUser(Long userId) {
    boolean archived = true;
    return jdbcTemplate.query(PersonalPlansSQL.SQL_SELECT_PLANS_FOR_USER, new Object[] {userId, archived},
        new JodaBeanPropertyRowMapper<>(PersonalProgramDTO.class));
  }

  @Override
  public Integer countPlansForUser(Long userId) {
    return jdbcTemplate.queryForObject(PersonalPlansSQL.SQL_SELECT_COUNT_PLANS_FOR_USER, new Object[] {userId},
        Integer.class);
  }

  @Override
  public List<PersonalProgramDTO> getPlansSharedWithUser(Long userId) {
    return jdbcTemplate.query(PersonalPlansSQL.SQL_SELECT_PLANS_SHARED_WITH_USER, new Object[] {userId},
        new JodaBeanPropertyRowMapper<>(PersonalProgramDTO.class));
  }

  @Override
  public List<PersonalProgramDTO> getPersonalSharedPlansForUser(Long userId) {
    return jdbcTemplate.query(PersonalPlansSQL.SQL_SELECT_PLANS_SHARED_FROM_USER, new Object[] {userId},
        new JodaBeanPropertyRowMapper<>(PersonalProgramDTO.class));
  }

  @Override
  public void archivePlanForUser(Long userId, Long pathId) {
    jdbcTemplate.update(PersonalPlansSQL.SQL_UPDATE_ARCHIVE_PLAN_FOR_USER, new Object[] {userId, pathId});
  }

}
