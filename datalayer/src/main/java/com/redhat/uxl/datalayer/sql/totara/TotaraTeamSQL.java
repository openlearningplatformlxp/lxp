package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara team sql.
 */
public class TotaraTeamSQL {

    /**
     * The constant SQL_SELECT_COUNT_TEAM_MEMBERS.
     */
    public static final String SQL_SELECT_COUNT_TEAM_MEMBERS = "select count(u.id)"
        + "from mdl_user u where id in (select userid from mdl_job_assignment where u.suspended = 0 and u.deleted = 0 and managerjaid = (select id from mdl_job_assignment where userid = ?))\n";

    /**
     * The constant SQL_SELECT_TEAM_MANGERS_WITH_COUNTS.
     */
    public static final String SQL_SELECT_TEAM_MANGERS_WITH_COUNTS =
        "select u.id as id, firstname as firstName, lastname as lastName, email as email, city as city, country as country, lang as language,\n"
            + "(select count(*) from mdl_prog_completion pc where pc.userid = u.id) programCount,\n"
            + "(select count(*) from mdl_course_completions cc where cc.userid = u.id) courseCount,\n"
            + "(select count(*) from mdl_course_modules_completion mc where mc.userid = u.id) activityCount\n"
            + "from mdl_user u where u.suspended = 0 and u.deleted = 0 and id in (select userid from mdl_job_assignment where managerjaid = (select id from mdl_job_assignment where userid = ?))\n"
            + "ORDER BY firstName, lastname";

    /**
     * The constant SQL_SELECT_MDL_PROGRAM_BASE.
     */
    public static final String SQL_SELECT_MDL_PROGRAM_BASE =
        "select  program.programId as programId, program.programTrackingId as programTrackingId, program.moduleId as moduleId, cmc.id as moduleTrackingId, cc.id as courseTrackingId, cmc.id as moduleTrackingId, program.userid as userId, program.courseid as courseId, program.courseModuleId as courseModuleId, \n"
            + "program.programStatus as programStatus, program.programName as programName, program.programSummary as programSummary,\n"
            + " program.courseFullName as courseFullName, program.courseShortName as courseshortName, program.courseSummary as courseSummary, cc.status as courseStatus, \n"
            + " program.moduleName as moduleName, \n"
            + " cmc.completionstate as moduleStatus, program.timecompleted as completedDate, program.timestarted as timeEnrolled \n"
            + " from (\n"
            + "select u.id as userid, pc.id as programTrackingId, p.id as programId, cs.id, c.id as courseid, csc.id, m.id as moduleId,\n"
            + " u.username as userName, pc.status as programStatus, p.fullname as programName, m.name as moduleName,\n"
            + "p.summary as programSummary, c.fullname as courseFullName, c.shortname as courseShortName, c.summary as courseSummary, cm.id as courseModuleId, pc.timecompleted, pc.timestarted \n"
            + "from mdl_prog_completion pc, mdl_user u, mdl_prog p, mdl_prog_courseset cs,  mdl_course c, mdl_prog_courseset_course csc, mdl_course_modules cm, mdl_modules m\n"
            + "where " + " pc.userid = u.id\n" + "and pc.programid = p.id\n"
            + "and cs.programid = p.id\n" + "and csc.coursesetid = cs.id\n"
            + "and csc.courseid = c.id\n" + "and pc.coursesetid = 0 \n" + "and cm.course = c.id\n"
            + "and cm.module = m.id) program\n"
            + "left outer join mdl_course_completions cc on program.courseid = cc.course and program.userid = cc.userid\n"
            + "left outer join mdl_course_modules_completion cmc on program.courseModuleId = cmc.coursemoduleid and cc.userid = cmc.userid\n"
            + "where ";

    /**
     * The constant SQL_SELECT_MDL_PROGRAM.
     */
    public static final String SQL_SELECT_MDL_PROGRAM =
        SQL_SELECT_MDL_PROGRAM_BASE + "program.userid = ?\n" + "order by userid, courseid\n";

    /**
     * The constant SQL_SELECT_MDL_PROGRAM_BY_USERS.
     */
    public static final String SQL_SELECT_MDL_PROGRAM_BY_USERS = SQL_SELECT_MDL_PROGRAM_BASE
        + "program.userid = :replaceme \n " + "order by userid, courseid\n";

    /**
     * The constant SQL_SELECT_MDL_COURSE_BY_USERS.
     */
    public static final String SQL_SELECT_MDL_COURSE_BY_USERS =
        "select cc.timeenrolled as timeEnrolled, c.fullname as fullName, cc.userId as userId, cc.course as course, cc.status as status, u.firstname as firstName, u.lastname as lastName, c.id as courseId, c.shortname as shortName, c.summary as description, (select data from mdl_course_info_data where courseid = c.id and fieldid = (select id from mdl_course_info_field where shortname = 'modeofdelivery2')) as type, cc.timecompleted as completedDate\n"
            + "        from mdl_course_completions cc,\n" + "                mdl_course c,\n"
            + "                mdl_user u\n" + "        where userid =  :replaceme\n"
            + "               and cc.course = c.id\n" + "               and cc.userid = u.id";

}
