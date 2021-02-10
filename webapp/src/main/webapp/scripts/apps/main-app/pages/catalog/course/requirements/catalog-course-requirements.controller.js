'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('CatalogCourseRequirementsController',
        function($stateParams, CourseService) {
            var data = {},
                init = function() {
                    CourseService.getCoursePrerequisitesData({
                        courseId: $stateParams.courseId
                    }).then(function success(response) {
                        data.sections = prepareSections(response.sections);
                    }, function(error) {
                        console.log(error);
                    });
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
                };
            init();


            return {
                data: data,
                getNumArray: getNumArray,
            };
        }
    );
})();
