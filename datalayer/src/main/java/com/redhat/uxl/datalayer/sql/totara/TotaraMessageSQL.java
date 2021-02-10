package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara message sql.
 */
public class TotaraMessageSQL {

    /**
     * The constant S_SELECT_USER_NOTIFICATIONS.
     */
    public static final String S_SELECT_USER_NOTIFICATIONS =
        "select h.id as id, title as title from mdl_facetoface_notification_hist h, mdl_facetoface_notification n where h.notificationid = n.id and h.userid = ?";
    /**
     * The constant S_SELECT_USER_DISMISSED_NOTIFICATIONS.
     */
    public static final String S_SELECT_USER_DISMISSED_NOTIFICATIONS =
        "select m.id, m.subject as title, m.smallmessage as message from mdl_message_read m,mdl_message_metadata mm where m.useridto = ? and m.timeusertodeleted = 0 and m.eventtype = 'alert' and m.id = mm.messagereadid and mm.messageid is null order by m.id desc limit 10";
    /**
     * The constant DELETE_NOTIFICATIONS.
     */
    public static final String DELETE_NOTIFICATIONS = "delete from mdl_message where id = ?";
    /**
     * The constant DELETE_NOTIFICATIONS_BY_USER_ID.
     */
    public static final String DELETE_NOTIFICATIONS_BY_USER_ID = "delete from mdl_message where useridto = ?";

    /**
     * The constant S_CLEAN_NOTIFICATION.
     */
    public static final String S_CLEAN_NOTIFICATION =
        "select replace((replace(replace(replace(replace(title, '[sessiondate]', to_char(to_timestamp(d.timestart), 'mm-dd-yyyy')),\n"
            + "'[facetofacename]', f.name),\n" + "'[coursename]',f.name),\n"
            + " '[lastname]', u.lastname)),\n" + "'[firstname]', u.firstname) from\n"
            + "mdl_facetoface_notification_hist nh, \n" + "mdl_facetoface_notification n, \n"
            + "mdl_user u,\n" + "mdl_facetoface f,\n" + "mdl_facetoface_sessions s,\n"
            + "mdl_facetoface_sessions_dates d\n" + "where n.id = nh.notificationid\n"
            + "and nh.sessionid = s.id\n" + "and u.id = nh.userid\n" + "and f.id = s.facetoface\n"
            + "and s.id = d.sessionid\n"
            + "and d.id = (select max(id) from mdl_facetoface_sessions_dates where sessionid = s.id group by sessionid)\n"
            + "and u.id = ? and nh.id = ?\n";

    /**
     * The constant S_SELECT_NOTIFICATIONS_FOR_TEAM.
     */
    public static final String S_SELECT_NOTIFICATIONS_FOR_TEAM =
        "select h.id as id, title as title, u.id as userid, n.conditiontype as action, sessionid as session from mdl_user u, mdl_facetoface_notification_hist h, mdl_facetoface_notification n where h.notificationid = n.id and u.id = h.userid and h.userid in (select userid from mdl_job_assignment where managerjaid = (select id from mdl_job_assignment where userid = ?)) and n.conditiontype = 32 and ccmanager = 1";

    /**
     * The constant S_SELECT_MESSAGE_NOTIFACTIONS.
     */
    public static final String S_SELECT_MESSAGE_NOTIFACTIONS =
        "select m.id, m.subject as title, m.smallmessage as message from mdl_message m,mdl_message_metadata mm where m.useridto = ? and m.timeusertodeleted = 0 and m.eventtype = 'alert' and m.id = mm.messageid and mm.messagereadid is null;";


}
