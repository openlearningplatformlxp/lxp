'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('CatalogCourseActivityController',
        function($state, $stateParams, $location, resolvedCourseData, resolvedCourseSectionData, CourseService) {

            var data = {
                    isUserEnrolled: resolvedCourseData.enrolled,
                    activityStatuses: resolvedCourseSectionData.activityStatuses || {}
                },

                init = function() {
                    data.sections = prepareSections(resolvedCourseSectionData.sections);
                },

                prepareSections = function(sections) {
                    var i,
                        len,
                        section,
                        sectionFound;

                    sections = sections || [];

                    for (i = 0, len = sections.length; i < len && !sectionFound; i++) {
                        section = sections[i];
                        if (section.activities && section.activities.length) {
                            sectionFound = true;
                            section.showActivities = true;
                        }
                    }

                    return sections;
                },

                getNumArray = function(num) {
                    return new Array(num);
                },

                goToCoursePlayer = function(section, activity) {
                    if (data.isUserEnrolled) {
                        $state.go("course-player-activity", {
                            courseId: $stateParams.courseId,
                            sectionId: section.id,
                            activityId: activity.id,
                            returnPath: $location.url()
                        });
                    }
                },

                isActivityComplete = function(activity) {
                    return data.activityStatuses[activity.id] && data.activityStatuses[activity.id].status === 1;
                },

                isActivityLocked = function(activity) {
                    return data.activityStatuses[activity.id] && data.activityStatuses[activity.id].isLocked;
                },

                getActivityTypeIconClass = function(activity) {
                    var typeIconClass = "fa-list";

                    // TODO: (WJK) Fix the handling of RESOURCE to separate PDF and VIDEO cases
                    if (activity.type === 'VIDEO') {
                        typeIconClass = "fa-play-circle";
                    } else if (activity.type === 'URL') {
                        typeIconClass = "fa-link";
                    } else if (activity.type === 'SCORM') {
                        typeIconClass = "fa-question-circle";
                    } else if (activity.type === 'QUIZ') {
                        typeIconClass = "fa-question-circle";
                    }

                    return typeIconClass;
                },

                getActivityCompletionIconClass = function(activity) {
                    var iconClass = "fa-circle-thin";

                    if (isActivityLocked(activity)) {
                        iconClass = "fa-lock";
                    } else if (isActivityComplete(activity)) {
                        iconClass = "fa-check-circle";
                    }

                    return iconClass;
                },

                zend;

            init();

            return {
                data: data,
                getNumArray: getNumArray,
                goToCoursePlayer: goToCoursePlayer,
                isActivityComplete: isActivityComplete,
                isActivityLocked: isActivityLocked,
                getActivityTypeIconClass: getActivityTypeIconClass,
                getActivityCompletionIconClass: getActivityCompletionIconClass
            };
        }
    );
})();
