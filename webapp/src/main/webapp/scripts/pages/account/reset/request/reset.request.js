'use strict';

(function() {
    var module = angular.module('app.pages');

    module.config(function($stateProvider) {
        $stateProvider.state('requestReset', {
            parent: 'account',
            url: '/reset/request',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'RequestResetController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/pages/account/reset/request/reset.request.html'
                }
            },
            resolve: {
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reset');
                    return $translate.refresh();
                }
            }
        });
    });
})();