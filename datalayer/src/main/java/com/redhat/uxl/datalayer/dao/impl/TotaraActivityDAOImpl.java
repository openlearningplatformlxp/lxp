package com.redhat.uxl.datalayer.dao.impl;

import com.google.gson.Gson;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.datalayer.dao.TotaraActivityDAO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityChoiceDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentFeedbackItemDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityContentQuizDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityQuizSubmitDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityStatusAvailabilityConstraintDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityStatusAvailabilityDTO;
import com.redhat.uxl.datalayer.dto.QuizScoreDTO;
import com.redhat.uxl.datalayer.dto.TotaraActivityCompletionDTO;
import com.redhat.uxl.datalayer.repository.ScormValueRepository;
import com.redhat.uxl.datalayer.repository.VideoTimeRepository;
import com.redhat.uxl.datalayer.sql.totara.TotaraActivitySQL;
import com.redhat.uxl.dataobjects.domain.VideoTime;
import com.redhat.uxl.dataobjects.domain.types.ActivityCompletionType;
import com.redhat.uxl.dataobjects.domain.types.ActivityDisplayPackageType;
import com.redhat.uxl.dataobjects.domain.types.ActivityDisplayType;
import com.redhat.uxl.dataobjects.domain.types.ActivityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Totara activity dao.
 */
@Slf4j
@Service
public class TotaraActivityDAOImpl extends AbstractBaseTotaraDAOImpl implements TotaraActivityDAO {

    @Inject
    private JdbcTemplate totaraJdbcTemplate;

    @Value("${totara.url.file}")
    private String totaraUrlFile;

    @Inject
    private VideoTimeRepository videoTimeRepository;

    /**
     * The Scorm value repository.
     */
    @Inject
    ScormValueRepository scormValueRepository;

    @Override
    public Long getActivityCountForProgram(Long programId) {
        return totaraJdbcTemplate.queryForObject(TotaraActivitySQL.getAllActivitiesForProgram,
            new Object[] {programId}, Long.class);
    }

    @Override
    public Long getActivityCount(Long courseId) {
        return totaraJdbcTemplate.queryForObject(TotaraActivitySQL.getActivityCountForCourse,
            new Object[] {courseId}, Long.class);
    }

    @Override
    public Long getCompletedActivityCount(Long courseId, Long userId) {
        return totaraJdbcTemplate.queryForObject(TotaraActivitySQL.getCompletedActivitiesForCourseCount,
            new Object[] {courseId, userId}, Long.class);
    }

    @Override
    public Long getCourseStatus(Long courseId, Long userId) {
        return totaraJdbcTemplate.queryForObject(TotaraActivitySQL.getCourseStatus,
            new Object[] {userId, courseId}, Long.class);
    }

    @Override
    public void completeURLSingleActCourse(Long courseId, Long userId) {
        // Check to see if we have a single activity course with just URLs

        // get course module data
        String query = TotaraActivitySQL.getModulesForCourse.replace(":course", courseId.toString());

        try {
            List<Integer> courseModules = totaraJdbcTemplate.queryForList(query, Integer.class);
            if (courseModules != null && courseModules.size() == 1) {

                // Complete the course
                totaraJdbcTemplate.update(TotaraActivitySQL.completeCourseForSingle,
                    new Object[] {userId, courseId});

            }
        } catch (Exception e) {
            log.error("Could not complete URL course...", e);
        }
        // We do, complete the course, since completion criteria cant be set up

    }

    @Override
    public List<CoursePlayerActivityDTO> getActivitiesBySection(Long courseId, Long sectionId) {

        // get course module data
        String query =
            TotaraActivitySQL.activitiesBySectionSQL.replace(":courseId", courseId.toString());
        query = query.replace(":sectionId", sectionId.toString());

        log.debug(query);

        List<Map<String, Object>> courseModules = totaraJdbcTemplate.queryForList(query);

        // sort modules by type for querying
        Map<Long, List<Map<String, Object>>> sortedModules = new HashMap<>();
        for (Map<String, Object> module : courseModules) {
            List<Map<String, Object>> moduleList;
            if (sortedModules.containsKey((Long) (module.get("module")))) {
                moduleList = sortedModules.get((Long) (module.get("module")));
            } else {
                moduleList = new ArrayList<>();
            }
            moduleList.add(module);
            sortedModules.put((Long) (module.get("module")), moduleList);
        }

        // make query for each type

        // first get map of module mapping to table
        String sql_module_mapping = "Select id, name from mdl_modules";
        List<Map<String, Object>> module_mappings = totaraJdbcTemplate.queryForList(sql_module_mapping);

        for (Long moduleId : sortedModules.keySet()) {
            List<Map<String, Object>> moduleList = sortedModules.get(moduleId);

            // get ids from list of activities
            String set = "(";
            for (Map<String, Object> module : moduleList) {
                set = set + module.get("instance") + ",";
            }
            set = set.substring(0, set.length() - 1);
            set = set + ")";

            // find module mapping
            String tableName = "";
            for (Map<String, Object> module_mapping : module_mappings) {
                if (module_mapping.get("id").equals(moduleId)) {
                    tableName = "mdl_" + module_mapping.get("name");
                    break;
                }
            }

            if (!tableName.equals("")) {

                String sql_for_activity = "Select * from " + tableName + " where id in " + set + ";";

                List<Map<String, Object>> activities = totaraJdbcTemplate.queryForList(sql_for_activity);

                for (Map<String, Object> activity : activities) {
                    for (Map<String, Object> module : moduleList) {
                        if (module.get("instance").equals(activity.get("id"))) {
                            module.put("data_activity", activity);
                            module.put("data_activity_name", (String) activity.get("name"));
                            break;
                        }
                    }
                }
            }
        }

        // get users status in each activity
        // get a list of course moduleId
        List<CoursePlayerActivityDTO> activitiesOut = new ArrayList<>();
        if (!courseModules.isEmpty()) {
            List<Long> courseModuleIds = new ArrayList<>();
            for (Map<String, Object> courseModule : courseModules) {
                courseModuleIds.add((Long) courseModule.get("id"));
            }

            String set = "(";
            for (Long module : courseModuleIds) {
                set = set + module.toString() + ",";
            }
            set = set.substring(0, set.length() - 1);
            set = set + ")";

            // query for mdl_course_modules_completeion
            String sql_for_module_completeion =
                "Select * from mdl_course_modules_completion where coursemoduleid in " + set + ";";
            List<Map<String, Object>> courseModuleCompletions =
                totaraJdbcTemplate.queryForList(sql_for_module_completeion);

            // Process Activity data
            CoursePlayerActivityDTO activityOut;
            ActivityType type;
            for (Long moudleId : sortedModules.keySet()) {
                List<Map<String, Object>> modules = sortedModules.get(moudleId);
                for (Map<String, Object> module : modules) {
                    activityOut = new CoursePlayerActivityDTO();
                    activityOut.setId((Long) module.get("id"));
                    activityOut.setCourseId(courseId);
                    activityOut.setName((String) module.get("data_activity_name"));
                    activityOut.setTimecompleted((Long) module.get("timecompleted"));
                    for (Map<String, Object> module_mapping : module_mappings) {
                        if (((Long) module_mapping.get("id")).equals((Long) module.get("module"))) {
                            try {
                                type = ActivityType.valueOf(((String) module_mapping.get("name")).toUpperCase());
                            } catch (Exception e) {
                                type = ActivityType.NOT_SUPPORT;
                            }
                            activityOut.setType(type);
                            break;
                        }
                    }

                    activityOut.setStatus(0l);
                    for (Map<String, Object> courseModuleCompletion : courseModuleCompletions) {
                        if (activityOut.getId().equals((Long) courseModuleCompletion.get("coursemoduleid"))) {
                            activityOut
                                .setStatus(((Integer) courseModuleCompletion.get("completionstate")).longValue());
                        }
                    }
                    activitiesOut.add(activityOut);
                }
            }

        }

        return activitiesOut;
    }

    @Override
    public CoursePlayerActivityDTO getActivityContent(Long activityId, Long personId, Long personTotaraId) {
        CoursePlayerActivityDTO activity = new CoursePlayerActivityDTO();
        activity.setIsLocked(false);

        // get course_module
        String courseModuleQuery = TotaraActivitySQL.getActivityCourseModuleById
            .replace(":courseModuleId", activityId.toString());
        log.debug(courseModuleQuery);

        Map<String, Object> courseModuleMap = totaraJdbcTemplate.queryForMap(courseModuleQuery);

        // check if this activity is locked
        CoursePlayerActivityStatusAvailabilityDTO statusBo =
            CoursePlayerActivityStatusAvailabilityDTO.valueOf(courseModuleMap);
        activity.setIsLocked(isActivityLocked(personTotaraId, statusBo));

        // get activity type from module_mapping
        String moduleTypeQuery = TotaraActivitySQL.getActivityTypeById.replace(":moduleId",
            ((Long) courseModuleMap.get("module")).toString());
        log.debug(moduleTypeQuery);

        Map<String, Object> activityTypeMap = totaraJdbcTemplate.queryForMap(moduleTypeQuery);

        ActivityType activityType;
        String activityTypeString = (String) activityTypeMap.get("name");
        activity.setOriginalActivityType(activityTypeString);

        try {
            activityType = ActivityType.valueOf((activityTypeString).toUpperCase());
        } catch (Exception e) {
            activityType = ActivityType.NOT_SUPPORT;
        }

        // get activity content from specific table
        String activityContentQuery =
            TotaraActivitySQL.getActivityContentById.replace(":tableName", "mdl_" + activityTypeString);
        activityContentQuery = activityContentQuery.replace(":activityInstanceId",
            ((Long) courseModuleMap.get("instance")).toString());
        log.debug(activityContentQuery);

        Map<String, Object> activityContentMap = totaraJdbcTemplate.queryForMap(activityContentQuery);

        activity.setId((Long) courseModuleMap.get("id"));
        activity.setCourseId((Long) courseModuleMap.get("course"));
        activity.setName((String) activityContentMap.get("name"));
        activity.setType(activityType);

        // if activity is not supported, just return it no need to process contents
        if (activityType.equals(ActivityType.NOT_SUPPORT)) {
            return activity;
        }

        if (activity.getIsLocked()) {
            return activity;
        }

        Integer completionValue = (Integer) courseModuleMap.get("completion");
        boolean hasCompletionRequirements = false;

        if (completionValue != null) {
            if (completionValue.equals(ActivityCompletionType.COMPLETION_TRACKING_MANUAL.getValue())) {
                activity.setAllowsManualCompletion(true);
            } else if (completionValue
                .equals(ActivityCompletionType.COMPLETION_TRACKING_AUTOMATIC.getValue())) {
                hasCompletionRequirements = true;
            }
        }

        CoursePlayerActivityContentDTO activityContentBO = null;

        // process activity contents
        switch (activityType) {
            case RESOURCE:

                activityContentBO = new CoursePlayerActivityContentDTO();

                // get context id - context for "modules" is 70
                String contextQuery = TotaraActivitySQL.getActivityContextSQL.replace(":courseModuleId",
                    ((Long) courseModuleMap.get("id")).toString());
                log.debug(contextQuery);

                Map<String, Object> resourceContextMap = totaraJdbcTemplate.queryForMap(contextQuery);

                // get row from mdl_files
                String filesQuery = TotaraActivitySQL.getActivityFilesSQL.replace(":contextId",
                    ((Long) resourceContextMap.get("id")).toString());
                filesQuery = filesQuery.replace(":componentName", "mod_" + activityTypeString);
                log.debug(filesQuery);

                Map<String, Object> filesMap = totaraJdbcTemplate.queryForMap(filesQuery);

                String contextId = ((Long) resourceContextMap.get("id")).toString();
                String revision = ((Long) activityContentMap.get("revision")).toString();
                String filepath = (String) filesMap.get("filepath");
                String filename = (String) filesMap.get("filename");

                activityContentBO.setDescription((String) activityContentMap.get("intro"));

                String url = totaraUrlFile + "/pluginfile.php/" + contextId + "/mod_resource/content/"
                    + revision + filepath + filename;
                activityContentBO.setUrl(url);

                Integer displayValue = (Integer) activityContentMap.get("display");
                activityContentBO.setShouldDisplayInNewWindow(
                    displayValue.equals(ActivityDisplayType.DISPLAY_NEW_WINDOW.getValue())
                        || displayValue.equals(ActivityDisplayType.DISPLAY_OPEN.getValue())
                        || displayValue.equals(ActivityDisplayType.DISPLAY_IN_POPUP.getValue()));

                if (hasCompletionRequirements) {
                    Integer completionViewValue = (Integer) courseModuleMap.get("completionview");

                    if (completionViewValue != null && completionViewValue.equals(1)) {
                        activity.setShouldCompleteOnView(true);
                    }
                }

                // get video play time
                VideoTime videoTime = videoTimeRepository.findByModuleIdAndPersonId(activityId, personId);
                if (videoTime != null) {
                    activityContentBO.setVideoTime(videoTime.getTime());
                }

                break;
            case URL:

                activityContentBO = new CoursePlayerActivityContentDTO();

                activityContentBO.setDescription((String) activityContentMap.get("intro"));
                activityContentBO.setUrl((String) activityContentMap.get("externalurl"));
                activityContentBO.setShouldDisplayInNewWindow(true);

                if (hasCompletionRequirements) {
                    Integer completionViewValue = (Integer) courseModuleMap.get("completionview");

                    if (completionViewValue != null && completionViewValue.equals(1)) {
                        activity.setShouldCompleteOnView(true);
                    }
                }
                break;
            case FEEDBACK:
                activityContentBO = new CoursePlayerActivityContentDTO();

                // get questions from mdl_feedback_items
                Long feedbackId = (Long) activityContentMap.get("id");

                String feedbackItemQuery =
                    TotaraActivitySQL.getFeedbackItemSQL.replace(":feedbackId", feedbackId.toString());

                List<Map<String, Object>> items = totaraJdbcTemplate.queryForList(feedbackItemQuery);

                List<CoursePlayerActivityContentFeedbackItemDTO> itemsList = new ArrayList<>();
                CoursePlayerActivityContentFeedbackItemDTO bo;
                for (Map<String, Object> item : items) {
                    bo = new CoursePlayerActivityContentFeedbackItemDTO();
                    bo.setId((Long) item.get("id"));
                    bo.setName((String) item.get("name"));
                    bo.setLabel((String) item.get("label"));
                    bo.setPresentation((String) item.get("presentation"));
                    bo.setType((String) item.get("typ"));
                    bo.setPosition((Integer) item.get("position"));
                    bo.setRequired((Integer) item.get("required"));
                    itemsList.add(bo);
                }

                activityContentBO.setItems(itemsList);

                break;
            case SCORM:

                activityContentBO = new CoursePlayerActivityContentDTO();

                // get scrom_scoes values
                String scoesQuery = TotaraActivitySQL.getScormLaunchUrlSQL.replace(":scormId",
                    ((Long) activityContentMap.get("id")).toString());
                log.debug(scoesQuery);

                Map<String, Object> scoeMap = totaraJdbcTemplate.queryForMap(scoesQuery);

                // get context id - context for "modules" is 70
                String scormContextQuery = TotaraActivitySQL.getActivityContextSQL
                    .replace(":courseModuleId", ((Long) courseModuleMap.get("id")).toString());
                log.debug(scormContextQuery);

                Map<String, Object> scormContextMap = totaraJdbcTemplate.queryForMap(scormContextQuery);

                String scormContextId = ((Long) scormContextMap.get("id")).toString();
                String scormRevision = ((Long) activityContentMap.get("revision")).toString();
                String scormLauncher = (String) scoeMap.get("launch");

                activityContentBO.setDescription((String) activityContentMap.get("intro"));

                String scormUrl = totaraUrlFile + "/pluginfile.php/" + scormContextId
                    + "/mod_scorm/content/" + scormRevision + "/" + scormLauncher;
                activityContentBO.setUrl(scormUrl);

                Integer displayPackageValue = (Integer) activityContentMap.get("popup");
                activityContentBO.setShouldDisplayInNewWindow(
                    displayPackageValue.equals(ActivityDisplayPackageType.DISPLAY_NEW_WINDOW.getValue())
                        || displayPackageValue
                        .equals(ActivityDisplayPackageType.DISPLAY_NEW_WINDOW_SIMPLE.getValue()));

                // get scorm values
                activityContentBO.setValues(scormValueRepository.findByModuleIdAndPersonId(activityId, personId));

                if (hasCompletionRequirements) {
                    // TODO: (WJK) Handle scorm-related requirements
                }

                break;

            case QUIZ:

                activityContentBO = new CoursePlayerActivityContentDTO();

                activityContentBO.setDescription((String) activityContentMap.get("intro"));
                activityContentBO.setQuiz(getQuizContentFromTotara(personTotaraId, activityId));

                break;

            case LABEL:

                activityContentBO = new CoursePlayerActivityContentDTO();

                activityContentBO.setHtml((String) activityContentMap.get("intro"));

                break;

            case PAGE:

                activityContentBO = new CoursePlayerActivityContentDTO();

                activityContentBO.setDescription((String) activityContentMap.get("intro"));
                activityContentBO.setHtml((String) activityContentMap.get("content"));

                if (hasCompletionRequirements) {
                    Integer completionViewValue = (Integer) courseModuleMap.get("completionview");

                    if (completionViewValue != null && completionViewValue.equals(1)) {
                        activity.setShouldCompleteOnView(true);
                    }
                }

                break;

            case CERTIFICATE:

                break;

            case CHOICE:

                break;
            default:
                break;
        }

        activity.setContent(activityContentBO);

        return activity;
    }

    @Override
    public List<CoursePlayerActivityStatusAvailabilityDTO> getStatusForActivitiesInCourse(Long courseId, Long personTotaraId) {
        String sql = TotaraActivitySQL.getActivityStatusForActivitiesInCourseSQL
            .replace(":courseId", courseId.toString()).replace(":userId", personTotaraId.toString());
        log.debug(sql);

        return processActivityStatuses(courseId, personTotaraId, totaraJdbcTemplate.queryForList(sql));
    }

    @Override
    public List<CoursePlayerActivityStatusAvailabilityDTO> getStatusForActivitiesInSameCourse(Long activityId, Long personTotaraId) {
        String sql = TotaraActivitySQL.getActivityStatusForActivitiesInSameCourseSQL
            .replace(":moduleId", activityId.toString()).replace(":userId", personTotaraId.toString());
        log.debug(sql);

        return processActivityStatuses(null, personTotaraId, totaraJdbcTemplate.queryForList(sql));
    }

    @Override
    public TotaraActivityCompletionDTO completeActivity(Long moduleId, Long personTotaraId, int targetState) {
        String function = "&wsfunction=ws_submit_activity";
        String params =
            "&userid=" + personTotaraId + "&moduleid=" + moduleId + "&targetstate=" + targetState;
        try {
            TotaraActivityCompletionDTO result =
                (restTemplate.postForObject(generateURL(COURSE_TOKEN, function, params), "{}",
                    TotaraActivityCompletionDTO.class));
            return result;
        } catch (Exception e) {
            log.error("Error completing Totara Activity: " + e.getMessage() + ". Params were: " + params);
            return null;
        }
    }

    @Override
    public TotaraActivityCompletionDTO completeActivityWithData(Long moduleId, Long personTotaraId, int targetState, String data) {
        String function = "&wsfunction=ws_submit_activity";
        String params =
            "&userid=" + personTotaraId + "&moduleid=" + moduleId + "&targetstate=" + targetState;

        try {
            TotaraActivityCompletionDTO activity =
                (restTemplate.postForObject(generateURL(COURSE_TOKEN, function, params), data,
                    TotaraActivityCompletionDTO.class));

            return activity;
        } catch (Exception e) {
            log.error("Error completing Totara Activity: " + e.getMessage() + ".");
            return null;
        }
    }

    @Override
    public String submitTotaraActivityQuizQuestionAnswer(CoursePlayerActivityQuizSubmitDTO dto, Long personTotaraId) {
        String function = "&wsfunction=ws_submit_quiz_answer";
        String params = "&userid=" + personTotaraId + "&attemptid=" + dto.getAttemptId();

        Gson gson = new Gson();
        String postData = gson.toJson(dto);

        try {
            String url = generateURL(COURSE_TOKEN, function, params);
            String response = (restTemplate.postForObject(url, postData, String.class));

            return response;
        } catch (Exception e) {
            log.error("Error submitting Totara Activity Quiz Question Answer: " + e.getMessage() + ".");
            throw new GeneralException(ErrorCodeGeneral.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public QuizScoreDTO submitTotaraActivityQuizAttempt(Long attemptid, Long personTotaraId) {
        log.info("Submitting attempt id " + attemptid + " for " + personTotaraId);
        String function = "&wsfunction=ws_submit_quiz_attempt";
        String params = "&attemptid=" + attemptid;

        QuizScoreDTO response = (restTemplate.postForObject(generateURL(COURSE_TOKEN, function, params),
            null, QuizScoreDTO.class));
        log.info("Got response from Totara - Grade: " + response.getAttempt_grade()
            + " Course Complete: " + response.getCourse_complete());

        return response;
    }

    @Override
    public CoursePlayerActivityChoiceDTO getChoiceContent(Long personTotaraId, Long activityId) {
        log.info("Get choice content: activity id " + activityId + " for " + personTotaraId);
        String function = "&wsfunction=ws_get_choice";
        String params = "&userid=" + personTotaraId + "&moduleid=" + activityId;
        CoursePlayerActivityChoiceDTO response =
            (restTemplate.getForObject(generateURL(COURSE_TOKEN, function, params),
                CoursePlayerActivityChoiceDTO.class));
        return response;
    }

    @Override
    public CoursePlayerActivityChoiceDTO submitChoice(Long totaraId, Long moduleId, CoursePlayerActivityChoiceDTO postData) {
        log.info("Submitting choice content: activity id " + moduleId + " for " + totaraId);
        String function = "&wsfunction=ws_submit_choice";
        String params = "&userid=" + totaraId + "&moduleid=" + moduleId;
        Gson gson = new Gson();
        String postDataJson = gson.toJson(postData);
        CoursePlayerActivityChoiceDTO response =
            (restTemplate.postForObject(generateURL(COURSE_TOKEN, function, params), postDataJson,
                CoursePlayerActivityChoiceDTO.class));
        return response;
    }






    // PRIVATE HELPER METHODS





    private Boolean isActivityLocked(Long personTotaraId,
        CoursePlayerActivityStatusAvailabilityDTO statusBo) {

        Boolean isActivityLocked = false;

        if (statusBo.getC() != null && statusBo.getC().length > 0) {
            String completionSet = "(";
            String gradeSet = "(";
            int numCompletion = 0;
            int numGrades = 0;
            for (CoursePlayerActivityStatusAvailabilityConstraintDTO constraintBO : statusBo.getC()) {
                if (constraintBO.getType().equals("completion")) {
                    completionSet = completionSet + constraintBO.getCm().toString() + ",";
                    numCompletion++;
                }

                if (constraintBO.getType().equals("grade")) {
                    gradeSet = gradeSet + constraintBO.getId().toString() + ",";
                    numGrades++;
                }
            }
            completionSet = completionSet.substring(0, completionSet.length() - 1);
            completionSet = completionSet + ")";

            gradeSet = gradeSet.substring(0, gradeSet.length() - 1);
            gradeSet = gradeSet + ")";

            // get all completions
            List<Map<String, Object>> activityCompletions = new ArrayList<>();
            if (numCompletion > 0) {
                String activitiesCompleteQuery =
                    TotaraActivitySQL.getActivityCompletionWithCourseModuleIdInAndActivityIsComplete
                        .replace(":set", completionSet).replace(":userId", personTotaraId.toString());
                log.debug(activitiesCompleteQuery);

                activityCompletions = totaraJdbcTemplate.queryForList(activitiesCompleteQuery);
            }

            // get all grades
            List<Map<String, Object>> activityGrades = new ArrayList<>();
            if (numGrades > 0) {
                String gradesQuery = TotaraActivitySQL.getGradeItemsGradeByUser.replace(":set", gradeSet)
                    .replace(":userId", personTotaraId.toString());
                log.debug(gradesQuery);

                activityGrades = totaraJdbcTemplate.queryForList(gradesQuery);
            }

            // check operation ( "|" means any of the constraints need to be completed, "&" all of the
            // constraints need
            // to be completed)
            if (statusBo.getOp().contains("&")) {

                boolean allCompletionsComplete = false;
                boolean allGradeRequirementsMet = false;

                // completions
                // the completion should be the same number as activityCompletions
                if (numCompletion > 0) {
                    if (activityCompletions == null || activityCompletions.isEmpty()
                        || activityCompletions.size() != numCompletion) {
                        allCompletionsComplete = false;
                    } else {
                        allCompletionsComplete = true;
                    }
                } else {
                    allCompletionsComplete = true;
                }

                // grades
                // make sure all grades are
                if (numGrades > 0) {
                    boolean allGradesMet = true;
                    for (CoursePlayerActivityStatusAvailabilityConstraintDTO constraintBO : statusBo.getC()) {
                        if (constraintBO.getType().equals("grade")) {
                            for (Map<String, Object> gradeItem : activityGrades) {
                                if (constraintBO.getId().equals((Long) gradeItem.get("itemid"))) {

                                    if (null == gradeItem.get("rawgrade")) {
                                        allGradesMet = false;
                                        continue;
                                    }

                                    // check min/max
                                    if (null != constraintBO.getMax()) {
                                        if (constraintBO.getMax() <= ((BigDecimal) gradeItem.get("rawgrade"))
                                            .doubleValue()) {
                                            allGradesMet = false;
                                        }
                                    } else if (null != constraintBO.getMin()) {
                                        if (constraintBO.getMin() > ((BigDecimal) gradeItem.get("rawgrade"))
                                            .doubleValue()) {
                                            allGradesMet = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    allCompletionsComplete = allGradesMet;
                } else {
                    allGradeRequirementsMet = true;
                }

                // set activity.isLocked status
                if (allCompletionsComplete && allGradeRequirementsMet) {
                    isActivityLocked = false;
                } else {
                    isActivityLocked = true;
                }

            } else if (statusBo.getOp().contains("|")) {

                boolean anyCompletionsComplete = false;
                boolean anyGradeRequirementsMet = false;

                // completions
                if (activityCompletions.size() > 0) {
                    anyCompletionsComplete = true;
                } else {
                    anyCompletionsComplete = false;
                }

                // grades
                if (numGrades > 0) {
                    boolean anyGradesMet = false;
                    for (CoursePlayerActivityStatusAvailabilityConstraintDTO constraintBO : statusBo.getC()) {
                        if (constraintBO.getType().equals("grade")) {
                            for (Map<String, Object> gradeItem : activityGrades) {
                                if (constraintBO.getId().equals((Long) gradeItem.get("itemid"))) {

                                    if (null == gradeItem.get("rawgrade")) {
                                        continue;
                                    }

                                    // check min/max
                                    if (null != constraintBO.getMax()) {
                                        if (constraintBO.getMax() <= ((BigDecimal) gradeItem.get("rawgrade"))
                                            .doubleValue()) {
                                            anyGradesMet = true;
                                            break;
                                        }
                                    } else if (null != constraintBO.getMin()) {
                                        if (constraintBO.getMin() > ((BigDecimal) gradeItem.get("rawgrade"))
                                            .doubleValue()) {
                                            anyGradesMet = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (anyGradesMet) {
                            break;
                        }
                    }
                    anyGradeRequirementsMet = anyGradesMet;
                } else {
                    anyGradeRequirementsMet = false;
                }

                if (anyCompletionsComplete || anyGradeRequirementsMet) {
                    isActivityLocked = false;
                } else {
                    isActivityLocked = true;
                }
            }

            // if the "op" value is "!|" or "!&" reverse the value
            if (statusBo.getOp().contains("!")) {
                isActivityLocked = !isActivityLocked;
            }
        }

        return isActivityLocked;
    }

    private CoursePlayerActivityContentQuizDTO getQuizContentFromTotara(Long personTotaraId,
        Long moduleId) {
        String function = "&wsfunction=ws_get_module";
        String params = "&userid=" + personTotaraId + "&moduleid=" + moduleId;
        try {
            CoursePlayerActivityContentQuizDTO result =
                (restTemplate.postForObject(generateURL(COURSE_TOKEN, function, params), "",
                    CoursePlayerActivityContentQuizDTO.class));
            return result;
        } catch (Exception e) {
            log.error("Error completing Totara Activity: " + e.getMessage() + ". Params were: " + params);
            return null;
        }
    }

    private List<CoursePlayerActivityStatusAvailabilityDTO> processActivityStatuses(Long courseId,
        Long personTotaraId, List<Map<String, Object>> activityStatusQueryResults) {

        // make map so we can mark each object as locked or not
        Map<Long, CoursePlayerActivityStatusAvailabilityDTO> courseModuleMap = new HashMap<>();
        for (Map<String, Object> result : activityStatusQueryResults) {
            courseModuleMap.put((Long) result.get("id"),
                CoursePlayerActivityStatusAvailabilityDTO.valueOf(result));
            if (courseId == null) {
                courseId = (Long) result.get("course");
            }
        }

        for (Long key : courseModuleMap.keySet()) {
            if (courseModuleMap.get(key).getC() != null && courseModuleMap.get(key).getC().length > 0) {
                List<Long> constraintModuleIds = new ArrayList<>();
                List<Long> gradeItemIds = new ArrayList<>();
                for (CoursePlayerActivityStatusAvailabilityConstraintDTO constraint : courseModuleMap
                    .get(key).getC()) {
                    constraintModuleIds.add(constraint.getCm());
                    gradeItemIds.add(constraint.getId());
                }

                courseModuleMap.get(key)
                    .setIsLocked(isActivityLocked(personTotaraId, courseModuleMap.get(key)));

                // if the module is now locked get the names of the activities that need to have a complete
                // status
                if (courseModuleMap.get(key).getIsLocked()) {
                    courseModuleMap.get(key)
                        .setRequiredAction(getActivityNames(constraintModuleIds, gradeItemIds));
                }

            }
        }

        // get activity required boolean
        getRequiredBooleanForActivities(courseId, courseModuleMap);

        return new ArrayList<>(courseModuleMap.values());
    }

    private List<Map<String, Object>> getActivityNames(List<Long> moduleIds,
        List<Long> gradeItemIds) {

        // get gradeItems names
        List<Map<String, Object>> gradItemsInfo = new ArrayList<>();
        if (!gradeItemIds.isEmpty()) {
            String gradeItemIdSet = "(";
            for (Long moduleId : gradeItemIds) {
                gradeItemIdSet = gradeItemIdSet + moduleId + ",";
            }
            gradeItemIdSet = gradeItemIdSet.substring(0, gradeItemIdSet.length() - 1);
            gradeItemIdSet = gradeItemIdSet + ")";

            String gradeItemQuery = "Select * from mdl_grade_items where id in " + gradeItemIdSet + ";";

            log.debug(gradeItemQuery);

            List<Map<String, Object>> gradItemsMaps = totaraJdbcTemplate.queryForList(gradeItemQuery);

            // sort grade items to query tables
            Map<String, List<Map<String, Object>>> sortedGradeItemsMaps = new HashMap<>();

            for (Map<String, Object> gradeItemMap : gradItemsMaps) {
                if (sortedGradeItemsMaps.containsKey((String) gradeItemMap.get("itemmodule"))) {
                    sortedGradeItemsMaps.get((String) gradeItemMap.get("itemmodule")).add(gradeItemMap);
                } else {
                    List<Map<String, Object>> newMapList = new ArrayList<>();
                    newMapList.add(gradeItemMap);
                    sortedGradeItemsMaps.put((String) gradeItemMap.get("itemmodule"), newMapList);
                }
            }

            // query the tables for their names
            List<Map<String, Object>> gradItemNames = new ArrayList<>();
            for (String key : sortedGradeItemsMaps.keySet()) {

                String set = "(";
                for (Map<String, Object> gradeItemMap : sortedGradeItemsMaps.get(key)) {
                    set = set + gradeItemMap.get("iteminstance") + ",";
                }
                set = set.substring(0, set.length() - 1);
                set = set + ")";

                String tableName = "mdl_" + key;

                String gradeItemTablequery = "Select * from " + tableName + " where id in " + set + ";";

                log.debug(gradeItemQuery);

                List<Map<String, Object>> gradeItemMapsByTable =
                    totaraJdbcTemplate.queryForList(gradeItemTablequery);

                Map<String, Object> gradeItemOut;
                for (Map<String, Object> gradeItemMapByTable : gradeItemMapsByTable) {
                    gradeItemOut = new HashMap<>();
                    gradeItemOut.put("activityName", (String) gradeItemMapByTable.get("name"));
                    gradeItemOut.put("type", "grade");
                    gradItemsInfo.add(gradeItemOut);
                }

            }

        }

        // create set of moduleIds
        String moduleIdset = "(";
        for (Long moduleId : moduleIds) {
            moduleIdset = moduleIdset + moduleId + ",";
        }
        moduleIdset = moduleIdset.substring(0, moduleIdset.length() - 1);
        moduleIdset = moduleIdset + ")";

        // get course module data
        String query = "Select * from mdl_course_modules where id in " + moduleIdset + ";";

        log.debug(query);

        List<Map<String, Object>> courseModules = totaraJdbcTemplate.queryForList(query);

        // sort modules by type for querying
        Map<Long, List<Map<String, Object>>> sortedModules = new HashMap<>();
        for (Map<String, Object> module : courseModules) {
            List<Map<String, Object>> moduleList;
            if (sortedModules.containsKey((Long) (module.get("module")))) {
                moduleList = sortedModules.get((Long) (module.get("module")));
            } else {
                moduleList = new ArrayList<>();
            }
            moduleList.add(module);
            sortedModules.put((Long) (module.get("module")), moduleList);

        }

        // make query for each type

        // first get map of module mapping to table
        String sql_module_mapping = "Select id, name from mdl_modules";
        List<Map<String, Object>> module_mappings = totaraJdbcTemplate.queryForList(sql_module_mapping);

        for (Long moduleId : sortedModules.keySet()) {
            List<Map<String, Object>> moduleList = sortedModules.get(moduleId);

            // get ids from list of activities
            String set = "(";
            for (Map<String, Object> module : moduleList) {
                set = set + module.get("instance") + ",";
            }
            set = set.substring(0, set.length() - 1);
            set = set + ")";

            // find module mapping
            String tableName = "";
            for (Map<String, Object> module_mapping : module_mappings) {
                if (module_mapping.get("id").equals(moduleId)) {
                    tableName = "mdl_" + module_mapping.get("name");
                    break;
                }
            }

            if (!tableName.equals("")) {

                String sql_for_activity = "Select * from " + tableName + " where id in " + set + ";";

                List<Map<String, Object>> activities = totaraJdbcTemplate.queryForList(sql_for_activity);

                for (Map<String, Object> activity : activities) {
                    for (Map<String, Object> module : moduleList) {
                        if (module.get("instance").equals(activity.get("id"))) {
                            module.put("data_activity", activity);
                            module.put("data_activity_name", (String) activity.get("name"));
                            break;
                        }
                    }
                }
            }
        }

        // order modules so they appear in same order they came in
        List<Map<String, Object>> activitiesOut = new ArrayList<>();
        Map<String, Object> activityOut;
        for (Long moduleId : moduleIds) {
            if (null == moduleId) {
                continue;
            }
            for (Map<String, Object> activity : courseModules) {
                if (moduleId.equals((Long) activity.get("id"))) {
                    activityOut = new HashMap<>();
                    activityOut.put("id", activity.get("id"));
                    activityOut.put("activityName", activity.get("data_activity_name"));
                    activityOut.put("type", "completion");
                    activitiesOut.add(activityOut);
                }
            }

        }

        activitiesOut.addAll(gradItemsInfo);

        return activitiesOut;
    }

    private void getRequiredBooleanForActivities(Long courseId,
        Map<Long, CoursePlayerActivityStatusAvailabilityDTO> courseModuleMap) {
        // get activity required boolean
        String query = TotaraActivitySQL.getRequiredactivitiesByCompletionCriteriaSQL
            .replace(":courseId", courseId.toString());
        query = query.replace(":criteriatype", "4");

        log.debug(query);

        List<Map<String, Object>> results = totaraJdbcTemplate.queryForList(query);

        for (Map<String, Object> result : results) {
            if (courseModuleMap.containsKey((Long) result.get("moduleinstance"))) {
                courseModuleMap.get((Long) result.get("moduleinstance")).setRequired(true);
            }
        }
    }


}
