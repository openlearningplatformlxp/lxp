package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara file sql.
 */
public class TotaraFileSQL {

    /**
     * The constant S_SELECT_COURSE_FILE_BY_SECTION_AND_NAME.
     */
    public static final String S_SELECT_COURSE_FILE_BY_SECTION_AND_NAME =
        "SELECT * FROM mdl_files" + " WHERE component = 'course'" + "   and filearea = 'section' "
            + "   and itemid = ?" + "   and filename = ?" + "   limit 1";

}
