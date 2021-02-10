package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara user sql.
 */
public class TotaraUserSQL {

    /**
     * The constant S_SELECT_ACTIVE_USERS.
     */
    public static final String S_SELECT_ACTIVE_USERS =
        "select usr.id as id from mdl_user usr where deleted = 0 and suspended = 0;";

    /**
     * The constant S_SELECT_ACIVE_USERS_FOR_PROG.
     */
    public static final String S_SELECT_ACIVE_USERS_FOR_PROG =
        "select distinct usr.id as id From mdl_user usr, mdl_prog_completion comp "
            + "where programid = ? and coursesetid = 0 and status = 0 and comp.userid = usr.id and usr.deleted = 0 and usr.suspended = 0";

    /**
     * The constant S_SELECT_USERS_BY_IDS.
     */
    public static final String S_SELECT_USERS_BY_IDS =
        "select usr.id as id, usr.firstname as firstName, lastname as lastName from mdl_user usr where usr.id in (:userIds);";

    /**
     * The constant S_SELECT_USER_BY_ID.
     */
    public static final String S_SELECT_USER_BY_ID =
        "select usr.id as id, usr.firstname as firstName, lastname as lastName from mdl_user usr where usr.id = ?";

}
