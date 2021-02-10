package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara program sql.
 */
public class TotaraProgramSQL {

    /**
     * The constant getProgramInfoSQL.
     */
    public static final String getProgramInfoSQL = "SELECT p.*, pid.data as prog_selfenroll " + "FROM mdl_prog as p "
            + "left join mdl_prog_info_data as pid on pid.programid = p.id and pid.fieldid = (select id from mdl_prog_info_field where shortname = 'allowselfassignment') "
            + "where p.id = :programId\n";

    /**
     * The constant getUserProgramEnrollment.
     */
    public static final String getUserProgramEnrollment = "SELECT * FROM mdl_prog_completion where programid = :programId and userid = :userId and coursesetid = 0\n";

    /**
     * The constant getNonEnrolledProgression.
     */
    public static final String getNonEnrolledProgression = "select base.programName as programName, base.programId as programId, base.course as courseId, base.status as status , NULLIF(d.data, '')::float as duration \n"
            + "from ( \n"
            + "select p.id as programId, p.fullname as programName, p.shortname as programShortName, p.Summary as programSummary, cm.course as course, 0 as status\n"
            + " from \n" + "mdl_prog p,\n" + "mdl_prog_courseset pc,\n" + "mdl_prog_courseset_course pcc,\n"
            + "mdl_course_modules cm \n" + "where   p.id = ? \n" + "and p.id =  pc.programid \n"
            + "and pc.id = pcc.coursesetid \n" + "and pcc.courseid = cm.course \n" + "group by p.id, cm.course \n"
            + ") as base, \n" + "MDL_COURSE_INFO_DATA d, \n" + "MDL_COURSE_INFO_FIELD f \n" + "WHERE \n"
            + "f.shortname = 'duration' \n" + "and d.courseid = base.course \n" + "and d.fieldid = f.id;";

    /**
     * The constant isCourseANestedProgram.
     */
    public static final String isCourseANestedProgram = "select programid from mdl_course_modules cm, mdl_modules m, mdl_programlink pl where cm.module = m.id and m.name = 'programlink' and cm.instance = pl.id and pl.course = ?";

    /**
     * The constant isCourseATextEntry.
     */
    // Need to modifiy ths query to return the value if previously entered
    public static final String isCourseATextEntry = "select cm.instance, te.intro, tv.textvalue from mdl_course_modules cm left outer join mdl_textentry_values tv on  cm.instance = tv.textentryid and tv.userid = ?,  mdl_textentry te,mdl_modules m where cm.module = m.id and m.name = 'textentry' and cm.instance = te.id and cm.course = ? and tv.id = (select max(tv.id) from mdl_course_modules cm left outer join mdl_textentry_values tv on  cm.instance = tv.textentryid and tv.userid = ?,  mdl_textentry te,mdl_modules m where cm.module = m.id and m.name = 'textentry'  and cm.instance = te.id and cm.course = ?)";

    /**
     * The constant getProgramTotalDuration.
     */
    public static final String getProgramTotalDuration = "select  TO_CHAR((sum(timeallowed) || ' second')::interval, 'HH24') as duration from mdl_prog_courseset where programid = ? and completiontype != 4";

    /**
     * The constant getProgramProgressionStats.
     */
    public static final String getProgramProgressionStats = "select base.programName as programName, base.programId as programId, base.course as courseId, base.status as status , base.completiontype as cccompletion, NULLIF(d.data, '')::float as duration from (\n"
            + "                        select p.id as programId, p.fullname as programName, p.shortname as programShortName, p.Summary as programSummary, cm.course as course, cc.status as status, pc.completiontype\n"
            + "                                from mdl_prog_completion pco,\n"
            + "                                        mdl_prog p,\n"
            + "                                        mdl_prog_courseset pc,\n"
            + "                                       mdl_prog_courseset_course pcc\n"
            + "                                                left outer join mdl_course_completions cc on pcc.courseid = cc.course and cc.userid = ?,\n"
            + "                                        mdl_course_modules cm\n"
            + "                                                left outer join mdl_course_modules_completion cmc on cm.id = cmc.coursemoduleid and cmc.userid = ?\n"
            + "                                where   p.id = ?\n"
            + "                                        and pco.userid = ?\n"
            + "                                        and pco.programid = p.id \n"
            + "                                        and pco.programid = pc.programid\n"
            + "                                        and pc.id = pcc.coursesetid\n"
            + "                                        and pcc.courseid = cm.course\n"
            + "                                group by p.id, cm.course, cc.status, pc.completiontype\n"
            + "                ) as base,\n" + "                MDL_COURSE_INFO_DATA d,\n"
            + "                MDL_COURSE_INFO_FIELD f\n" + "                WHERE\n"
            + "                        f.shortname = 'duration'\n"
            + "                        and d.courseid = base.course\n"
            + "                        and d.fieldid = f.id ";
    /**
     * The constant deleteProgramAssignment.
     */
    public static final String deleteProgramAssignment =
        "DELETE FROM mdl_prog_user_assignment pa WHERE pa.userid = ? AND pa.programid = ?";

    /**
     * The constant deleteProgram.
     */
    public static final String deleteProgram =
        "DELETE FROM mdl_prog_completion pa WHERE pa.programid = ? and pa.userid = ? and pa.coursesetid = 0";

}
