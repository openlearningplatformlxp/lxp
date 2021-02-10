'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('health', {
            parent: 'admin',
            url: '/health',
            data: {
                pageTitle: 'Health',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'HealthController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/health/health.html'
                }
            },
            resolve: {
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('health');
                    return $translate.refresh();
                }
            }
        });
    });
})();
