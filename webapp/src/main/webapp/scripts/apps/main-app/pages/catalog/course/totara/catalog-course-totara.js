'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('course-totara', {
            parent: 'site',
            url: '/totara/{courseId:int}?{externalUrl:string}',
            data: {
                authorities: []
            },
            params: {
                source: null,
                externalUrl: null,
                programId: null,
                personal: null
            },
            views: {
                'content@site': {
                    controller: 'CatalogCourseTotaraController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/catalog/course/totara/catalog-course-totara.html'
                },
                'footer@site': {
                    template: '<div></div>'
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
