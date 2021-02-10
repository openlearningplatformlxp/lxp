'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('notifications-user', {
            parent: 'site',
            url: '/notifications-user?link',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'NotificationsController',
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
                    templateUrl: 'scripts/apps/main-app/pages/notifications/notifications.html'
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