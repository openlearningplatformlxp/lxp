package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.dataobjects.domain.types.TotaraCourseSetNextSetOperatorType;
import com.redhat.uxl.services.service.ProgramPlayerService;
import com.redhat.uxl.services.service.TotaraEnrollmentService;
import com.redhat.uxl.services.service.TotaraProgramService;
import com.redhat.uxl.datalayer.dto.TotaraCourseContentDTO;
import com.redhat.uxl.datalayer.dto.TotaraCourseSetDTO;
import com.redhat.uxl.datalayer.dto.TotaraProgramDTO;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * The type Program player service.
 */
@Service
public class ProgramPlayerServiceImpl implements ProgramPlayerService {

    /**
     * The Totara program service.
     */
    @Inject
  TotaraProgramService totaraProgramService;

    /**
     * The Totara enrollment service.
     */
    @Inject
  TotaraEnrollmentService totaraEnrollmentService;

  public TotaraProgramDTO getProgramContentForUser(Long programId, Long personTotaraId) {

    // get Program
    final CompletableFuture<TotaraProgramDTO> programBo =
        CompletableFuture.supplyAsync(() -> totaraProgramService.getProgramById(programId));
    final CompletableFuture<Boolean> isUserEnrolledInProgram = CompletableFuture
        .supplyAsync(() -> totaraProgramService.isUserEnrolledInProgram(personTotaraId, programId));
    final CompletableFuture<List<TotaraCourseSetDTO>> courseSets = CompletableFuture.supplyAsync(
        () -> totaraProgramService.getCourseSetsInProgram(personTotaraId, programId, true));

    // create Response
    try {
      TotaraProgramDTO program = programBo.get();
      // get courses for program in order
      program.setCourseSets(calculateCourseSetLockedStatus(courseSets.get()));
      // check if user is enrolled in the program
      program.setIsUserEnrolled(isUserEnrolledInProgram.get());

      List<Long> courseIds =
          courseSets.get().stream().map(TotaraCourseSetDTO::getCourses).flatMap(List::stream)
              .map(TotaraCourseContentDTO::getCourseID).collect(Collectors.toList());
      program.setEnrolledCourses(
          totaraEnrollmentService.getEnrolledCoursesById(personTotaraId, courseIds));
      return program;
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private List<TotaraCourseSetDTO> calculateCourseSetLockedStatus(
      List<TotaraCourseSetDTO> courseSets) {

    // make sure the courseSets are in order
    Collections.sort(courseSets);

    // group course sets by THEN operators
    List<List<TotaraCourseSetDTO>> THENcourseSets = new ArrayList<>();
    List<TotaraCourseSetDTO> currentSet = new ArrayList<>();
    for (TotaraCourseSetDTO courseSetBO : courseSets) {

      currentSet.add(courseSetBO);

      // if nextSetOperator is null then it is a last courseSet
      if (null == courseSetBO.getNextSetOperator()) {
        continue;
      }

      if (courseSetBO.getNextSetOperator()
          .equals(TotaraCourseSetNextSetOperatorType.NEXTSETOPERATOR_THEN)) {
        THENcourseSets.add(currentSet);
        currentSet = new ArrayList<>();
      }
    }

    THENcourseSets.add(currentSet);

    // create an expression for each set and test it
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("JavaScript");

    List<TotaraCourseSetDTO> courseSetBOsOut = new ArrayList<>();
    String exp = "";
    String booleanString = "";
    String operatorString = "";
    Boolean outComeOfPreviousCourseSetList = true;
    for (List<TotaraCourseSetDTO> courseSetList : THENcourseSets) {

      if (outComeOfPreviousCourseSetList) {
        exp = "";
        booleanString = "";
        for (TotaraCourseSetDTO courseSet : courseSetList) {

          // since youve made it this, all the coursesets in the list should be marked as unlocked
          courseSet.setIsLocked(false);
          courseSetBOsOut.add(courseSet);

          operatorString = "";

          if (courseSet.getUserStatus() == null || courseSet.getUserStatus() < 3) {
            booleanString = "false ";
          } else {
            booleanString = "true ";
          }

          if (null != courseSet.getNextSetOperator()) {
            if (courseSet.getNextSetOperator()
                .equals(TotaraCourseSetNextSetOperatorType.NEXTSETOPERATOR_OR)) {
              operatorString = "|| ";
            } else if (courseSet.getNextSetOperator()
                .equals(TotaraCourseSetNextSetOperatorType.NEXTSETOPERATOR_AND)) {
              operatorString = "&& ";
            }
          }

          exp = exp + booleanString + operatorString;

        }

        try {
          outComeOfPreviousCourseSetList = (Boolean) engine.eval(exp);
        } catch (ScriptException e) {
          // throw an error?
        }
      } else {
        // since the previous set was not complete, make the rest of the sets as locked
        for (TotaraCourseSetDTO courseSet : courseSetList) {
          courseSet.setIsLocked(true);
          courseSetBOsOut.add(courseSet);
        }
      }

    }

    return courseSetBOsOut;
  }
}
