'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('discovery', {
            parent: 'admin',
            url: '/discovery',
            data: {
                pageTitle: 'Discovery',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminDiscoveryController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/discovery/discovery.html'
                }
            },
            resolve: {
                discoveryProgramData: function(AdminDiscoveryService) {
                    return AdminDiscoveryService.getDiscoveryProgramData().then(function(response) {
                        return response;
                    });
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('discovery');
                    return $translate.refresh();
                }
            }
        });
    });
})();
