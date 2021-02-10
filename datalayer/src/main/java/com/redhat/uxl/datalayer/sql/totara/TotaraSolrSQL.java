package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara solr sql.
 */
public class TotaraSolrSQL {

    /**
     * The constant INSERT_COURSE.
     */
    public static final String INSERT_COURSE =
        "INSERT INTO public.mdl_xl_solr_courses(field_full_name, field_short_name, field_description, field_program_type, field_delivery, field_skill_level, field_language, field_tags, field_content_source, field_time_created)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    /**
     * The constant CLEAR_TABLE.
     */
    public static final String CLEAR_TABLE = "truncate mdl_xl_solr_courses";

}
