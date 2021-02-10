'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('course-resources', {
            parent: 'course',
            url: '/resources',
            data: {
                authorities: []
            },
            views: {
                'lesson@course': {
                    controller: 'CatalogCourseResourcesController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/catalog/course/resources/catalog-course-resources.html'
                }
            },
            resolve: {
                mainTranslatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                }
            }
        });
    });
})();
