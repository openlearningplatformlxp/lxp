package com.redhat.uxl.dataobjects.domain.types;

/**
 * The enum Program course type.
 */
public enum ProgramCourseType {
    /**
     * Course program course type.
     */
    COURSE("Course"),
    /**
     * The Offline task.
     */
    OFFLINE_TASK("Offline Task"),
    /**
     * The Web task.
     */
    WEB_TASK("Web Activity"),
    /**
     * The Single activity course.
     */
    SINGLE_ACTIVITY_COURSE("Single Activity"),
    /**
     * Text entry program course type.
     */
    TEXT_ENTRY("Essay"),
    /**
     * Wikipage program course type.
     */
    WIKIPAGE("Wikipage"),
    /**
     * The Program link.
     */
    PROGRAM_LINK("Program Link");

    private String description;

    ProgramCourseType(String description) {
        this.description = description;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
