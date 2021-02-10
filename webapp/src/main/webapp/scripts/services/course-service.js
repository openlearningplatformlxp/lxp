'use strict';

(function() {
    var module = angular.module('app.services');

    module.service('CourseService',
        function($http, $q, $location) {
            var getCourseData = function(params) {
                    return $http({
                        url: 'api/course/' + params.courseId,
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                getCourseOverviewData = function(params) {
                    return $http({
                        url: 'api/course/' + params.courseId + '/overview/',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                getCourseSectionData = function(params) {
                    return $http({
                        url: 'api/course/' + params.courseId + '/sections/',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                getCourseUpcomingData = function(params) {
                    return $http({
                        url: 'api/course/' + params.courseId + '/upcoming/',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                getCoursePrerequisitesData = function(params) {
                    return $http({
                        url: 'api/course/' + params.courseId + '/prerequisites/',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                getCourseResourcesData = function(params) {
                    return $http({
                        url: 'api/course/' + params.courseId + '/resources/',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                getCourseProgressionOverview = function(params) {
                    return $http({
                        url: 'api/course/' + params.courseId + '/' + params.memberId + '/progression/',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                getProgramOverviewData = function(params) {
                    var personalUrl = params.personal === "true" ? 'personal/' : '';
                    return $http({
                        url: 'api/course/program/' + personalUrl + params.programId,
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        if (response.status == 404) {
                            $location.url('/');
                        } else {
                            return $q.reject(response);
                        }
                    });
                },

                getPersonalPrograms = function() {
                    return $http({
                        url: 'api/course/program/personal/',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                getSharedProgramProgressionOverview = function(params) {
                    return $http({
                        url: 'api/course/program/' + params.programId + '/' + params.memberId + '/shared/progression/',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                getProgramProgressionOverview = function(params) {
                    return $http({
                        url: 'api/course/program/' + params.programId + '/' + params.memberId + '/progression/',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                convertFloatDurationToHoursAndMinutes = function(duration) {
                    var hours,
                        minutes;

                    if (!duration || duration < 0) {
                        return null;
                    }

                    hours = Math.floor(duration);
                    minutes = Math.ceil((duration - hours) * 60.0);

                    return {
                        hours: hours,
                        minutes: minutes
                    }
                },

                convertMinutesDurationToHoursAndMinutes = function(duration) {
                    var hours,
                        minutes;

                    if (!duration || duration < 0) {
                        return null;
                    }

                    hours = Math.floor(duration / 60.0);
                    minutes = Math.ceil(duration - (hours * 60.0));

                    return {
                        hours: hours,
                        minutes: minutes
                    }
                };

            return {
                getCourseData: getCourseData,
                getCourseOverviewData: getCourseOverviewData,
                getCourseSectionData: getCourseSectionData,
                getCourseUpcomingData: getCourseUpcomingData,
                getCoursePrerequisitesData: getCoursePrerequisitesData,
                getCourseResourcesData: getCourseResourcesData,
                getCourseProgressionOverview: getCourseProgressionOverview,
                getPersonalPrograms: getPersonalPrograms,
                getProgramOverviewData: getProgramOverviewData,
                getProgramProgressionOverview: getProgramProgressionOverview,
                getSharedProgramProgressionOverview: getSharedProgramProgressionOverview,
                convertFloatDurationToHoursAndMinutes: convertFloatDurationToHoursAndMinutes,
                convertMinutesDurationToHoursAndMinutes: convertMinutesDurationToHoursAndMinutes
            };
        }
    );
})();
