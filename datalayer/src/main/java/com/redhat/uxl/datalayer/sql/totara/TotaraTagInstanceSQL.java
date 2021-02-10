package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara tag instance sql.
 */
public class TotaraTagInstanceSQL {

    /**
     * The constant INSERT_INTO.
     */
    public static final String INSERT_INTO =
        "insert into mdl_tag_instance (tagid, component, itemtype, itemid, contextid, tiuserid, ordering, timecreated, timemodified) values (?, ?, ?, ?, ?, ?, ?, ? , ? )";
    /**
     * The constant DELETE.
     */
    public static final String DELETE =
        "delete from mdl_tag_instance where tagid = ? and itemid = ? and itemtype = 'user';";
    /**
     * The constant DELETE_WIKI.
     */
    public static final String DELETE_WIKI =
        "delete from mdl_tag_instance where itemid = ? and tagid = ? and itemtype = 'wiki';";

}
