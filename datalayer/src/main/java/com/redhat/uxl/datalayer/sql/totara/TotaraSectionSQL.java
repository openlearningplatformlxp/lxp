package com.redhat.uxl.datalayer.sql.totara;

/**
 * The type Totara section sql.
 */
public class TotaraSectionSQL {

    /**
     * The constant getCourseSectionsHaveActivitiesSQL.
     */
    public static final String getCourseSectionsHaveActivitiesSQL = "select cs.id," + " cs.name,"
      + " cs.sequence," + " cs.section, " + " cs.summary " + " from mdl_course_sections as cs"
      + " where course = :courseId and (cs.name is null or (cs.name not ilike 'Resources' and cs.name not ilike 'Prerequisites')) and cs.id in ( \n"
      + " select cm.section \n" + " from mdl_course_modules as cm \n"
      + " where cm.course = :courseId ) \n" + " order by section ASC\n";

    /**
     * The constant getCoursePrerequisitesHaveActivitiesSQL.
     */
    public static final String getCoursePrerequisitesHaveActivitiesSQL = "select cs.id," + " cs.name,"
      + " cs.sequence," + " cs.section, " + " cs.summary " + " from mdl_course_sections as cs"
      + " where course = :courseId and cs.name ilike 'Prerequisites' " + " order by section ASC\n";

    /**
     * The constant getCourseResourcesHaveActivitiesSQL.
     */
    public static final String getCourseResourcesHaveActivitiesSQL = "select cs.id," + " cs.name,"
      + " cs.sequence," + " cs.section, " + " cs.summary " + " from mdl_course_sections as cs"
      + " where course = :courseId and cs.name ilike 'Resources' " + " order by section ASC\n";

    /**
     * The constant courseSectionSQL.
     */
    public static final String courseSectionSQL =
      "select cs.id," + " cs.name," + " cs.sequence," + " cs.section," + " cs.summary "
          + " from mdl_course_sections as cs" + " where course = :courseId and id = :sectionId\n";
}
