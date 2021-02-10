'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('support', {
            parent: 'site',
            url: '/support',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'SupportController',
                    controllerAs: 'ctrl',

                    templateUrl: 'scripts/apps/main-app/pages/support/support.html'
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
