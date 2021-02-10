'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('asset-add', {
            parent: 'admin',
            url: '/asset-add',
            data: {
                pageTitle: 'Create Asset',
                authorities: ['ROLE_ADMIN'],
                breadcrumbs: [{
                        name: 'global.breadcrumbs.home',
                        stateName: 'home'
                    },
                    {
                        name: 'global.breadcrumbs.assets',
                        stateName: 'assets'
                    },
                    {
                        name: 'global.breadcrumbs.asset-add',
                        stateName: 'asset-add'
                    }
                ]
            },
            views: {
                'content@site': {
                    controller: 'AdminAssetAddEditController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/asset/asset-add-edit/asset-add-edit.html'
                }
            },
            resolve: {
                resolvedData: function($http) {
                    return $http.get('api/asset/pages/asset-upsert').then(
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
                    $translatePartialLoader.addPart('assets');
                    return $translate.refresh();
                }
            }
        });

        $stateProvider.state('asset-edit', {
            parent: 'admin',
            url: '/asset-edit/{assetId:int}',
            data: {
                pageTitle: 'Edit Asset',
                authorities: ['ROLE_ADMIN'],
                breadcrumbs: [{
                        name: 'global.breadcrumbs.home',
                        stateName: 'home'
                    },
                    {
                        name: 'global.breadcrumbs.assets',
                        stateName: 'assets'
                    },
                    {
                        name: 'global.breadcrumbs.asset-edit',
                        stateName: 'asset-edit'
                    }
                ]
            },
            views: {
                'content@site': {
                    controller: 'AdminAssetAddEditController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/asset/asset-add-edit/asset-add-edit.html'
                }
            },
            resolve: {
                resolvedData: function($http, $stateParams) {
                    return $http.get('api/asset/pages/asset-upsert/' + $stateParams.assetId).then(
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
                    $translatePartialLoader.addPart('assets');
                    return $translate.refresh();
                }
            }
        });
    });
})();
