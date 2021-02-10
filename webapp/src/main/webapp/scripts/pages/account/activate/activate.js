'use strict';

(function() {
    var module = angular.module('app.pages');

    module.config(function($stateProvider) {
        $stateProvider.state('activate', {
            parent: 'account',
            url: '/activate?key',
            data: {
                pageTitle: 'Activation',
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'ActivationController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/pages/account/activate/activate.html'
                }
            },
            resolve: {
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('activate');
                    return $translate.refresh();
                }
            }
        });
    });
})();