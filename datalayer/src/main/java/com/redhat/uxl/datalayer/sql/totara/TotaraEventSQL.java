package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara event sql.
 */
public class TotaraEventSQL {

    /**
     * The constant S_SELECT_EVENTS.
     */
    public final static String S_SELECT_EVENTS =
        "select DISTINCT ON (e.courseid) e.courseid as courseId, e.userid, e.name as name, e.timestart as time, e.timeduration as duration, ftfsd3.sessiontimezone as timezone, cat.name as categoryname, ftfsd1.data as country, ftfsd2.data as city\n"
            + "      from mdl_event e\n" + "        join mdl_course c on e.courseid = c.id\n"
            + "        join mdl_course_categories cat on c.category = cat.id and c.audiencevisible = 2\n"
            + "        left join mdl_facetoface_sessions ftfs on cast(nullif(e.uuid , '') as bigint) = ftfs.id\n"
            + "        left join mdl_facetoface_session_info_data ftfsd1 on ftfsd1.facetofacesessionid = ftfs.id and ftfsd1.fieldid in (select id from mdl_facetoface_session_info_field ftfsf where ftfsf.shortname = 'country')\n"
            + "        left join mdl_facetoface_session_info_data ftfsd2 on ftfsd2.facetofacesessionid = ftfs.id and ftfsd2.fieldid in (select id from mdl_facetoface_session_info_field ftfsf where ftfsf.shortname = 'city')\n"
            + "        left join mdl_facetoface_sessions_dates ftfsd3 on ftfsd3.sessionid = ftfs.id \n"
            + "        where e.visible = 1\n"
            + "        and ((e.timestart between ? and ?) or (e.timestart+e.timeduration between ? and ?))\n"
            + "        and e.courseid <> 0\n"
            + "      and (e.timestart + e.timeduration) > EXTRACT(epoch from now())\n"
            + "      and e.eventtype='facetofacesession'\n" + "        and ftfsd1.data like ?\n"
            + "        and ftfsd2.data like ? \n" + "        and ftfs.cancelledstatus <> 1";

    /**
     * The constant SQL_SELECT_COUNT_ACTIVE_EVENTS.
     */
    public final static String SQL_SELECT_COUNT_ACTIVE_EVENTS =
        "SELECT COUNT(e.id) as count FROM mdl_course c,mdl_event e \n"
            + "       left join mdl_facetoface_sessions ftfs on cast(nullif(e.uuid , '') as bigint) = ftfs.id \n"
            + "    where e.visible = 1 \n" + "        and e.courseid <> 0 \n"
            + "        and (e.timestart + e.timeduration) > EXTRACT(epoch from now()) \n"
            + "        and e.courseid = c.id \n" + "        and c.audiencevisible = 2 \n"
            + "        and ftfs.cancelledstatus <> 1";

    /**
     * The constant SQL_SELECT_COUNT_MY_UPCOMING_EVENTS.
     */
    public final static String SQL_SELECT_COUNT_MY_UPCOMING_EVENTS = "select count( distinct c.*) \n"
        + "        from\n" + "                mdl_course c, \n"
        + "                mdl_facetoface f2f, \n" + "                mdl_facetoface_sessions s,\n"
        + "                mdl_facetoface_signups fs, \n"
        + "                mdl_facetoface_signups_status fss, \n"
        + "                mdl_facetoface_sessions_dates fd,\n"
        + "                mdl_facetoface_session_info_field country,              \n"
        + "                mdl_facetoface_session_info_field city,\n"
        + "                mdl_facetoface_session_info_data countrydata,\n"
        + "                mdl_facetoface_session_info_data citydata\n"
        + "        where f2f.course = c.id and s.facetoface = f2f.id\n"
        + "                and fs.sessionid = s.id\n" + "                and fs.userid = ?\n"
        + "                and fss.signupid = fs.id\n"
        + "                and fss.statuscode in (50,70) and fss.superceded = 0\n"
        + "                and fd.sessionid = s.id\n"
        + "                and fd.timefinish > EXTRACT(epoch from now())\n"
        + "                and country.shortname = 'country'\n"
        + "                and city.shortname = 'city'\n"
        + "                and s.id = countrydata.facetofacesessionid\n"
        + "                and s.id = citydata.facetofacesessionid\n"
        + "                and country.id = countrydata.fieldid\n"
        + "                and city.id = citydata.fieldid \n" + "and s.cancelledstatus <> 1";

    /**
     * The constant SQL_SELECT_MY_UPCOMING_EVENTS.
     */
    public final static String SQL_SELECT_MY_UPCOMING_EVENTS =
        "select distinct fs.sessionid as sessionId, fss.statuscode, s.facetoface, c.id as courseId, cat.name as categoryname, f2f.id as eventId, fs.userid, c.fullname as name, fd.sessiontimezone as timezone, fd.timestart as time, 0 as duration, countrydata.data as country, citydata.data as city \n"
            + "        from\n" + "                mdl_course c, \n"
            + "                mdl_course_categories cat, \n"
            + "                mdl_facetoface f2f, \n"
            + "                mdl_facetoface_sessions s,\n"
            + "                mdl_facetoface_signups fs, \n"
            + "                mdl_facetoface_signups_status fss, \n"
            + "                mdl_facetoface_sessions_dates fd,\n"
            + "                mdl_facetoface_session_info_field country,\n"
            + "                mdl_facetoface_session_info_field city,\n"
            + "                mdl_facetoface_session_info_data countrydata,\n"
            + "                mdl_facetoface_session_info_data citydata\n"
            + "        where f2f.course = c.id and c.category = cat.id and s.facetoface = f2f.id\n"
            + "                and fs.sessionid = s.id\n" + "                and fs.userid = ?\n"
            + "                and fss.signupid = fs.id\n"
            + "                and fss.statuscode in (50,70)\n"
            + "                and fd.sessionid = s.id\n"
            + "                and fd.timefinish > EXTRACT(epoch from now())\n"
            + "                and country.shortname = 'country'\n"
            + "                and city.shortname = 'city'\n"
            + "                and s.id = countrydata.facetofacesessionid\n"
            + "                and s.id = citydata.facetofacesessionid\n"
            + "                and country.id = countrydata.fieldid\n"
            + "                and city.id = citydata.fieldid\n" + "and s.cancelledstatus <> 1 \n"
            + "        order by fd.timestart asc limit ? offset ?";

    /**
     * The constant SQL_SELECT_ALL_ACTIVE_EVENTS_PAGED.
     */
    public final static String SQL_SELECT_ALL_ACTIVE_EVENTS_PAGED =
        "select distinct(e.courseid) as courseId, cat.name as categoryname, e.id as eventId, ftfs.id as sessionId, e.userid, e.name as name, e.timestart as time, e.timeduration as duration, ftfsd3.sessiontimezone as timezone, ftfsd1.data as country, ftfsd2.data as city from mdl_course c \n"
            + "  left join mdl_event e on e.courseid = c.id \n"
            + "  left join mdl_course_categories cat on c.category = cat.id\n"
            + "  left join mdl_facetoface_sessions ftfs on cast(nullif(e.uuid , '') as bigint) = ftfs.id\n"
            + "  left join mdl_facetoface_session_info_data ftfsd1 on ftfsd1.facetofacesessionid = ftfs.id and ftfsd1.fieldid in (select id from mdl_facetoface_session_info_field ftfsf where ftfsf.shortname = 'country')\n"
            + "  left join mdl_facetoface_session_info_data ftfsd2 on ftfsd2.facetofacesessionid = ftfs.id and ftfsd2.fieldid in (select id from mdl_facetoface_session_info_field ftfsf where ftfsf.shortname = 'city')\n"
            + "  left join mdl_facetoface_sessions_dates ftfsd3 on ftfsd3.sessionid = ftfs.id \n"
            + "  where e.visible = 1 \n" + "        and e.courseid <> 0 \n"
            + "        and (e.timestart + e.timeduration) > EXTRACT(epoch from now()) \n"
            + "        and e.courseid = c.id \n" + "        and c.audiencevisible = 2 \n"
            + "        and ftfs.cancelledstatus <> 1 \n" + "  LIMIT ? OFFSET ?";

    /**
     * The constant SQL_SELECT_ALL_EVENT_LOCATIONS.
     */
    public final static String SQL_SELECT_ALL_EVENT_LOCATIONS =
        "select distinct(ftfsd1.data) as country, ftfsd2.data as city from mdl_event e\n"
            + "          left join mdl_facetoface_sessions ftfs on cast(nullif(e.uuid , '') as bigint) = ftfs.id\n"
            + "          left join mdl_facetoface_session_info_data ftfsd1 on ftfsd1.facetofacesessionid = ftfs.id and ftfsd1.fieldid in (select id from mdl_facetoface_session_info_field ftfsf where ftfsf.shortname = 'country')\n"
            + "          left join mdl_facetoface_session_info_data ftfsd2 on ftfsd2.facetofacesessionid = ftfs.id and ftfsd2.fieldid in (select id from mdl_facetoface_session_info_field ftfsf where ftfsf.shortname = 'city')\n"
            + "          where ftfsd1.data != 'Not Applicable' \n" + "ORDER BY country ASC, CITY ASC";


}
