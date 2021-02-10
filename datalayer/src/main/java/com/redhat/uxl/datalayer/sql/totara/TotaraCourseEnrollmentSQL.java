package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara course enrollment sql.
 */
public class TotaraCourseEnrollmentSQL {

    /**
     * The constant getEnrollmentsByCourseId.
     */
    public static final String getEnrollmentsByCourseId = "select distinct(c.id), \n"
      + "c.fullname, \n" + "c.summary as description, \n" + "cc.timestarted,\n"
      + "cc.timeenrolled, \n" + "cc.timecompleted,\n" + "c.enddate, \n" + "cc.status, \n"
      + "(select count(id) from mdl_course_completion_criteria as ccc where ccc.course = cc.course and criteriatype = 4) as required_activities_count, \n"
      + "(select count(id) from mdl_course_modules_completion where coursemoduleid in (select moduleinstance from mdl_course_completion_criteria as ccc where ccc.course = cc.course and criteriatype = 4) and userid = :totaraUserId) as completed_activities_count,\n"
      + "case \n"
      + "        when(select count(id) from mdl_course_completion_criteria as ccc where ccc.course = cc.course and criteriatype = 4) > 0\n"
      + "        then (select count(id) from mdl_course_modules_completion where coursemoduleid in (select moduleinstance from mdl_course_completion_criteria as ccc where ccc.course = cc.course and criteriatype = 4) and userid = :totaraUserId )/(select count(id) from mdl_course_completion_criteria as ccc where ccc.course = cc.course and criteriatype = 4)::float *100 \n"
      + "else 0\n" + "end as completion_percentage \n" + "from mdl_user_enrolments as ue \n"
      + "join mdl_enrol as e on e.id = ue.enrolid\n" + "join mdl_course as c on c.id = e.courseid\n"
      + "join mdl_course_completions as cc on cc.userid = ue.userid and cc.course = c.id\n"
      + "where ue.userid = :totaraUserId and c.id in (:courseIdSet)\n";
}
