'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('home', {
            parent: 'site',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'MainController',
                    controllerAs: 'ctrl',
                    resolve: {
                        resolvedAccountInfo: function($state, Principal) {
                            return Principal.identity().then(function(account) {
                                if (Principal.isAuthenticated()) {
                                    if (Principal.hasAuthority('ROLE_ADMIN')) {
                                        $state.go('admin-status');
                                    }
                                }

                                return {
                                    account: account,
                                    isAuthenticated: Principal.isAuthenticated()
                                };
                            });
                        }
                    },
                    templateUrl: 'scripts/apps/admin-portal-app/pages/main/main.html'
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