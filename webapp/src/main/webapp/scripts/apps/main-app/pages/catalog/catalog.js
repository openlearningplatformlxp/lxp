'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('catalog', {
            parent: 'site',
            url: '/catalog/t/:id',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'CatalogController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/catalog/catalog.html'
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
