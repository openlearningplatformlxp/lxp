'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('featured-searches-create', {
            parent: 'admin',
            url: '/featured-searches/create',
            data: {
                pageTitle: 'Create a new Featured Search',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminFeaturedSearchCreateController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/featured-searches/create/featured-searches-create.html'
                }
            },
            resolve: {
                resolvedData: function($http, $stateParams) {
                    return {};
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('featured-searches');
                    return $translate.refresh();
                }
            }
        });

        $stateProvider.state('featured-searches-edit', {
            parent: 'admin',
            url: '/featured-searches/edit/{id:int}',
            data: {
                pageTitle: 'Edit Featured Search',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminFeaturedSearchCreateController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/featured-searches/create/featured-searches-create.html'
                }
            },
            resolve: {
                resolvedData: function($http, $stateParams) {
                    return $http.get('api/admin/featured-searches/' + $stateParams.id).then(
                        function success(response) {
                            return {
                                featuredSearch: response.data,
                                featuredSearchId: $stateParams.id
                            };
                        },
                        function error(response) {
                            return {
                                httpError: response
                            };
                        }
                    );
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('featured-searches');
                    return $translate.refresh();
                }
            }
        });
    });
})();
