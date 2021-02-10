package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara tag sql.
 */
public class TotaraTagSQL {

    /**
     * The constant SQL_SELECT_MDL_TAG_BY_ID.
     */
    public static final String SQL_SELECT_MDL_TAG_BY_ID = " select t.id as id, t.userid, t.name, t.rawname, t.description, t.flag  \n"
        + " From mdl_tag t \n" + " where t.id = ?";

    /**
     * The constant SQL_SELECT_MDL_TAG_PARENT.
     */
    public static final String SQL_SELECT_MDL_TAG_PARENT = " select t.id as id, t.userid, t.name, t.rawname, t.description, t.flag  \n"
        + " From mdl_tag_parent t";

    /**
     * The constant SQL_SELECT_MDL_TAG_PARENT_BY_ID.
     */
    public static final String SQL_SELECT_MDL_TAG_PARENT_BY_ID = " select t.id as id, t.userid, t.name, t.rawname, t.description, t.flag  \n"
        + " From mdl_tag_parent t\n" + " where t.id = ?";

    /**
     * The constant SQL_SELECT_MDL_TAG_CHILD.
     */
    public static final String SQL_SELECT_MDL_TAG_CHILD = " select t.id as id, t.userid, t.name, t.rawname, t.description, t.flag  \n"
        + " From mdl_tag t \n" + " join mdl_tag_parent_child pt on pt.child_tag = t.id \n"
        + " join mdl_tag_parent p on p.id = pt.parent_tag \n" + " where p.id = ? " + " order by t.name";

    /**
     * The constant SQL_SELECT_MDL_TAGS.
     */
    public static final String SQL_SELECT_MDL_TAGS = " select t.id as id, t.userid, t.name, t.rawname, t.description, t.flag  \n"
        + " From mdl_tag t \n" + " join mdl_tag_parent_child pt on pt.child_tag = t.id \n"
        + " join mdl_tag_parent p on p.id = pt.parent_tag \n"
        + " where p.name = ? and t.isstandard = 1 and t.tagcollid = 2 ";

    /**
     * The constant SQL_SELECT_MDL_TAGS_ORDERED.
     */
    public static final String SQL_SELECT_MDL_TAGS_ORDERED = SQL_SELECT_MDL_TAGS + " ORDER by t.name";

    /**
     * The constant SQL_SELECT_MDL_TAGS_OF_ITEM.
     */
    public static final String SQL_SELECT_MDL_TAGS_OF_ITEM = " select t.id as id, t.userid, t.name, t.rawname, t.description, t.flag  \n"
        + " From mdl_tag t \n" + " join mdl_tag_instance ti on ti.tagid = t.id \n"
        + " where ti.itemid=? and ti.itemtype=':itemType'";

    /**
     * The constant SQL_SELECT_PROGRAM_PARENT_TAGS.
     */
    public static final String SQL_SELECT_PROGRAM_PARENT_TAGS = " select p.name as name, t.rawname as rawname from mdl_tag_instance i, mdl_tag_parent p, mdl_tag_parent_child pc, mdl_tag t where itemid = ? and i.tagid = pc.child_tag and i.tagid = t.id and pc.parent_tag = p.id and itemtype in ('prog', 'totara_program') order by p.name  \n";

    /**
     * The constant SQL_SELECT_COURSE_PARENT_TAGS.
     */
    public static final String SQL_SELECT_COURSE_PARENT_TAGS = " select p.name as name, t.rawname as rawname from mdl_tag_instance i, mdl_tag_parent p, mdl_tag_parent_child pc, mdl_tag t where itemid = ? and i.tagid = pc.child_tag and i.tagid = t.id and pc.parent_tag = p.id and itemtype='course' order by p.name  \n";

    /**
     * The constant SQL_SELECT_TAG_OF_TYPE.
     */
    public static final String SQL_SELECT_TAG_OF_TYPE = "select t2.id as id, t.userid, t.name, t.rawname, t.description, t.flag\n"
        + "         From mdl_tag t\n" + "         join mdl_tag_instance ti on ti.tagid = t.id\n"
        + "         join mdl_tag t2 on t2.rawname like t.rawname and t2.isstandard = 1\n"
        + "         join mdl_tag_parent_child pt on pt.child_tag = t2.id\n"
        + "         join mdl_tag_parent p on p.id = pt.parent_tag\n"
        + "         where p.name = ? and ti.itemid=? and ti.itemtype=? order by t2.name limit 1";

    /**
     * The constant SQL_SELECT_TAG_OF_TYPE_FROM_LIST.
     */
    public static final String SQL_SELECT_TAG_OF_TYPE_FROM_LIST = " select ti.itemid as itemId, t.id as id, t.userid, t.name, t.rawname, t.description, t.flag  \n"
        + " From mdl_tag t \n" + " join mdl_tag_parent_child pt on pt.child_tag = t.id \n"
        + " join mdl_tag_parent p on p.id = pt.parent_tag \n" + " join mdl_tag_instance ti on ti.tagid = t.id \n"
        + " where p.name = :parentTag and ti.itemid in (:itemId) and ti.itemtype=:itemType";

    /**
     * The constant UNMATCHED_USER_TAGS.
     */
    // ID 1/2/3 Product/Topic/Competencies of tags parents
    public static final String UNMATCHED_USER_TAGS = " select distinct(t.id) as id, t.userid, t.name, t.rawname, t.description, t.flag \n"
        + " From mdl_tag t \n" + " inner join mdl_tag_parent_child pt on pt.child_tag = t.id \n"
        + " inner join mdl_tag_parent p on pt.parent_tag = p.id \n"
        + " where (p.name = 'Product' or p.name = 'Topic' or p.name = 'Competencies' ) \n"
        + " and t.id not in (SELECT ti.tagid from mdl_tag_instance ti where ti.itemtype='user' and ti.itemid=?) and t.name ilike ? limit ?";

    /**
     * The constant ALL_USER_TAGS.
     */
    public static final String ALL_USER_TAGS = " select distinct(t.id) as id, t.userid, t.name, t.rawname, t.description, t.flag \n"
        + " From mdl_tag t \n" + " inner join mdl_tag_parent_child pt on pt.child_tag = t.id \n"
        + " inner join mdl_tag_parent p on pt.parent_tag = p.id \n"
        + " where (p.name = 'Product' or p.name = 'Topic' or p.name = 'Competencies' ) \n"
        + "  and t.id not in (SELECT ti.tagid from mdl_tag_instance ti where ti.itemtype='user' and ti.itemid=?) order by t.name";

    /**
     * The constant ALL_WIKI_TAGS.
     */
    public static final String ALL_WIKI_TAGS = " select distinct(t.id) as id, t.userid, t.name, t.rawname, t.description, t.flag \n"
        + " From mdl_tag t \n" + " inner join mdl_tag_parent_child pt on pt.child_tag = t.id \n"
        + " inner join mdl_tag_parent p on pt.parent_tag = p.id \n"
        + "  and t.id in (SELECT ti.tagid from mdl_tag_instance ti where ti.itemtype='wiki' and ti.itemid=?) order by t.name";

    /**
     * The constant UNMATCHED_USER_TAGS_ROLES.
     */
    // ID 5 Roles of tags parents
    public static final String UNMATCHED_USER_TAGS_ROLES = " select t.id as id, t.userid, t.name, t.rawname, t.description, t.flag  \n"
        + " From mdl_tag t \n" + " join mdl_tag_parent_child pt on pt.child_tag = t.id"
        + " join mdl_tag_parent p on p.id = pt.parent_tag \n"
        + " where (p.name = ?) and t.id not in (SELECT ti.tagid from mdl_tag_instance ti where ti.itemtype='user' and ti.itemid=?) and t.name ilike ? limit ?";

    /**
     * The constant UNMATCHED_WIKI_TAGS.
     */
    public static final String UNMATCHED_WIKI_TAGS = " select t.id as id, t.userid, t.name, t.rawname, t.description, t.flag  \n"
        + " From mdl_tag t \n" + " join mdl_tag_parent_child pt on pt.child_tag = t.id"
        + " join mdl_tag_parent p on p.id = pt.parent_tag \n"
        + " where t.id not in (SELECT ti.tagid from mdl_tag_instance ti where ti.itemtype='wiki' and ti.itemid=?) and t.name ilike ? limit ?";

    /**
     * The constant TAGS_ROLES.
     */
    public static final String TAGS_ROLES = " select t.id as id, t.userid, t.name, t.rawname, t.description, t.flag  \n"
        + " From mdl_tag t \n" + " join mdl_tag_parent_child pt on pt.child_tag = t.id"
        + " join mdl_tag_parent p on p.id = pt.parent_tag \n" + " where (p.name = ?) order by t.name";


}
