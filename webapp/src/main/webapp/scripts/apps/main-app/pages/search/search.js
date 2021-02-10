'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('search', {
            parent: 'site',
            url: '/search/?{q:string}',
            data: {
                authorities: []
            },
            params: {
                q: ""
            },
            views: {
                'content@site': {
                    controller: 'SearchController',
                    controllerAs: 'ctrl',

                    templateUrl: 'scripts/apps/main-app/pages/search/search.html'
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
