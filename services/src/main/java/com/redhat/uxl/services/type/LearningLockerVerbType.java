package com.redhat.uxl.services.type;

/**
 * The enum Learning locker verb type.
 */
public enum LearningLockerVerbType {
    /**
     * Played learning locker verb type.
     */
    PLAYED("http://activitystrea.ms/schema/1.0/play"),
    /**
     * Watched learning locker verb type.
     */
    WATCHED("http://activitystrea.ms/schema/1.0/watch"),
    /**
     * Viewed learning locker verb type.
     */
    VIEWED("http://adlnet.gov/expapi/verbs/viewed"),
    /**
     * Completed learning locker verb type.
     */
    COMPLETED("http://adlnet.gov/expapi/verbs/completed"),
    /**
     * Allego completed learning locker verb type.
     */
    ALLEGO_COMPLETED("https://my.allego.com#completed"),
    /**
     * Allego scored learning locker verb type.
     */
    ALLEGO_SCORED("https://my.allego.com#scored");

    private String objectType;

    LearningLockerVerbType(String objectType) {
        this.objectType = objectType;
    }

    /**
     * Gets object type.
     *
     * @return the object type
     */
    public String getObjectType() {
        return objectType;
    }
}
