'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('discovery-program-upsert', {
            parent: 'admin',
            url: '/discovery/program-upsert',
            params: {
                discoveryProgramItem: null
            },
            data: {
                pageTitle: 'Add/Edit Discovery Program',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminDiscoveryProgramUpsertController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/discovery/program-upsert/discovery-program-upsert.html'
                }
            },
            resolve: {
                resolvedProgramsData: function($http, $stateParams) {
                    return $http.get('api/admin/programs/').then(
                        function success(response) {
                            return {
                                programs: response.data
                            };
                        },
                        function error(response) {
                            return {
                                httpError: response
                            };
                        }
                    );
                },
                resolvedDiscoveryTypes: function($http) {
                    return $http.get('api/admin/discovery/types').then(
                        function success(response) {
                            return response.data;
                        },
                        function error(response) {
                            return {
                                httpError: response
                            };
                        }
                    );
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('discovery');
                    return $translate.refresh();
                }
            }
        });
    });
})();
