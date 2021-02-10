'use strict';

(function() {
    var module = angular.module('app.pages');

    module.config(function($stateProvider) {
        $stateProvider.state('finishReset', {
            parent: 'account',
            url: '/reset/finish?key',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'ResetFinishController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/pages/account/reset/finish/reset.finish.html'
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