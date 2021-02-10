package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara profile sql.
 */
public class TotaraProfileSQL {

    /**
     * The constant S_SELECT_USER_PROFILE.
     */
    public static final String S_SELECT_USER_PROFILE = "select id as id, firstname as firstName, lastname as lastName, email as email, city as city, country as country, timezone as timezone, lang as language\n"
        + ",(select data from mdl_user_info_data md, mdl_user_info_field mf where mf.shortname = 'Redhatcustomerportalusernamerhnid' and mf.id = md.fieldid and md.userid = mdl_user.id) as rhnId\n"
        + ",(select data from mdl_user_info_data md, mdl_user_info_field mf where mf.shortname = 'certid' and mf.id = md.fieldid and md.userid = mdl_user.id) as certId\n"
        + "from mdl_user \n" + "where id = ?";

    /**
     * The constant S_SELECT_USER_AUDIENCE.
     */
    public static final String S_SELECT_USER_AUDIENCE = "select c.id as id, name as name from mdl_cohort_members cm , mdl_cohort c where cm.userid = ? and cm.cohortid = c.id;";

    /**
     * The constant S_SELECT_USER_MANGER.
     */
    public static final String S_SELECT_USER_MANGER = "select usr.id as id, usr.firstname as firstName, lastname as lastName, city as city, country as country from mdl_job_assignment u, mdl_job_assignment m, mdl_user usr where u.userid = ? and u.managerjaid = m.id and m.userid = usr.id;";

    /**
     * The constant S_SELECT_USER_TAGS.
     */
    // ID 1/2/3 Product/Topic/Competencies of tags parents
    public static final String S_SELECT_USER_TAGS = " select distinct t.id as id, t.userid, t.name, t.rawname, t.description, t.flag  \n"
        + " From mdl_tag t \n" + " join mdl_tag_instance ti on ti.tagid = t.id \n"
        + " join mdl_tag_parent_child tpc on tpc.child_tag = t.id \n"
        + " inner join mdl_tag_parent p on tpc.parent_tag = p.id \n"
        + " where ti.itemid=? and ti.itemtype='user' and (p.name = 'Product' or p.name = 'Topic' or p.name = 'Competencies') order by t.name ASC";

    /**
     * The constant S_SELECT_USER_TAGS_ROLES.
     */
    // Parent ID 5 = ROLE
    public static final String S_SELECT_USER_TAGS_ROLES = " select t.id as id, t.userid, t.name, t.rawname, t.description, t.flag  \n"
        + " From mdl_tag t \n" + " join mdl_tag_instance ti on ti.tagid = t.id \n"
        + " join mdl_tag_parent_child tpc on tpc.child_tag = t.id \n"
        + " inner join mdl_tag_parent p on tpc.parent_tag = p.id \n"
        + " where ti.itemid=? and ti.itemtype='user' and p.name = 'Role' order by t.name ASC";

    /**
     * The constant S_SELECT_USER_PROFILE_BY_EMAIL.
     */
    public static final String S_SELECT_USER_PROFILE_BY_EMAIL = "SELECT * FROM MDL_USER WHERE lower(email) = ?";

    /**
     * The constant S_SELECT_USER_PROFILE_FOR_FEEDBACK.
     */
    public static final String S_SELECT_USER_PROFILE_FOR_FEEDBACK = "select id as id, email as email\n"
        + ",(select data from mdl_user_info_data md, mdl_user_info_field mf where mf.shortname = 'region' and mf.id = md.fieldid and md.userid = mdl_user.id) as region\n"
        + ",(select data from mdl_user_info_data md, mdl_user_info_field mf where mf.shortname = 'learnerjobtitle' and mf.id = md.fieldid and md.userid = mdl_user.id) as learnerJobTitle\n"
        + "from mdl_user \n" + "where id = ?";

}
