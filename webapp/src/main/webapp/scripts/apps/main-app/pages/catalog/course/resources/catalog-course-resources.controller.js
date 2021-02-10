'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('CatalogCourseResourcesController',
        function($stateParams, CourseService) {

            var data = {},
                init = function() {
                    CourseService.getCourseResourcesData({
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
