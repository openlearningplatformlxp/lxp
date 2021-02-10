'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('course-upcoming', {
            parent: 'course',
            url: '/upcoming',
            data: {
                authorities: []
            },
            views: {
                'lesson@course': {
                    controller: 'CatalogCourseUpcomingController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/catalog/course/upcoming/catalog-course-upcoming.html'
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
