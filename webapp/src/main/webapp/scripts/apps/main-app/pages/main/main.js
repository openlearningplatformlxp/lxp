'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

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
                        resolvedAccountInfo: function(Principal) {
                            return Principal.identity().then(function(account) {
                                return {
                                    account: account,
                                    isAuthenticated: Principal.isAuthenticated()
                                };
                            });
                        }
                    },
                    templateUrl: 'scripts/apps/main-app/pages/main/main.html'
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