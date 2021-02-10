'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('course-requirements', {
            parent: 'course',
            url: '/requirements',
            data: {
                authorities: []
            },
            views: {
                'lesson@course': {
                    controller: 'CatalogCourseRequirementsController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/catalog/course/requirements/catalog-course-requirements.html'
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
