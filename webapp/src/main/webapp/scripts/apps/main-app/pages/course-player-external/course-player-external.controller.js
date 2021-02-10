'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('CoursePlayerExternalController',
        function($state, resolvedCourseId, resolvedActivityId, CoursePlayerService) {

            var data = {
                    courseId: resolvedCourseId,
                    activityId: resolvedActivityId
                },

                init = function() {

                    CoursePlayerService.getActivityData({
                        courseId: resolvedCourseId,
                        activityId: resolvedActivityId
                    }).then(function success(response) {
                        data.activityConfig = {
                            activityData: response
                        };
                    }, function(error) {
                        console.log(error);
                    });
                },

                zend;

            init();

            return {
                data: data
            };
        }
    );
})();
