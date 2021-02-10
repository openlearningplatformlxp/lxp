'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('logs', {
            parent: 'admin',
            url: '/logs',
            data: {
                pageTitle: 'Logs',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'LogsController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/logs/logs.html'
                }
            },
            resolve: {
                resolvedLoggers: function(LogsService) {
                    return LogsService.findAll().$promise;
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('logs');
                    return $translate.refresh();
                }
            }
        });
    });
})();
