/**
 * TODO: SAC: should the login be editable? (I think yes!)
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('user-add', {
            parent: 'admin',
            url: '/user-add',
            data: {
                pageTitle: 'Create User',
                authorities: ['ROLE_ADMIN'],
                breadcrumbs: [{
                        name: 'global.breadcrumbs.home',
                        stateName: 'home'
                    },
                    {
                        name: 'global.breadcrumbs.users',
                        stateName: 'users'
                    },
                    {
                        name: 'global.breadcrumbs.user-add',
                        stateName: 'user-add'
                    }
                ]
            },
            views: {
                'content@site': {
                    controller: 'AdminUserAddEditController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/user-add-edit/user-add-edit.html'
                }
            },
            resolve: {
                resolvedData: function($http) {
                    return $http.get('api/admin/pages/user-upsert').then(
                        function success(response) {
                            return {
                                availableAuthorities: response.data.availableAuthorities
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
                    $translatePartialLoader.addPart('users');
                    return $translate.refresh();
                }
            }
        });

        $stateProvider.state('user-edit', {
            parent: 'admin',
            url: '/user-edit/{userId:int}',
            data: {
                pageTitle: 'Edit User',
                authorities: ['ROLE_ADMIN'],
                breadcrumbs: [{
                        name: 'global.breadcrumbs.home',
                        stateName: 'home'
                    },
                    {
                        name: 'global.breadcrumbs.users',
                        stateName: 'users'
                    },
                    {
                        name: 'global.breadcrumbs.user-edit',
                        stateName: 'user-edit'
                    }
                ]
            },
            views: {
                'content@site': {
                    controller: 'AdminUserAddEditController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/user-add-edit/user-add-edit.html'
                }
            },
            resolve: {
                resolvedData: function($http, $stateParams) {
                    return $http.get('api/admin/pages/user-upsert/' + $stateParams.userId).then(
                        function success(response) {
                            return {
                                appSecurityImpersonateEnabled: response.data.appSecurityImpersonateEnabled,
                                availableAuthorities: response.data.availableAuthorities,
                                user: response.data.user,
                                userId: $stateParams.userId
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
                    $translatePartialLoader.addPart('users');
                    return $translate.refresh();
                }
            }
        });
    });
})();
