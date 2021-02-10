'use strict';

(function() {
    var module = angular.module('app.pages');

    module.config(function($stateProvider) {
        $stateProvider.state('sessions', {
            parent: 'account',
            url: '/sessions',
            data: {
                pageTitle: 'Sessions',
                authorities: ['ROLE_USER']
            },
            views: {
                'content@site': {
                    controller: 'SessionsController',
                    controllerAs: 'ctrl',
                    resolve: {
                        resolvedAccount: function(Principal) {
                            return Principal.identity(true).then(function(account) {
                                return account;
                            });
                        },
                        resolvedSessions: function(Sessions) {
                            return Sessions.getAll().$promise;
                        }
                    },
                    templateUrl: 'scripts/pages/account/sessions/sessions.html'
                }
            },
            resolve: {
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sessions');
                    return $translate.refresh();
                }
            }
        });
    });
})();