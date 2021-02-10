'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('CatalogCourseOverviewController',
        function($stateParams, CourseService) {

            var data = {};
            var init = function() {
                    CourseService.getCourseOverviewData({
                        courseId: $stateParams.courseId
                    }).then(function success(response) {
                        data.overview = response;
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
