'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('discover', {
            parent: 'site',
            url: '/discover',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'DiscoverController',
                    controllerAs: 'ctrl',

                    templateUrl: 'scripts/apps/main-app/pages/discover/discover.html'
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
