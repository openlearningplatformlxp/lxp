'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('CatalogCourseController',
        function($state, $location, $rootScope, $scope, resolvedCourseData, resolvedCourseSectionData, CourseEnrollmentService) {

            var data = {
                    courseData: {
                        remainingTime: '36 hours',
                        status: 'IN_PROGRESS',
                        type: 'ONLINE_COURSE',
                        title: resolvedCourseData.courseFullName,
                        subtitle: 'Online Course',
                        description: '',
                        sectionType: resolvedCourseData.firstTopic,
                        percentComplete: resolvedCourseSectionData.courseProgress.percentComplete,
                        mobile: true
                    },
                    returnPath: $state.params.returnPath,
                    showResume: !!resolvedCourseData.enrolled,
                    showEnroll: !resolvedCourseData.enrolled
                },

                enrollmentStatusListener = null,

                init = function() {
                    enrollmentStatusListener = $rootScope.$on('course-enrollment-service.courseEnrolled', function(event, eventData) {
                        if (eventData.courseId === resolvedCourseData.courseId) {
                            resolvedCourseData.enrolled = true;
                            data.showResume = true;
                            data.showEnroll = false;
                        }
                    });

                    $scope.$on("$destroy", function() {
                        enrollmentStatusListener();
                    });
                },

                getNumArray = function(num) {
                    return new Array(num);
                },

                enrollInCourse = function() {
                    CourseEnrollmentService.enrollInCourse({
                        courseId: resolvedCourseData.courseId
                    }).then(function success(response) {
                        resolvedCourseData.enrolled = true;
                        data.showResume = true;
                        data.showEnroll = false;
                    }, function(error) {
                        console.log(error);
                    });
                },

                resumeCourse = function() {
                    $state.go("course-player", {
                        courseId: resolvedCourseData.courseId,
                        step: "resume",
                        returnPath: $location.url()
                    });
                },

                zend;

            init();

            return {
                data: data,
                getNumArray: getNumArray,
                enrollInCourse: enrollInCourse,
                resumeCourse: resumeCourse
            };
        }
    );
})();
