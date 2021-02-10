'use strict';

(function() {
    var module = angular.module('app.services');

    module.service('CourseEnrollmentService',
        function($rootScope, $http, $state, $location, $q, AlertsService) {

            var requestApproval = function(params) {
                    return $http({
                        url: 'api/course-enrollment/approval/' + params.sessionId,
                        method: 'POST'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                enrollInCourseSession = function(params) {
                    return $http({
                        url: 'api/course-enrollment/course/' + params.courseId + '/session/' + params.sessionId + '/enroll',
                        method: 'POST'
                    }).then(function success(response) {
                        $rootScope.$broadcast("course-enrollment-service.courseEnrolled", {
                            courseId: params.courseId
                        });
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                saveTextEntry = function(params) {
                    return $http.post('api/course/programs/' + params.programId + '/textentry', params).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                enrollInCourse = function(params) {
                    return $http({
                        url: 'api/course-enrollment/course/' + params.courseId + '/enroll',
                        method: 'POST'
                    }).then(function success(response) {
                        $rootScope.$broadcast("course-enrollment-service.courseEnrolled", {
                            courseId: params.courseId
                        });
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                enrollInProgram = function(params) {
                    return $http({
                        url: 'api/course-enrollment/program/' + params.programId + '/enroll',
                        method: 'POST'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                enrollInProgramCourse = function(params) {
                    return $http({
                        url: 'api/course-enrollment/program/' + params.programId + '/course/' + params.courseId + '/enroll',
                        method: 'POST'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                toggleManualCompletion = function(params) {
                    return $http.post('api/course/program/personal/manual/', params).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                dropProgram = function(params) {
                    return $http({
                        url: 'api/course-enrollment/program/' + params.programId + '/drop',
                        method: 'POST'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                navigateToCourse = function(courseId, isInProgress, isCompleted, programId, source, personal) {
                    $state.go("course-totara", {
                        courseId: courseId,
                        programId: programId,
                        source: source,
                        personal: personal
                    });

                    /*
                    if (!isInProgress || isCompleted) {
                        return $state.go("course-overview", {
                            courseId: courseId,
                            returnPath: $location.url()
                        });
                    }

                    // Course is incomplete and in progress: trigger resume of the course
                    return $state.go("course-player", {
                        courseId: courseId,
                        step: "resume",
                        returnPath: $location.url()
                    });
                    */
                },

                navigateToCourseWithEnrollment = function(programId, courseId, isEnrolled, isInProgress, isCompleted, source, personal) {
                    if (!isEnrolled) {
                        return enrollInProgramCourse({
                            programId: programId,
                            courseId: courseId
                        }).then(function success(response) {
                            navigateToCourse(courseId, isInProgress, isCompleted, programId, source, personal);
                        }, function(error) {
                            AlertsService.alert({
                                title: "Error enrolling in course",
                                text: "An error occurred while enrolling in this course."
                            });
                        });
                    }

                    navigateToCourse(courseId, isInProgress, isCompleted, programId, source, personal);
                };

            return {
                requestApproval: requestApproval,
                enrollInCourseSession: enrollInCourseSession,
                enrollInCourse: enrollInCourse,
                enrollInProgram: enrollInProgram,
                dropProgram: dropProgram,
                enrollInProgramCourse: enrollInProgramCourse,
                navigateToCourse: navigateToCourse,
                saveTextEntry: saveTextEntry,
                navigateToCourseWithEnrollment: navigateToCourseWithEnrollment,
                toggleManualCompletion: toggleManualCompletion,
            };
        }
    );
})();
