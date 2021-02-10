'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('course-overview', {
            parent: 'course',
            url: '/overview',
            data: {
                authorities: []
            },
            views: {
                'lesson@course': {
                    controller: 'CatalogCourseOverviewController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/catalog/course/overview/catalog-course-overview.html'
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
