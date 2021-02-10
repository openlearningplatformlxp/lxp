package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara activity sql.
 */
public class TotaraActivitySQL {

    /**
     * The constant completeCourseForSingle.
     */
    public static final String completeCourseForSingle =
      "update mdl_course_completions set timecompleted = extract(epoch from now()), status = 75, rpl = 'LRS Completed' where userid = ? and course = ? and status != 75";
    /**
     * The constant getModulesForCourse.
     */
    public static final String getModulesForCourse =
      "select id from mdl_course where id = :course and format = 'singleactivity'";
    /**
     * The constant getAllActivitiesForProgram.
     */
    public static final String getAllActivitiesForProgram =
      "select count(*) from mdl_prog_courseset pc, mdl_prog_courseset_course pcc, mdl_course_modules cm where pc.id = pcc.coursesetid and cm.course = pcc.courseid and pc.programid = ?";
    /**
     * The constant getActivityCountForCourse.
     */
    public static final String getActivityCountForCourse =
      "select count(*) from mdl_course_modules where course = ?";
    /**
     * The constant getCompletedActivitiesForCourseCount.
     */
    public static final String getCompletedActivitiesForCourseCount =
      "select count(*) from mdl_course_modules_completion cmc, mdl_course_modules m, mdl_course_completions mcc where m.id = cmc.coursemoduleid and m.course=? and cmc.userid = ? and cmc.completionstate=1 and mcc.userid = cmc.userid and mcc.course = m.course and mcc.timecompleted is not null";

    /**
     * The constant getCourseStatus.
     */
    public static final String getCourseStatus =
      "select cc.status from mdl_course_completions cc where cc.userid = ? and cc.course = ?";

    /**
     * The constant activitiesBySectionSQL.
     */
    public static final String activitiesBySectionSQL = "select *" + " from mdl_course_modules as cm"
      + " where cm.course = :courseId" + " and cm.section = :sectionId and cm.visible = 1\n";

    /**
     * The constant getActivityCourseModuleById.
     */
    public static final String getActivityCourseModuleById =
      "select *" + " from mdl_course_modules" + " where id = :courseModuleId and visible = 1 \n";

    /**
     * The constant getActivityTypeById.
     */
    public static final String getActivityTypeById =
      "select *" + " from mdl_modules" + " where id = :moduleId\n";

    /**
     * The constant getActivityContentById.
     */
    public static final String getActivityContentById =
      "select *" + " from :tableName" + " where id = :activityInstanceId\n";

    /**
     * The constant getActivityContextSQL.
     */
    public static final String getActivityContextSQL = "select *" + " from mdl_context"
      + " where contextlevel = 70" + " and instanceid = :courseModuleId\n";

    /**
     * The constant getActivityFilesSQL.
     */
    public static final String getActivityFilesSQL =
      "select *" + " from mdl_files" + " where contextid = :contextId"
          + " and component = ':componentName'" + " and NOT (filename = '.')\n";

    /**
     * The constant getFeedbackItemSQL.
     */
    public static final String getFeedbackItemSQL = "select *" + " from mdl_feedback_item"
      + " where feedback = :feedbackId" + " order by position ASC\n";

    /**
     * The constant getScormLaunchUrlSQL.
     */
    public static final String getScormLaunchUrlSQL = "select *" + " from mdl_scorm_scoes"
      + " where scorm = :scormId" + " and  NOT (launch = '')\n";

    /**
     * The constant getActivityStatusForActivitiesInCourseSQL.
     */
    public static final String getActivityStatusForActivitiesInCourseSQL =
      "select cm.id, cmc.completionstate, cm.availability, cm.course, cmc.timemodified"
          + " from mdl_course_modules as cm" + " left join mdl_course_modules_completion as cmc"
          + " on cmc.coursemoduleid = cm.id" + " and userid = :userId" + " where cm.course in"
          + " (select id from mdl_course where id = :courseId ) and cm.visible = 1\n";

    /**
     * The constant getActivityStatusForActivitiesInSameCourseSQL.
     */
    public static final String getActivityStatusForActivitiesInSameCourseSQL =
      "select cm.id, cmc.completionstate, cm.availability, cm.course"
          + " from mdl_course_modules as cm" + " left join mdl_course_modules_completion as cmc"
          + " on cmc.coursemoduleid = cm.id" + " and userid = :userId" + " where cm.course in"
          + " (select course from mdl_course_modules where id = :moduleId ) and cm.visible = 1\n";

    /**
     * The constant getActivityCompletionWithCourseModuleIdInAndActivityIsComplete.
     */
    public static final String getActivityCompletionWithCourseModuleIdInAndActivityIsComplete =
      "select id" + " from mdl_course_modules_completion" + " where coursemoduleid in :set"
          + " and completionstate > 0" + " and userid = :userId\n";

    /**
     * The constant getGradeItemsGradeByUser.
     */
    public static final String getGradeItemsGradeByUser =
      "select *" + " from mdl_grade_grades" + " where itemid in :set" + " and userid = :userId\n";

    /**
     * The constant getRequiredactivitiesByCompletionCriteriaSQL.
     */
    public static final String getRequiredactivitiesByCompletionCriteriaSQL =
      "select ccc.moduleinstance" + " from mdl_course_completion_criteria as ccc"
          + " where ccc.course = :courseId" + " and ccc.criteriatype = :criteriatype\n";
}
