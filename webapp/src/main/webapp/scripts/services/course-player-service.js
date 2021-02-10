'use strict';

(function() {
    var module = angular.module('app.services');

    module.service('CoursePlayerService',
        function($rootScope, $http, $q) {

            var getCoursePlayerData = function(params) {
                    return $http({
                        url: 'api/courseplayer/course/' + params.courseId + "?includeActivities=true",
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                getActivityData = function(params) {
                    return $http({
                        url: 'api/courseplayer/activity/' + params.courseId + '/' + params.activityId,
                        method: 'GET'
                    }).then(function success(response) {
                        if (response.data.activityStatuses) {
                            $rootScope.$broadcast("course-player-service.activityStatusesUpdated", {
                                activityStatuses: response.data.activityStatuses
                            });
                        }
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                markActivityComplete = function(params) {
                    return $http({
                        url: 'api/courseplayer/activity/complete/' + params.courseId + '/' + params.activityId,
                        method: 'POST'
                    }).then(function success(response) {
                        if (response.data.activityStatuses) {
                            $rootScope.$broadcast("course-player-service.activityStatusesUpdated", {
                                activityStatuses: response.data.activityStatuses
                            });
                        }
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                markActivityIncomplete = function(params) {
                    return $http({
                        url: 'api/courseplayer/activity/incomplete/' + params.courseId + '/' + params.activityId,
                        method: 'POST'
                    }).then(function success(response) {
                        if (response.data.activityStatuses) {
                            $rootScope.$broadcast("course-player-service.activityStatusesUpdated", {
                                activityStatuses: response.data.activityStatuses
                            });
                        }
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                submitQuizActivity = function(params) {
                    return $http({
                        url: 'api/courseplayer/activity/quiz/submit/' + params.courseId + '/' + params.activityId,
                        method: 'POST',
                        data: params.data
                    }).then(function success(response) {
                        if (response.data.activityStatuses) {
                            $rootScope.$broadcast("course-player-service.activityStatusesUpdated", {
                                activityStatuses: response.data.activityStatuses
                            });
                        }
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                };

            return {
                getCoursePlayerData: getCoursePlayerData,
                getActivityData: getActivityData,
                markActivityComplete: markActivityComplete,
                markActivityIncomplete: markActivityIncomplete,
                submitQuizActivity: submitQuizActivity
            };
        }
    );
})();
