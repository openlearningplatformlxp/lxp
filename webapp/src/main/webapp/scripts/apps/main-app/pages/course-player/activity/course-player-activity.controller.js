'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('CoursePlayerActivityController',
        function($rootScope, $scope, resolvedCourseId, resolvedSectionId, resolvedActivityId, resolvedCoursePlayerData, CoursePlayerService) {

            var data = {
                    courseId: resolvedCourseId,
                    sectionId: resolvedSectionId,
                    activityId: resolvedActivityId
                },

                nextActivityData,
                activityStatusListener = null,

                init = function() {
                    nextActivityData = determineNextActivityData(resolvedCoursePlayerData.courseData, resolvedActivityId);

                    activityStatusListener = $rootScope.$on('course-player-service.activityStatusesUpdated', function(event, eventData) {
                        var activityStatuses = eventData.activityStatuses || {};
                        if (data.activityConfig && data.activityConfig.activityData) {
                            data.activityConfig.activityData.activityStatus = activityStatuses[data.activityConfig.activityData.id];
                        }
                        if (data.activityConfig && data.activityConfig.nextActivityData) {
                            data.activityConfig.nextActivityData.activityStatus = activityStatuses[data.activityConfig.nextActivityData.id];
                        }
                    });

                    $scope.$on("$destroy", function() {
                        activityStatusListener();
                    });

                    CoursePlayerService.getActivityData({
                        courseId: resolvedCourseId,
                        activityId: resolvedActivityId
                    }).then(function success(response) {
                        var activityStatuses = response.activityStatuses || {};
                        data.activityConfig = {
                            activityData: response,
                            nextActivityData: nextActivityData ? nextActivityData.activity : null
                        };
                        if (data.activityConfig.nextActivityData) {
                            data.activityConfig.nextActivityData.activityStatus = activityStatuses[data.activityConfig.nextActivityData.id];
                        }
                    }, function(error) {
                        console.log(error);
                    });
                },

                determineNextActivityData = function(course, activityId) {
                    var i,
                        iLen,
                        j,
                        jLen,
                        section,
                        activity,
                        activityFound,
                        nextActivityData;

                    for (i = 0, iLen = course.sections.length; !nextActivityData && i < iLen; i += 1) {
                        section = course.sections[i];
                        for (j = 0, jLen = section.activities.length; !nextActivityData && j < jLen; j += 1) {
                            activity = section.activities[j];
                            if (activityFound) {
                                nextActivityData = {
                                    section: section,
                                    activity: activity
                                };
                            } else if (section.activities[j].id === activityId) {
                                activityFound = true;
                            }
                        }
                    }

                    return nextActivityData;
                },

                goToNextActivity = function() {
                    if (nextActivityData) {
                        $scope.$parent.ctrl.goToCoursePlayer(nextActivityData.section, nextActivityData.activity);
                    }
                },

                zend;

            init();

            return {
                data: data,
                goToNextActivity: goToNextActivity
            };
        }
    );
})();
