'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('CoursePlayerController',
        function($rootScope, $scope, $state, $location, $window, resolvedCourseId, resolvedCoursePlayerData, AlertsService) {

            var data = {
                    courseData: resolvedCoursePlayerData.courseData,
                    selectedActivityId: $state.params.activityId || -1,
                    activityStatuses: {},
                    percentComplete: 0
                },

                returnPath = $state.params.returnPath,
                activityStatusListener = null,
                initialCourseCompleteChecked = false,
                courseInitiallyComplete = false,
                courseCompleteDisplayed = false,

                init = function() {
                    data.sections = prepareSections($state.params.sectionId || -1, data.courseData.sections);
                    refreshActivityStatuses(resolvedCoursePlayerData.activityStatuses);

                    activityStatusListener = $rootScope.$on('course-player-service.activityStatusesUpdated', function(event, eventData) {
                        refreshActivityStatuses(eventData.activityStatuses);
                    });

                    $scope.$on("$destroy", function() {
                        activityStatusListener();
                    });

                    processActivityRedirect($state.params.step, data.selectedActivityId, data.sections);
                },

                refreshActivityStatuses = function(activityStatuses) {
                    if (activityStatuses) {
                        data.activityStatuses = activityStatuses;

                        updatePercentComplete(data.sections, data.activityStatuses, data.sections);
                    }
                },

                updatePercentComplete = function(sections, activityStatuses) {
                    var i,
                        iLen,
                        j,
                        jLen,
                        activity,
                        activityCount = 0,
                        activityCompleteCount = 0;

                    // TODO: (WJK) Potentially move this logic server-side for course itself
                    for (i = 0, iLen = sections.length; i < iLen; i += 1) {
                        if (sections[i].activities) {
                            for (j = 0, jLen = sections[i].activities.length; j < jLen; j += 1) {
                                activity = sections[i].activities[j];
                                activityCount += 1;
                                if (activityStatuses[activity.id] && data.activityStatuses[activity.id].status === 1) {
                                    activityCompleteCount += 1;
                                }
                            }
                        }
                    }

                    if (activityCount === 0) {
                        data.percentComplete = 100;
                    } else {
                        data.percentComplete = Math.ceil((activityCompleteCount / activityCount) * 100);
                    }

                    if (!initialCourseCompleteChecked) {
                        initialCourseCompleteChecked = true;
                        courseInitiallyComplete = activityCount === activityCompleteCount;
                    } else if (!courseInitiallyComplete && !courseCompleteDisplayed && (activityCount === activityCompleteCount)) {
                        courseCompleteDisplayed = true;
                        AlertsService.alert({
                            title: "Course Complete",
                            text: "You have successfully completed the course."
                        });
                    }
                },

                prepareSections = function(sectionId, sections) {
                    var i,
                        len,
                        section,
                        lastVisibleIndex = 0;

                    sections = sections || [];

                    for (i = 0, len = sections.length; i < len; i++) {
                        section = sections[i];
                        if (section.activities && section.activities.length) {
                            lastVisibleIndex = i;
                        }
                        if ((sectionId === -1 && i === 0) || (section.id === sectionId)) {
                            section.showActivities = true;
                        }
                    }
                    if (lastVisibleIndex < sections.length) {
                        sections[lastVisibleIndex].lastVisible = true;
                    }

                    return sections;
                },

                processActivityRedirect = function(step, currentActivityId, sections) {
                    var i,
                        iLen,
                        j,
                        jLen,
                        section,
                        activity,
                        redirectSection,
                        redirectActivity;


                    if (currentActivityId === -1 || step === 'resume') {
                        for (i = 0, iLen = sections.length; i < iLen && !redirectActivity; i += 1) {
                            section = sections[i];
                            if (section.activities) {
                                for (j = 0, jLen = section.activities.length; j < jLen && !redirectActivity; j += 1) {
                                    activity = section.activities[j];
                                    if (step === 'resume') {
                                        if (!isActivityComplete(activity) && !isActivityLocked(activity)) {
                                            redirectSection = section;
                                            redirectActivity = activity;
                                        }
                                    } else if (currentActivityId === -1) {
                                        redirectSection = section;
                                        redirectActivity = activity;
                                    }
                                }
                            }
                        }
                    }

                    if (redirectActivity) {
                        $state.go("course-player-activity", {
                            courseId: resolvedCourseId,
                            sectionId: redirectSection.id,
                            activityId: redirectActivity.id,
                            step: null
                        });
                    }
                },

                goBack = function() {
                    if (returnPath) {
                        $location.url(returnPath);
                        return;
                    }

                    $state.go("course-activity", {
                        courseId: resolvedCourseId
                    });
                },

                goToCoursePlayer = function(section, activity) {
                    var i,
                        len;

                    if (!isActivityLocked(activity)) {
                        data.selectedActivityId = activity.id;

                        for (i = 0, len = data.sections.length; i < len; i++) {
                            if (data.sections[i].id === section.id) {
                                data.sections[i].showActivities = true;
                            }
                        }

                        $state.go("course-player-activity", {
                            courseId: resolvedCourseId,
                            sectionId: section.id,
                            activityId: activity.id
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
                goBack: goBack,
                goToCoursePlayer: goToCoursePlayer,
                isActivityLocked: isActivityLocked,
                getActivityTypeIconClass: getActivityTypeIconClass,
                getActivityCompletionIconClass: getActivityCompletionIconClass
            };
        }
    );
})();
