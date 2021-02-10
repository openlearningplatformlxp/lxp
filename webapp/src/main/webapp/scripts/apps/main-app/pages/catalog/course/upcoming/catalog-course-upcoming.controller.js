'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('CatalogCourseUpcomingController',
        function($stateParams, resolvedCourseData, CourseService) {
            var data = {};
            var init = function() {

                    CourseService.getCourseUpcomingData({
                        courseId: $stateParams.courseId
                    }).then(function success(response) {
                        data.upcoming = response;

                        var now = new Date();

                        data.upcoming.upcomingClassList.forEach(function(clazz) {
                            clazz.courseId = resolvedCourseData.courseId;
                            if (moment(clazz.endTime).isBefore()) {
                                clazz.status = 'SESSION_OVER';
                            } else if (clazz.enrolled) {
                                clazz.status = 'ENROLLED';
                            } else if (clazz.soldOut) {
                                clazz.status = 'SOLD_OUT';
                            } else if (clazz.requestPending) {
                                clazz.status = 'REQUESTED_APPROVAL';
                            } else if (clazz.requiresManagerApproval) {
                                clazz.status = 'REQUIRES_MANAGER_APPROVAL';
                            } else {
                                clazz.status = 'ACTIVE';
                            }
                        });
                    }, function(error) {
                        console.log(error);
                    });
                },

                zend;

            init();

            var notYetImplemented = function() {
                alert("Not yet Implemented");
            };


            return {
                data: data,
                notYetImplemented: notYetImplemented
            };
        }
    );
})();
