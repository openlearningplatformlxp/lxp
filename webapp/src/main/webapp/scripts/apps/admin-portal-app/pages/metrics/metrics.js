'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(
        function($stateProvider) {
            $stateProvider.state('metrics', {
                parent: 'admin',
                url: '/metrics',
                data: {
                    pageTitle: 'Metrics',
                    authorities: ['ROLE_ADMIN']
                },
                views: {
                    'content@site': {
                        controller: 'MetricsController',
                        controllerAs: 'ctrl',
                        templateUrl: 'scripts/apps/admin-portal-app/pages/metrics/metrics.html'
                    }
                },
                resolve: {
                    translatePartialLoader: function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('metrics');
                        return $translate.refresh();
                    }
                }
            });
        }
    );
})();
