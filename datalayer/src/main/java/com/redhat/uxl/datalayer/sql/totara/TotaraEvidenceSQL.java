package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara evidence sql.
 */
public class TotaraEvidenceSQL {

    /**
     * The constant S_SELECT_EVIDENCE_TYPES.
     */
    public static final String S_SELECT_EVIDENCE_TYPES =
        "select id, name from mdl_dp_evidence_type \n" + "order by sortorder;";

    /**
     * The constant SQL_INSERT_EVIDENCE.
     */
    public static final String SQL_INSERT_EVIDENCE =
        "INSERT INTO mdl_dp_plan_evidence (name, evidencetypeid, userid) values (?,?, ?) RETURNING id";

    /**
     * The constant SQL_SELECT_EVIDENCE_INFO_FIELD.
     */
    public static final String SQL_SELECT_EVIDENCE_INFO_FIELD =
        "select id, shortname from mdl_dp_plan_evidence_info_field where shortname = ':shortname';";

    /**
     * The constant SQL_INSERT_EVIDENCE_INFO_DATA.
     */
    public static final String SQL_INSERT_EVIDENCE_INFO_DATA =
        "INSERT INTO mdl_dp_plan_evidence_info_data (fieldid, evidenceid, data) values (?, ?, ?) RETURNING id";

    /**
     * The constant SQL_UPDATE_EVIDENCE_INFO_DATA.
     */
    public static final String SQL_UPDATE_EVIDENCE_INFO_DATA =
        "UPDATE mdl_dp_plan_evidence_info_data set data = ? where id =  ?";

    /**
     * The constant SQL_INSERT_CECREDITS.
     */
    public static final String SQL_INSERT_CECREDITS =
        "insert into mdl_local_cecredit_earned (userid,instanceid,instancetype,status,credits,timeearned,timemodified) values (?,?,?,?,(select case when id = 1 then 0 else substring(name,0,3)::smallint end from mdl_dp_evidence_type where id = ?), ?, ?)";

    /**
     * The constant SQL_GET_PENDING_EVIDENCE.
     */
    public static final String SQL_GET_PENDING_EVIDENCE =
        "select (select de.data from mdl_dp_plan_evidence_info_data de where e.id = de.evidenceid and de.fieldid = (select id from mdl_dp_plan_evidence_info_field where shortname = 'evidencedescription')) as name, c.status, c.timemodified  from mdl_dp_plan_evidence e, mdl_local_cecredit_earned c where e.id = c.instanceid and c.userid = ? and c.instancetype = 50";

    /**
     * The constant SQL_SUBMIT_MESSAGE.
     */
    public static final String SQL_SUBMIT_MESSAGE =
        "insert into mdl_message(useridfrom, useridto,subject,fullmessage,fullmessageformat,fullmessagehtml,notification,timecreated,timeuserfromdeleted,timeusertodeleted,component,eventtype)\n"
            + "values (?,(select (select a.userid from mdl_job_assignment a where a.id = b.managerjaid) from mdl_job_assignment b where b.userid = ?),\n"
            + "'New learning evidence is awaiting your review.',\n"
            + "(select concat(concat(concat(firstname, ' '), lastname), ' has submitted new learning evidence.') from mdl_user where id = ?),\n"
            + "2,\n"
            + "(select concat(concat(concat(firstname, ' '), lastname), ' has submitted new learning evidence.') from mdl_user where id = ?),\n"
            + "1,extract(epoch from now()),0,0,'totara_message','alert')";

}
