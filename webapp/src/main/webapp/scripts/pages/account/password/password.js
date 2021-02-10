'use strict';

(function() {
    var module = angular.module('app.pages');

    module.config(function($stateProvider) {
        $stateProvider.state('password', {
            parent: 'account',
            url: '/password',
            data: {
                pageTitle: 'Password',
                authorities: ['ROLE_USER']
            },
            views: {
                'content@site': {
                    controller: 'PasswordController',
                    controllerAs: 'ctrl',
                    resolve: {
                        resolvedAccount: function(Principal) {
                            return Principal.identity().then(function(account) {
                                return account;
                            });
                        }
                    },
                    templateUrl: 'scripts/pages/account/password/password.html'
                }
            },
            resolve: {
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('password');
                    return $translate.refresh();
                }
            }
        });
    });
})();