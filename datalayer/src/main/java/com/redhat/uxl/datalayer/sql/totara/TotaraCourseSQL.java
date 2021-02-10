package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara course sql.
 */
public class TotaraCourseSQL {

    /**
     * The constant simpleCourseSearch.
     */
    public static final String simpleCourseSearch =
      "select id, fullname \n" + "from mdl_course \n" + "where fullname like ':search'";

    /**
     * The constant simpleCategorySearch.
     */
    public static final String simpleCategorySearch =
      "select id, name \n" + "from mdl_course_categories\n" + "where name like ':search'";

    /**
     * The constant isUserEnrolledInCourseSQL.
     */
    public static final String isUserEnrolledInCourseSQL = "SELECT distinct(c.id)"
      + " from mdl_user_enrolments as ue" + " join mdl_enrol as e on e.id = ue.enrolid"
      + " join mdl_course as c on c.id = e.courseid"
      + " where ue.userid = :totaraUserId and c.id = :totaraCourseId\n";

    /**
     * The constant enrolledUserCourseSQL.
     */
    public static final String enrolledUserCourseSQL = "SELECT distinct(c.id)," + " c.fullname,"
      + " c.shortname" + " from mdl_user_enrolments as ue"
      + " join mdl_enrol as e on e.id = ue.enrolid" + " join mdl_course as c on c.id = e.courseid"
      + " where ue.userid = :totaraUserId and c.id = :totaraCourseId\n";

    /**
     * The constant getCoursesForProgram.
     */
    public static final String getCoursesForProgram =
      "select c.id as courseid, c.fullname as coursefullname, c.summary as coursedescription, \n"
          + "cs.id as coursesetid, cs.timeallowed, cs.label as coursesetlabel, cs.sortorder as coursesetorder, cs.nextsetoperator as coursesetnextsetoperator, cs.completiontype as coursesetcompletiontype, \n"
          + "cs.mincourses as coursesetmincourses, cs.coursesumfield as coursesetsumfield, cs.coursesumfieldtotal as coursesetsumfieldtotal, cs.description as description, \n"
          + "(select count(*) from mdl_course_modules where course = c.id) as mycount from mdl_prog_courseset as cs, mdl_prog_courseset_course csc, mdl_course c \n"
          + "where cs.programid = :programId and c.id = csc.courseid and csc.coursesetid = cs.id order by cs.sortorder ASC, csc.id asc";

    /**
     * The constant getCoursesForProgramWithStatus.
     */
    public static final String getCoursesForProgramWithStatus =
      "select c.id as courseid, c.fullname as coursefullname, c.summary as coursedescription, \n"
          + "cs.id as coursesetid, cs.timeallowed, cs.label as coursesetlabel, cs.sortorder as coursesetorder, cs.nextsetoperator as coursesetnextsetoperator, cs.completiontype as coursesetcompletiontype, \n"
          + "cs.mincourses as coursesetmincourses, cs.coursesumfield as coursesetsumfield, cs.coursesumfieldtotal as coursesetsumfieldtotal, pc.status as coursesetstatus, \n"
          + "cs.description as description, (select count(*) from mdl_course_modules where course = c.id) as mycount from mdl_prog_courseset as cs \n"
          + "left join mdl_prog_completion as pc on pc.coursesetid = cs.id and pc.userid = :userId, \n"
          + "mdl_prog_courseset_course csc, mdl_course c \n"
          + "where cs.programid = :programId and c.id = csc.courseid and csc.coursesetid = cs.id order by cs.sortorder ASC, csc.id asc \n";
    /**
     * The constant SQL_INSERT_ACTIVITY_MESSAGE.
     */
    public static final String SQL_INSERT_ACTIVITY_MESSAGE = "insert into mdl_textentry_values (textentryid, userid, textvalue, timecreated, timemodified) values (?,?,?,?,?)";
    /**
     * The constant SQL_FIND_ACTIVITY_ID.
     */
    public static final String SQL_FIND_ACTIVITY_ID = "select id from mdl_course_modules where instance = ? and course = ?";
    /**
     * The constant SQL_INSERT_COMPLETED_ACTIVITY.
     */
    public static final String SQL_INSERT_COMPLETED_ACTIVITY = "insert into mdl_course_modules_completion (coursemoduleid, userid, completionstate, viewed, timecompleted, timemodified) values (?,?,1,1, (SELECT EXTRACT( EPOCH FROM now() )), (SELECT EXTRACT( EPOCH FROM now() )))";

    /**
     * The constant SQL_SELECT_CECREDITS.
     */
    public static final String SQL_SELECT_CECREDITS = "select sum(credits) from mdl_local_cecredit_earned where timeearned >= extract(epoch from timestamp ':startDate 00:00:00') and timeearned <=  extract(epoch from timestamp ':endDate 00:00:00') and userid = ? and status in (98,99,100)";

    /**
     * The constant SQL_SELECT_MDL_COURSE.
     */
    public static final String SQL_SELECT_MDL_COURSE = "SELECT c.ID as id, cast(COALESCE (de.data, '0') as int) as ceCredits, c.FULLNAME as fullName, c.SHORTNAME as shortName, c.SUMMARY as summary, c.coursetype as courseType, c.enddate as dueDate, c.timecreated as timeCreated, d.data as duration "
      + " FROM MDL_COURSE c \n" + " INNER JOIN MDL_COURSE_INFO_DATA d on d.courseid = c.id \n"
      + " INNER JOIN MDL_COURSE_INFO_FIELD f on f.shortname = 'duration' and d.fieldid = f.id \n"
      + " LEFT OUTER JOIN MDL_COURSE_INFO_DATA de on de.courseid = c.id and de.fieldid = (select id from mdl_course_info_field where shortname = 'cecredits')\n"
      + " LEFT OUTER JOIN MDL_COURSE_INFO_DATA da on da.courseid = c.id and da.fieldid = (select id from mdl_course_info_field where shortname = 'xlvisible')\n"
      + " WHERE " + " c.audiencevisible != 3 and " // Excluded no users value
      + "(da.data is null or da.data='0') ";
    /**
     * The constant SQL_SELECT_MDL_COURSE_PAGED.
     */
    public static final String SQL_SELECT_MDL_COURSE_PAGED = SQL_SELECT_MDL_COURSE + " LIMIT ? OFFSET ?";

    /**
     * The constant SQL_SELECT_COURSES_BY_ID.
     */
    public static final String SQL_SELECT_COURSES_BY_ID = "SELECT c.ID as id, cast(COALESCE (de.data, '0') as int) as ceCredits, c.FULLNAME as fullName, c.SHORTNAME as shortName, c.SUMMARY as summary, c.coursetype as courseType, c.enddate as dueDate, c.timecreated as timeCreated, d.data as duration "
      + " FROM MDL_COURSE c \n" + " INNER JOIN MDL_COURSE_INFO_DATA d on d.courseid = c.id \n"
      + " INNER JOIN MDL_COURSE_INFO_FIELD f on f.shortname = 'duration' and d.fieldid = f.id \n"
      + " LEFT OUTER JOIN MDL_COURSE_INFO_DATA de on de.courseid = c.id and de.fieldid = (select id from mdl_course_info_field where shortname = 'cecredits')\n"
      + " LEFT OUTER JOIN MDL_COURSE_INFO_DATA da on da.courseid = c.id and da.fieldid = (select id from mdl_course_info_field where shortname = 'xlvisible')\n"
      + " WHERE c.id in (:ids)";

    /**
     * The constant SQL_SELECT_MDL_PROGRAM_BASE.
     */
    public static final String SQL_SELECT_MDL_PROGRAM_BASE = "select  program.programId as programId, program.programTrackingId as programTrackingId, program.moduleId as moduleId, cmc.id as moduleTrackingId, cc.id as courseTrackingId, cmc.id as moduleTrackingId, program.userid as userId, program.courseid as courseId, program.courseModuleId as courseModuleId, \n"
      + "program.programStatus as programStatus, program.programName as programName, program.programSummary as programSummary,\n"
      + " program.courseFullName as courseFullName, program.courseShortName as courseshortName, program.courseSummary as courseSummary, cc.status as courseStatus, \n"
      + " program.moduleName as moduleName, \n" + " cmc.completionstate as moduleStatus\n" + " from (\n"
      + "select u.id as userid, pc.id as programTrackingId, p.id as programId, cs.id, c.id as courseid, csc.id, m.id as moduleId,\n"
      + " u.username as userName, pc.status as programStatus, p.fullname as programName, m.name as moduleName,\n"
      + "p.summary as programSummary, c.fullname as courseFullName, c.shortname as courseShortName, c.summary as courseSummary, cm.id as courseModuleId\n"
      + "from mdl_prog_completion pc, mdl_user u, mdl_prog p, mdl_prog_courseset cs,  mdl_course c, mdl_prog_courseset_course csc, mdl_course_modules cm, mdl_modules m\n"
      + "where pc.programid = ?\n" + "and pc.userid = u.id\n" + "and pc.programid = p.id\n"
      + "and cs.programid = p.id\n" + "and csc.coursesetid = cs.id\n" + "and csc.courseid = c.id\n"
      + "and pc.coursesetid = 0 \n" + "and cm.course = c.id\n" + "and cm.module = m.id) program\n"
      + "left outer join mdl_course_completions cc on program.courseid = cc.course and program.userid = cc.userid\n"
      + "left outer join mdl_course_modules_completion cmc on program.courseModuleId = cmc.coursemoduleid and cc.userid = cmc.userid\n"
      + "where ";

    /**
     * The constant SQL_SELECT_MDL_PROGRAM.
     */
    public static final String SQL_SELECT_MDL_PROGRAM = SQL_SELECT_MDL_PROGRAM_BASE + "program.userid = ?\n"
      + "order by userid, courseid\n";

    /**
     * The constant SQL_SELECT_MDL_PROGRAM_BY_USERS.
     */
    public static final String SQL_SELECT_MDL_PROGRAM_BY_USERS = SQL_SELECT_MDL_PROGRAM_BASE + "program.userid in (:userIds) \n "
      + "order by userid, courseid\n";;

    /**
     * The constant SQL_SELECT_MDL_PROGRAM_BY_USER.
     */
    public static final String SQL_SELECT_MDL_PROGRAM_BY_USER = " select p.id as programId, p.fullname as programName, p.shortname as programShortName, p.Summary as programSummary, MAX(pc.status) as programStatus, MIN(pc.timestarted) as timeEnrolled, MAX(pc.timecompleted) as completedDate\n"
      + "                From mdl_prog p\n"
      + "                join mdl_prog_completion pc on pc.programid = p.id\n"
      + "                and pc.coursesetid = 0\n" + "                where pc.userid = ?\n"
      + "                group by (p.id);";

    /**
     * The constant SQL_SELECT_MDL_PROGRAMS_BY_USERS_IN_PROGRESS.
     */
    public static final String SQL_SELECT_MDL_PROGRAMS_BY_USERS_IN_PROGRESS = "select programId, programName, programShortName,programSummary, SUM(NULLIF(d.data, '')::float) as duration, MAX(coalesce(base.timemodified,0)) as timemodified from\n"
      + "        (\n"
      + "                select p.id as programId, p.fullname as programName, p.shortname as programShortName, p.Summary as programSummary, cm.course as course, MAX(coalesce(cmc.timemodified,0)) as timemodified\n"
      + "                        from mdl_prog_completion pco,\n"
      + "                                mdl_prog p,\n"
      + "                                mdl_prog_courseset pc,\n"
      + "                                mdl_prog_courseset_course pcc,\n" +
      "                                mdl_course_modules cm\n"
      + "                                        left outer join mdl_course_modules_completion cmc on cm.id = cmc.coursemoduleid and cmc.userid = ?\n"
      + "                        where pco.userid = ?\n" + "                                and pco.status != 1\n"
      + "                                and pco.timedue = -1\n"
      + "                                and pco.programid = p.id \n" +
      "                                and pco.programid = pc.programid\n"
      + "                                and pc.id = pcc.coursesetid \n"
      + "                              and pco.coursesetid = 0\n"
      + "                                and pcc.courseid = cm.course\n " +
      "                        group by p.id, cm.course\n" + "        ) as base,\n"
      + "        MDL_COURSE_INFO_DATA d,\n" + "        MDL_COURSE_INFO_FIELD f\n" + "       " + "        WHERE\n"
      + "                f.shortname = 'duration'\n" + "                and d.courseid = base.course\n"
      + "                and d.fieldid = f.id \n"
      + "        group by programId, programName, programShortName,programSummary order by timemodified DESC LIMIT ? OFFSET ?";

    /**
     * The constant SQL_SELECT_COUNT_MDL_PROGRAMS_BY_USERS_IN_PROGRESS.
     */
    public static final String SQL_SELECT_COUNT_MDL_PROGRAMS_BY_USERS_IN_PROGRESS = "select count(s.programId) from ("
      + SQL_SELECT_MDL_PROGRAMS_BY_USERS_IN_PROGRESS + ") as s";

    /**
     * The constant SQL_SELECT_ACTIVE_MDL_PROGRAM.
     */
    public static final String SQL_SELECT_ACTIVE_MDL_PROGRAM = " select p.id as programId, p.fullname as programName, p.shortname as programShortName  \n"
      + " From mdl_prog p \n" + " where p.visible = '1' ORDER BY p.fullname, p.shortname;";

    /**
     * The constant SQL_SELECT_ACTIVE_MDL_PROGRAM_PAGED.
     */
    public static final String SQL_SELECT_ACTIVE_MDL_PROGRAM_PAGED = " select distinct p.id as programId, p.fullname as programName, p.shortname as programShortName, p.summary as programSummary, p.timecreated as timeCreated, SUM(NULLIF(d.data, '')::float) as duration   \n"
      + " From mdl_prog p \n" + "  JOIN mdl_prog_courseset pc on pc.programid = p.id \n"
      + "  JOIN mdl_prog_courseset_course c on c.coursesetid = pc.id \n"
      + "  JOIN MDL_COURSE_INFO_DATA d on d.courseid = c.courseid \n"
      + "  JOIN MDL_COURSE_INFO_FIELD f on f.shortname = 'duration' and d.fieldid = f.id \n"
      + " where p.audiencevisible != 3 group by p.id LIMIT ? OFFSET ?";

    /**
     * The constant SQL_SELECT_ACTIVE_MDL_PROGRAM_BY_ID_PAGED.
     */
    public static final String SQL_SELECT_ACTIVE_MDL_PROGRAM_BY_ID_PAGED = " select p.id as programId, p.fullname as programName, p.shortname as programShortName, p.summary as programSummary, p.timecreated as timeCreated, SUM(NULLIF(d.data, '')::float) as duration   \n"
      + " From mdl_prog p \n" + "  JOIN mdl_prog_courseset pc on pc.programid = p.id \n"
      + "  JOIN mdl_prog_courseset_course c on c.coursesetid = pc.id \n"
      + "  JOIN MDL_COURSE_INFO_DATA d on d.courseid = c.courseid \n"
      + "  JOIN MDL_COURSE_INFO_FIELD f on f.shortname = 'duration' and d.fieldid = f.id \n"
      + " where p.audiencevisible != 3 and p.id in (:programIds) group by p.id LIMIT (:limit) OFFSET (:offset)";

    /**
     * The constant SQL_SELECT_COURSE_ENROLLMENTS.
     */
    public static final String SQL_SELECT_COURSE_ENROLLMENTS = "select cc.id as enrollmentId, cc.course as courseId, c.fullname as fullName, cc.status as status, d.data as ceCredits, cc.timecompleted as completedDate, cc.timeenrolled as timeEnrolled \n"
      + " from mdl_course_completions cc,\n" + " mdl_course c \n"
      + " INNER JOIN MDL_COURSE_INFO_DATA d on d.courseid = c.id \n"
      + " INNER JOIN MDL_COURSE_INFO_FIELD f on f.shortname = 'cecredits' and d.fieldid = f.id \n"
      + " where cc.userid = ? and cc.course = c.id;";

    /**
     * The constant SQL_SELECT_COURSE_ENROLLMENTS_IN_PROGRESS.
     */
    public static final String SQL_SELECT_COURSE_ENROLLMENTS_IN_PROGRESS = "SELECT cc.id as enrollmentId, \n"
      + "cc.course as courseId, \n" + "    c.fullname as fullName, \n" + "    cc.status as status, \n"
      + "    c.summary as summary, \n" + "    d.data as duration, \n" + "    cast(dd.data as int) as enddate, \n"
      + "    cast(COALESCE (de.data, '0') as int) as ceCredits, \n"
      + "    MAX(cmc.timemodified) as timemodified\n" + "FROM mdl_course_completions cc\n"
      + "JOIN mdl_course c ON cc.course = c.id\n"
      + "LEFT OUTER JOIN mdl_course_info_data dd on dd.courseid = c.id and dd.fieldid = (SELECT id FROM mdl_course_info_field where shortname = 'courseenddate')\n"
      + "LEFT OUTER JOIN mdl_course_info_data de on de.courseid = c.id and de.fieldid = (SELECT id FROM mdl_course_info_field where shortname = 'cecredits')\n"
      + "LEFT OUTER JOIN mdl_course_info_data d on d.courseid = c.id and de.fieldid = (SELECT id FROM mdl_course_info_field where shortname = 'duration')\n"
      + "JOIN mdl_course_modules cm ON cm.course = c.id\n"
      + "LEFT OUTER JOIN mdl_course_modules_completion cmc on cm.id = cmc.coursemoduleid and cmc.userid = cc.userid\n"
      + "WHERE cc.userid = ? and cc.status < 50\n"
      + "GROUP BY cc.id, cc.course, c.fullname, cc.status, d.data, dd.data, c.summary, de.data\n"
      + "ORDER BY timemodified DESC limit ? offset ?";

    /**
     * The constant SQL_SELECT_COUNT_COURSE_ENROLLMENTS_IN_PROGRESS.
     */
    public static final String SQL_SELECT_COUNT_COURSE_ENROLLMENTS_IN_PROGRESS = "select count (s.courseId) from ("
      + SQL_SELECT_COURSE_ENROLLMENTS_IN_PROGRESS + ") as s;";

    /**
     * The constant SQL_SELECT_ACTIVITY_ID_BY_INSTANCE.
     */
    public static final String SQL_SELECT_ACTIVITY_ID_BY_INSTANCE = " select cm.id as id, cm.course as courseId \n"
      + " From mdl_course_modules cm\n" + " where cm.instance = ? limit 1\n";

    /**
     * The constant SQL_SELECT_ACTIVITY_ENROLLMENTS.
     */
    public static final String SQL_SELECT_ACTIVITY_ENROLLMENTS = " select cm.id as id, cmc.id as enrollmentId, cm.course as courseId, completionstate as status  \n"
      + " From mdl_course_modules_completion cmc, mdl_course_modules cm\n" + " where cmc.userid = ?\n"
      + " and cmc.coursemoduleid = cm.id";

    /**
     * The constant SQL_SELECT_ACTIVTIES_FOR_USER_COURSES.
     */
    public static final String SQL_SELECT_ACTIVTIES_FOR_USER_COURSES = "select id as id, course as courseId from mdl_course_modules where course in (      \n"
      + "        select cc.course from mdl_course_completions cc,\n" + "                mdl_course c\n"
      + "                where cc.userid = ? and cc.course = c.id\n" + "        )\n" + "        and visible = 1";

    /**
     * The constant SQL_SELECT_COMPLETED_ACTIVITY_ENROLLMENTS.
     */
    public static final String SQL_SELECT_COMPLETED_ACTIVITY_ENROLLMENTS = " select cmc.id as enrollmentId, cm.course as courseId, completionstate as status  \n"
      + " From mdl_course_modules_completion cmc, mdl_course_modules cm\n" + " where cmc.userid = ?\n"
      + " and cmc.coursemoduleid = cm.id";

    /**
     * The constant SQL_SELECT_COUNT_PROGRAM.
     */
    public static final String SQL_SELECT_COUNT_PROGRAM = "select COUNT(distinct p.id) From mdl_prog p \n"
      + "         JOIN mdl_prog_courseset pc on pc.programid = p.id \n"
      + "         JOIN mdl_prog_courseset_course c on c.coursesetid = pc.id \n"
      + "         JOIN MDL_COURSE_INFO_DATA d on d.courseid = c.courseid\n"
      + "        JOIN MDL_COURSE_INFO_FIELD f on f.shortname = 'duration' and d.fieldid = f.id\n"
      + "         where p.audiencevisible != 3";

    /**
     * The constant SQL_SELECT_COUNT_PROGRAM_BY_TAG.
     */
    public static final String SQL_SELECT_COUNT_PROGRAM_BY_TAG = "SELECT COUNT(p.ID) as count FROM MDL_PROG p \n"
      + " left join mdl_tag_instance t on t.itemid = p.id \n" + " where t.itemtype = 'program' and t.tagid = ?"
      + " and p.VISIBLE = '1' \n" + "and p.audiencevisible = 2";

    /**
     * The constant SQL_SELECT_COUNT_COURSE.
     */
    public static final String SQL_SELECT_COUNT_COURSE = "SELECT COUNT(c.ID) as count FROM MDL_COURSE c ";

    /**
     * The constant SQL_SELECT_DUPE_TAGS_IDS.
     */
    public static final String SQL_SELECT_DUPE_TAGS_IDS = "select t.* from mdl_tag t " + "   where name ilike ( \n"
      + "   select name from mdl_tag t2 where t2.id = ? \n" + ");";

    /**
     * The constant SQL_SELECT_COUNT_COURSE_BY_TAG.
     */
    public static final String SQL_SELECT_COUNT_COURSE_BY_TAG = "SELECT COUNT(c.ID) as count FROM MDL_COURSE c \n"
      + " left join mdl_tag_instance t on t.itemid = c.id \n"
      + " where t.itemtype = 'course' and t.tagid in (:ids)" + " and c.VISIBLE = '1' \n"
      + "and c.audiencevisible = 2";

    /**
     * The constant SQL_SELECT_PROGRAM_BY_TAG.
     */
    public static final String SQL_SELECT_PROGRAM_BY_TAG = " select p.id as programId, p.fullname as programName, p.shortname as programShortName  \n"
      + " From mdl_prog p \n" + " left join mdl_tag_instance t on t.itemid = p.id \n"
      + " where t.itemtype = 'program' and t.tagid = ?"
      + " and p.VISIBLE = '1' and p.audiencevisible = 2 LIMIT ?";

    /**
     * The constant SQL_SELECT_MDL_PROGRAM_BY_COURSE_ID.
     */
    public static final String SQL_SELECT_MDL_PROGRAM_BY_COURSE_ID = " select p.id as programId, p.fullname as programName, p.shortname as programShortName, p.summary as programSummary \n"
      + " From mdl_prog p \n" + " JOIN mdl_prog_courseset pcc on pcc.programid = p.id\n"
      + " JOIN mdl_prog_courseset_course c on c.coursesetid = pcc.id\n" + " where c.courseid = ? LIMIT 1";

    /**
     * The constant SQL_SELECT_MDL_PROGRAM_BY_PROGRAM_ID.
     */
    public static final String SQL_SELECT_MDL_PROGRAM_BY_PROGRAM_ID = " select p.audiencevisible as audienceVisible, p.id as programId, p.fullname as programName, p.shortname as programShortName, p.summary as programSummary, p.enrolselfenabled as enrolSelfEnabled, SUM(NULLIF(d.data, '')::float) as duration, (select NULLIF(d.data, '0') from mdl_prog_info_data d, MDL_prog_INFO_FIELD f where d.programid = p.id and f.shortname = 'hidecourseset' and d.fieldid = f.id) as hidesets \n"
      + " From mdl_prog p \n" + " JOIN mdl_prog_courseset pcc on pcc.programid = p.id\n"
      + " JOIN mdl_prog_courseset_course c on c.coursesetid = pcc.id\n"
      + " JOIN MDL_COURSE_INFO_DATA d on d.courseid = c.courseid \n"
      + " JOIN MDL_COURSE_INFO_FIELD f on f.shortname = 'duration' and d.fieldid = f.id \n"
      + " where p.id = ? group by p.id LIMIT 1";

    /**
     * The constant SQL_SELECT_MDL_PROGRAM_ENROLMENTS.
     */
    public static final String SQL_SELECT_MDL_PROGRAM_ENROLMENTS = "select mc.userid as userId, p.id as programId, p.fullname as programName, p.shortname as programShortName, p.summary as programSummary\n"
      + "From mdl_prog p, mdl_prog_completion mc\n" + "where mc.programid = p.id\n" + "and mc.timecreated > ? \n"
      + "and mc.coursesetid = 0";

    /**
     * The constant SQL_SELECT_MDL_PROGRAM_OVERDUE.
     */
    public static final String SQL_SELECT_MDL_PROGRAM_OVERDUE = "select DISTINCT mc.userid as userId, p.id as programId, p.fullname as programName, p.shortname as programShortName, p.summary as programSummary\n"
      + "From mdl_prog p, mdl_prog_completion mc\n" + "where mc.programid = p.id\n" + "and mc.status in (0,2) \n"
      + "and  (timedue > -1 and timedue < ?)";

    /**
     * The constant SQL_SELECT_MDL_PROGRAM_HTML_BLOCK.
     */
    public static final String SQL_SELECT_MDL_PROGRAM_HTML_BLOCK = "SELECT bi.configdata FROM mdl_block_instances bi\n"
      + "    JOIN mdl_context ctx ON bi.parentcontextid = ctx.id AND ctx.contextlevel = 45\n"
      + "    JOIN mdl_prog p ON ctx.instanceid = p.id\n" + "WHERE bi.blockname = 'html' \n"
      + "    AND bi.pagetypepattern = 'totara-program-view'\n" + "    AND ctx.instanceid = ?";

    /**
     * The constant SQL_SELECT_MDL_COURSE_BY_COURSE_ID.
     */
    public static final String SQL_SELECT_MDL_COURSE_BY_COURSE_ID = "select c.id as id, c.fullName as fullName, c.shortName as shortName, c.summary as summary, c.coursetype as courseType, c.enddate as dueDate \n"
      + " from mdl_course c \n" + " where c.visible = '1' and c.id = ? LIMIT 1";

    /**
     * The constant SQL_SELECT_COURSE_BY_TAG.
     */
    public static final String SQL_SELECT_COURSE_BY_TAG = "SELECT c.ID as id, c.FULLNAME as fullName, c.SHORTNAME as shortName, c.SUMMARY as summary, c.coursetype as courseType, c.enddate as dueDate, d.data as duration FROM MDL_COURSE c \n"
      + " left join mdl_tag_instance t on t.itemid = c.id \n"
      + " INNER JOIN MDL_COURSE_INFO_DATA d on d.courseid = c.id \n"
      + " INNER JOIN MDL_COURSE_INFO_FIELD f on f.shortname = 'duration' and d.fieldid = f.id \n"
      + " where t.itemtype = 'course' and t.tagid in (:ids)"
      + " and c.VISIBLE = '1' and c.audiencevisible = 2 LIMIT ? OFFSET ?";

    /**
     * The constant SQL_SELECT_ACTIVITIES_BY_PROGRAM_USER.
     */
    public static final String SQL_SELECT_ACTIVITIES_BY_PROGRAM_USER = "\n"
      + "select  forum.name as forumName, scorm.name as scormName, quiz.name as quizName, program.programId as programId, program.programTrackingId as programTrackingId, program.moduleId as moduleId, cmc.id as moduleTrackingId, cc.id as courseTrackingId, cmc.id as moduleTrackingId, program.userid as userId, program.courseid as courseId, program.courseModuleId as courseModuleId,\n"
      + "        program.programStatus as programStatus, program.programName as programName, program.programSummary as programSummary,\n"
      + "         program.courseFullName as courseFullName, program.courseShortName as courseshortName, program.courseSummary as courseSummary, cc.status as courseStatus,\n"
      + "         program.moduleName as moduleName,\n" + "         cmc.completionstate as moduleStatus\n"
      + "         from (\n"
      + "        select u.id as userid, pc.id as programTrackingId, p.id as programId, cs.id, c.id as courseid, csc.id, m.id as moduleId,\n"
      + "         u.username as userName, pc.status as programStatus, p.fullname as programName, m.name as moduleName,\n"
      + "        p.summary as programSummary, c.fullname as courseFullName, c.shortname as courseShortName, c.summary as courseSummary, cm.id as courseModuleId, cm.instance as instanceId \n"
      + "        from mdl_prog_completion pc, mdl_user u, mdl_prog p, mdl_prog_courseset cs,  mdl_course c, mdl_prog_courseset_course csc, mdl_course_modules cm, mdl_modules m\n"
      + "        where \n" + "         pc.userid = u.id\n" + "        and pc.programid = p.id\n"
      + "        and cs.programid = p.id\n" + "        and csc.coursesetid = cs.id\n"
      + "        and csc.courseid = c.id\n" + "        and pc.coursesetid = 0\n"
      + "        and cm.course = c.id\n" + "        and cm.module = m.id) program\n"
      + "        left outer join mdl_course_completions cc on program.courseid = cc.course and program.userid = cc.userid\n"
      + "        left outer join mdl_course_modules_completion cmc on program.courseModuleId = cmc.coursemoduleid and cc.userid = cmc.userid\n"
      + "        left outer join mdl_scorm scorm on program.moduleid = 20 and program.instanceId = scorm.id\n"
      + "        left outer join mdl_quiz quiz on program.moduleid = 18 and program.instanceId = quiz.id\n"
      + "        left outer join mdl_forum forum on program.moduleid = 11 and program.instanceId = forum.id\n"
      + "        where program.programId = ? and program.userId = ?";

    /**
     * The constant SQL_SELECT_COMPLETED_ACTIVITY.
     */
    public static final String SQL_SELECT_COMPLETED_ACTIVITY = "\n" + "";

    /**
     * The constant SQL_SELECT_COUNT_COURSE_BY_AUDIENCE.
     */
    public static final String SQL_SELECT_COUNT_COURSE_BY_AUDIENCE = "SELECT COUNT(c.ID) as count FROM MDL_COURSE c \n"
      + " left join mdl_cohort_recommended_item i on i.itemid = c.id \n"
      + " where i.cohortid = ? and c.VISIBLE = '1' ";

    /**
     * The constant SQL_SELECT_COURSE_BY_AUDIENCE.
     */
    public static final String SQL_SELECT_COURSE_BY_AUDIENCE = "SELECT c.ID as id, c.FULLNAME as fullName, c.SHORTNAME as shortName, c.SUMMARY as summary, c.coursetype as courseType, c.enddate as dueDate FROM MDL_COURSE c \n"
      + " left join mdl_cohort_recommended_item i on i.itemid = c.id \n"
      + " where i.cohortid = ? and c.VISIBLE = '1' LIMIT ?";

    /**
     * The constant SQL_SELECT_FACE_TO_FACE_SESSIONS_BY_COURSE_ID.
     */
    public static final String SQL_SELECT_FACE_TO_FACE_SESSIONS_BY_COURSE_ID = "select ftfs.id as sessionId,\n"
      + "            ftfs.facetoface as facetofaceId,\n" + "            ftf.approvaltype as approvaltype,\n"
      + "            ftfs.capacity as capacity,\n" + "            ftfs.normalcost as price, \n"
      + "            ftfsd.roomid as roomId,\n" + "            ftfsd.sessiontimezone as timezone,\n"
      + "            ftfsd.timestart * 1000 as startDate,\n" + "            ftfsd.timefinish * 1000 as endDate\n"
      + "            from mdl_facetoface_sessions as ftfs\n"
      + "            left join mdl_facetoface_sessions_dates as ftfsd on ftfsd.sessionid = ftfs.id\n"
      + "            left join mdl_facetoface as ftf on ftf.id = ftfs.facetoface\n"
      + "            where facetoface in \n"
      + "            ( select instance from mdl_course_modules where course = ? and module = (select id from mdl_modules where name = 'facetoface'))\n"
      + "            order by startdate asc";

    /**
     * The constant SQL_SELECT_FACE_TO_FACE_SESSION_BY_ID.
     */
    public static final String SQL_SELECT_FACE_TO_FACE_SESSION_BY_ID = "select ftf.course as courseId, ftf.name as courseName, ftfs.id as sessionId,\n"
      + "        ftfs.facetoface as facetofaceId,\n" + "        ftfs.capacity as capacity,\n"
      + "        ftfs.normalcost as price,\n" + "        ftfsd.roomid as roomId,\n"
      + "        ftfsd.sessiontimezone as timezone,\n" + "        ftfsd.timestart * 1000 as startDate,\n"
      + "        ftfsd.timefinish * 1000 as endDate\n" + "        from mdl_facetoface ftf\n"
      + "        ,mdl_facetoface_sessions as ftfs\n"
      + "        left join mdl_facetoface_sessions_dates as ftfsd on ftfsd.sessionid = ftfs.id\n"
      + "        where ftfs.facetoface = ftf.id\n" + "        and ftfs.id = ?";

    /**
     * The constant SQL_COURSE_ENROLLED_COMPLETED.
     */
    public static final String SQL_COURSE_ENROLLED_COMPLETED = "select distinct userid from mdl_course_completions pa where course = ?";

    /**
     * The constant SQL_INSERT_FACE_TO_FACE_SIGNUP.
     */
    public static final String SQL_INSERT_FACE_TO_FACE_SIGNUP = "INSERT INTO MDL_FACETOFACE_SIGNUPS (sessionId, userid, notificationtype) values (?,?, 3) RETURNING id";

    /**
     * The constant SQL_INSERT_FACE_TO_FACE_SIGNUP_STATUS.
     */
    public static final String SQL_INSERT_FACE_TO_FACE_SIGNUP_STATUS = "INSERT INTO MDL_FACETOFACE_SIGNUPS_STATUS (signupid, statuscode, superceded, createdby, timecreated) values (?,70,0,0,0) RETURNING id";

    /**
     * The constant SQL_INSERT_COURSE_COMPLETIONS.
     */
    public static final String SQL_INSERT_COURSE_COMPLETIONS = "INSERT INTO mdl_course_completions (userid, course, status) values (?,?,10) RETURNING id";

    /**
     * The constant SQL_SELECT_AUDIENCE_VISIBLE.
     */
    public static final String SQL_SELECT_AUDIENCE_VISIBLE = "select cm.userid from mdl_prog_assignment pa,mdl_cohort_members cm  where pa.assignmenttype = 3 and pa.programid = ? and pa.assignmenttypeid = cm.cohortid and cm.userid = ?";
    /**
     * The constant SQL_SELECT_AUDIENCE_VISIBLE_COHORT.
     */
    public static final String SQL_SELECT_AUDIENCE_VISIBLE_COHORT = "select distinct cm.userid from mdl_cohort_visibility cv,mdl_cohort_members cm  where cv.instancetype in (45,50) and cv.instanceid = ? and cv.cohortid = cm.cohortid";
    /**
     * The constant SQL_SELECT_AUDIENCE_VISIBLE_COHORT_BY_USER.
     */
    public static final String SQL_SELECT_AUDIENCE_VISIBLE_COHORT_BY_USER = "select distinct cm.userid from mdl_cohort_visibility cv,mdl_cohort_members cm  where cv.instancetype in (45,50) and cv.instanceid = ? and cm.userid = ? and cv.cohortid = cm.cohortid";

    /**
     * The constant SQL_SELECT_USER_ENROLLED.
     */
    public static final String SQL_SELECT_USER_ENROLLED = "select pa.userid from mdl_prog_user_assignment pa where pa.userid = ? and pa.programid = ?";

    /**
     * The constant SQL_SELECT_MDL_PROGRAM_COMPLETIONS_BY_USER.
     */
    public static final String SQL_SELECT_MDL_PROGRAM_COMPLETIONS_BY_USER = "select MAX(mc.timecompleted) as completedDate, p.id as programId, p.fullname as programName, p.shortname as programShortName, p.summary as programSummary, MAX(mc.status) as programStatus\n"
      + "        From mdl_prog p\n" + "        Join mdl_prog_completion mc on p.id = mc.programid\n"
      + "        where mc.userid = ?\n" + "        and mc.status = 1 and mc.coursesetid = 0\n"
      + "        group by (p.id);";

    /**
     * The constant SQL_SELECT_COURSE_ENROLLMENTS_COMPLETED.
     */
    public static final String SQL_SELECT_COURSE_ENROLLMENTS_COMPLETED = "select cc.timecompleted as completedDate, cc.id as enrollmentId, cc.course as courseId, c.fullname as fullName, cc.status as status \n"
      + " from mdl_course_completions cc,\n" + " mdl_course c \n"
      + " where cc.userid = ? and cc.status > 49 and cc.course = c.id;";

    /**
     * The constant SQL_SELECT_PROGRAM_VISIBLE.
     */
    public static final String SQL_SELECT_PROGRAM_VISIBLE = "select audiencevisible from mdl_prog where id = ?";

    /**
     * The constant SQL_SELECT_COURSE_VISIBLE.
     */
    public static final String SQL_SELECT_COURSE_VISIBLE = "select audiencevisible from mdl_course where id = ?";
}
