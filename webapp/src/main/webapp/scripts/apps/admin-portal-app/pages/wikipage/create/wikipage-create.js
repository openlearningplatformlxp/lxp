'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('wikipage-create', {
            parent: 'admin',
            url: '/wikipage/create',
            data: {
                pageTitle: 'Create a new Wikipage',
                authorities: ['ROLE_WIKI_EDITOR']
            },
            views: {
                'content@site': {
                    controller: 'AdminWikipageCreateController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/wikipage/create/wikipage-create.html'
                }
            },
            resolve: {
                resolvedData: function($http, $stateParams) {
                    return {};
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wikipage');
                    return $translate.refresh();
                }
            }
        });

        $stateProvider.state('wikipage-edit', {
            parent: 'admin',
            url: '/wikipage/edit/{id:int}',
            data: {
                pageTitle: 'Edit Wikipage',
                authorities: ['ROLE_WIKI_EDITOR']
            },
            views: {
                'content@site': {
                    controller: 'AdminWikipageCreateController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/wikipage/create/wikipage-create.html'
                }
            },
            resolve: {
                resolvedData: function($http, $stateParams) {
                    return $http.get('api/admin/wikipages/' + $stateParams.id).then(
                        function success(response) {
                            return {
                                wikipage: response.data,
                                wikipageId: $stateParams.id
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
                    $translatePartialLoader.addPart('wikipage');
                    return $translate.refresh();
                }
            }
        });
    });
})();
