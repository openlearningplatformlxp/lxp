package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara url sql.
 */
public class TotaraUrlSQL {

    /**
     * The constant SQL_SELECT_MDL_URL_BY_EXTERNAL_URL.
     */
    public final static String SQL_SELECT_MDL_URL_BY_EXTERNAL_URL =
        " select u.id as id, u.course, u.name, u.externalurl  \n" + " From mdl_url u \n"
            + " where u.externalurl = ?";

    /**
     * The constant SQL_SELECT_MDL_URL_BY_EXTERNAL_URL_PATH.
     */
    public final static String SQL_SELECT_MDL_URL_BY_EXTERNAL_URL_PATH =
        " select u.id as id, u.course, u.name, u.externalurl  \n" + " From mdl_url u \n"
            + " where strpos(u.externalurl, ?) > 0 and strpos(u.externalurl, ?) > 0";

    /**
     * The constant SQL_SELECT_MDL_URL_BY_EXTERNAL_URL_PATH_KALTURA.
     */
    public final static String SQL_SELECT_MDL_URL_BY_EXTERNAL_URL_PATH_KALTURA =
        " select u.id as id, u.course, u.name, u.externalurl  \n" + " From mdl_url u \n"
            + " where u.externalurl like ?";

}
