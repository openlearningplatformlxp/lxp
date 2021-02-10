'use strict';

(function() {
    var module = angular.module('app.pages');

    module.config(function($stateProvider) {
        $stateProvider.state('settings', {
            parent: 'account',
            url: '/settings',
            data: {
                pageTitle: 'Settings',
                authorities: ['ROLE_USER']
            },
            views: {
                'content@site': {
                    controller: 'SettingsController',
                    controllerAs: 'ctrl',
                    resolve: {
                        resolvedAccount: function(Principal) {
                            return Principal.identity(true).then(function(account) {
                                return account;
                            });
                        }
                    },
                    templateUrl: 'scripts/pages/account/settings/settings.html'
                }
            },
            resolve: {
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('settings');
                    return $translate.refresh();
                }
            }
        });
    });
})();