package com.redhat.uxl.datalayer.sql;

/**
 * The type Personal plans sql.
 */
public class PersonalPlansSQL {

    /**
     * The constant SQL_SELECT_PLANS_FOR_USER.
     */
    public static final String SQL_SELECT_PLANS_FOR_USER =
        "SELECT id as programId, name as programName, description as programSummary, userid, duedate as programDueDate FROM personal_plan where userid = ? and archived = ?";
    /**
     * The constant SQL_SELECT_COUNT_PLANS_FOR_USER.
     */
    public static final String SQL_SELECT_COUNT_PLANS_FOR_USER =
        "SELECT count(id) FROM personal_plan where userid = ?";
    /**
     * The constant SQL_SELECT_PLANS_SHARED_WITH_USER.
     */
    public static final String SQL_SELECT_PLANS_SHARED_WITH_USER =
        "SELECT p.id as programId, p.name as programName, p.description as programSummary, p.userid, p.duedate as programDueDate FROM personal_plan p"
            + " join personal_plan_share pps on pps.personal_plan_id = p.id"
            + " where pps.shared_user_id = ? and p.archived = false";
    /**
     * The constant SQL_SELECT_PLANS_SHARED_FROM_USER.
     */
    public static final String SQL_SELECT_PLANS_SHARED_FROM_USER =
        "SELECT distinct(p.id) as programId, p.name as programName, p.description as programSummary, p.userid, p.duedate as programDueDate FROM personal_plan p"
            + " join personal_plan_share pps on pps.personal_plan_id = p.id"
            + " where pps.owner_user_id = ? and type = 'DIRECT_REPORTS'";

    /**
     * The constant SQL_INSERT_PLAN_FOR_USER.
     */
    public static final String SQL_INSERT_PLAN_FOR_USER =
        "INSERT INTO personal_plan( name, description, duedate, userid) VALUES (?, ?, ?, ?) RETURNING ID";
    /**
     * The constant SQL_INSERT_PLAN_SECTION_FOR_USER.
     */
    public static final String SQL_INSERT_PLAN_SECTION_FOR_USER =
        "INSERT INTO personal_plan_section ( planid, name, description, duedate, sort_order) VALUES ( ?, ?, ?, ?, ?) RETURNING ID";
    /**
     * The constant SQL_INSERT_PLAN_SECTION_COURSE_FOR_USER.
     */
    public static final String SQL_INSERT_PLAN_SECTION_COURSE_FOR_USER =
        "INSERT INTO personal_plan_section_course ( personal_plan_section_id, courseid, type, cms, title, description, url, duedate, sort_order) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING ID";
    /**
     * The constant SQL_UPDATE_PLAN_SECTION_COURSE_FOR_USER.
     */
    public static final String SQL_UPDATE_PLAN_SECTION_COURSE_FOR_USER =
        "UPDATE personal_plan_section_course set type = ?, cms = ?, title = ?, description = ? , url = ? , duedate = ?, sort_order = ? where id = ? ";
    /**
     * The constant SQL_DELETE_PLAN_SECTION_COURSES_FOR_USER.
     */
    public static final String SQL_DELETE_PLAN_SECTION_COURSES_FOR_USER =
        "DELETE FROM personal_plan_section_course WHERE personal_plan_section_id = :setId and id in (:ids)";

    /**
     * The constant SQL_UPDATE_PLAN_FOR_USER.
     */
    public static final String SQL_UPDATE_PLAN_FOR_USER =
        "UPDATE personal_plan set name = ?, description = ?, duedate = ?, userid = ? where id = ?";
    /**
     * The constant SQL_UPDATE_PLAN_SECTION_FOR_USER.
     */
    public static final String SQL_UPDATE_PLAN_SECTION_FOR_USER =
        "UPDATE personal_plan_section set name = ?, description = ?, duedate = ?, sort_order = ? where id = ? and planid = ?";

    /**
     * The constant SQL_UPDATE_ARCHIVE_PLAN_FOR_USER.
     */
    public static final String SQL_UPDATE_ARCHIVE_PLAN_FOR_USER =
        "UPDATE personal_plan set archived = true where userid = ? and id = ?";

    /**
     * The constant SQL_SELECT_PLAN_SECTION_COURSE_ID.
     */
    public static final String SQL_SELECT_PLAN_SECTION_COURSE_ID = ""
        + "SELECT psc.id from personal_plan_section_course psc"
        + "  JOIN personal_plan_section ps on ps.id = psc.personal_plan_section_id and psc.personal_plan_section_id = ?"
        + "  JOIN personal_plan p on p.id = ps.planid and p.id = ? and p.userid = ?"
        + "  WHERE psc.courseid = ?";

    /**
     * The constant SQL_SELECT_PLAN_FOR_USER.
     */
    public static final String SQL_SELECT_PLAN_FOR_USER =
        "SELECT id as id, name as title, description as description, userid as userId, duedate, archived FROM personal_plan p "
            + "   left join personal_plan_share pps on pps.personal_plan_id = id and pps.shared_user_id = ?"
            + "   where (p.userid = ? or pps.shared_user_id = ?) and p.id = ?";
    /**
     * The constant SQL_SELECT_SECTION_FOR_PLAN.
     */
    public static final String SQL_SELECT_SECTION_FOR_PLAN =
        "SELECT id as id, planid, name, description as summary, duedate as dueDate, sort_order as sortOrder FROM personal_plan_section where planId = ?";
    /**
     * The constant SQL_SELECT_COURSES_FOR_SECTION_FOR_PLAN.
     */
    public static final String SQL_SELECT_COURSES_FOR_SECTION_FOR_PLAN =
        "SELECT courseid as id, type, cms, personal_plan_section_id as sectionId, id as itemId, title, description, url, duedate as dueDate FROM personal_plan_section_course where personal_plan_section_id in (:ids)";

    /**
     * The constant SQL_UPDATE_COMPLETION_MANUAL.
     */
    public static final String SQL_UPDATE_COMPLETION_MANUAL =
        "update personal_plan_manual_completion set complete_status = ? where plan_section_course_id = ?";
    /**
     * The constant SQL_INSERT_COMPLETION_MANUAL.
     */
    public static final String SQL_INSERT_COMPLETION_MANUAL =
        "insert into personal_plan_manual_completion (complete_status, userid, plan_section_course_id) VALUES (?,?,?) RETURNING ID";
    /**
     * The constant SQL_SELECT_COMPLETION_MANUAL.
     */
    public static final String SQL_SELECT_COMPLETION_MANUAL =
        "SELECT complete_status from personal_plan_manual_completion where plan_section_course_id = ? and userid = ?";
    /**
     * The constant SQL_SELECT_COMPLETION_EXISTS.
     */
    public static final String SQL_SELECT_COMPLETION_EXISTS =
        "SELECT count(*) from personal_plan_manual_completion where plan_section_course_id = ?";

    /**
     * The constant SQL_SELECT_PLANS_WITH_DUE_DATE_PASSED.
     */
    public static final String SQL_SELECT_PLANS_WITH_DUE_DATE_PASSED =
        "SELECT pp.id as programId, pp.name as programName, pp.description as programSummary, pp.userid, pp.duedate as programDueDate"
            + " from personal_plan pp\n"
            + "    left join personal_plan_section pps on pps.planid = pp.id\n"
            + "    left join personal_plan_section_course ppsc on ppsc.personal_plan_section_id = pps.id\n"
            + "    where (pp.duedate is not null and pp.duedate < CURRENT_DATE - 1)\n"
            + "        or (pps.duedate is not null and pps.duedate < CURRENT_DATE - 1)\n"
            + "        or (ppsc.duedate is not null and ppsc.duedate < CURRENT_DATE - 1);";


}
