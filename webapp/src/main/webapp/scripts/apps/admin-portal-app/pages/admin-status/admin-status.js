'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('admin-status', {
            parent: 'site',
            url: '/admin-status',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_WIKI_EDITOR']
            },
            views: {
                'content@site': {
                    controller: 'AdminStatusController',
                    controllerAs: 'ctrl',
                    resolve: {
                        resolvedPage: function($http) {
                            return $http.get('api/admin/pages/status').then(
                                function success(response) {
                                    return response.data;
                                },
                                function error() {
                                    return {};
                                }
                            );
                        }
                    },
                    templateUrl: 'scripts/apps/admin-portal-app/pages/admin-status/admin-status.html'
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
