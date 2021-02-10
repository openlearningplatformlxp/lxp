'use strict';

(function() {
    var module = angular.module('app.pages');

    module.config(function($stateProvider) {
        $stateProvider.state('login', {
            parent: 'account',
            url: '/login?error&manual',
            data: {
                pageTitle: 'Login',
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'LoginController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/pages/account/login/login.html'
                }
            },
            resolve: {
                resolvedError: function($stateParams) {
                    return $stateParams.error;
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('login');
                    return $translate.refresh();
                }
            }
        });
    });
})();