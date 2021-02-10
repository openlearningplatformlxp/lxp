'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('course', {
            parent: 'site',
            'abstract': true,
            url: '/catalog/course/{courseId:int}?{returnPath}',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'CatalogCourseController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/catalog/course/catalog-course.html'
                },
                'footer@site': {
                    template: '<div></div>'
                }
            },
            resolve: {
                mainTranslatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                },

                resolvedCourseData: function($stateParams, CourseService) {
                    return CourseService.getCourseData({
                        courseId: $stateParams.courseId
                    });
                },

                resolvedCourseSectionData: function($stateParams, CourseService) {
                    return CourseService.getCourseSectionData({
                        courseId: $stateParams.courseId
                    });
                }
            }
        });
    });
})();
