'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('user-role-add', {
            parent: 'admin',
            url: '/user-role-add',
            data: {
                authorities: ['USERS_ROLE_CREATE'],
                breadcrumbs: [{
                        name: 'global.breadcrumbs.home',
                        stateName: 'home'
                    },
                    {
                        name: 'global.breadcrumbs.user-roles',
                        stateName: 'user-roles'
                    },
                    {
                        name: 'global.breadcrumbs.user-role-add',
                        stateName: 'user-role-add'
                    }
                ],
                pageTitle: 'Create User'
            },
            views: {
                'content@site': {
                    controller: 'AdminUserRoleAddEditController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/user-role/user-role-add-edit/user-role-add-edit.html'
                }
            },
            resolve: {
                resolvedData: function($http) {
                    return $http.get('api/admin/roles/page/upsert').then(
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
                    $translatePartialLoader.addPart('user-role');
                    return $translate.refresh();
                }
            }
        });

        $stateProvider.state('user-role-edit', {
            parent: 'admin',
            url: '/user-role-edit/{roleId}',
            data: {
                authorities: ['USERS_ROLE_VIEW', 'USERS_ROLE_CREATE', 'USERS_ROLE_UPDATE', 'USERS_ROLE_DELETE'],
                breadcrumbs: [{
                        name: 'global.breadcrumbs.home',
                        stateName: 'home'
                    },
                    {
                        name: 'global.breadcrumbs.user-roles',
                        stateName: 'user-roles'
                    },
                    {
                        name: 'global.breadcrumbs.user-role-edit',
                        stateName: 'user-role-edit'
                    }
                ],
                pageTitle: 'Edit User'
            },
            views: {
                'content@site': {
                    controller: 'AdminUserRoleAddEditController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/user-role/user-role-add-edit/user-role-add-edit.html'
                }
            },
            resolve: {
                resolvedData: function($http, $stateParams) {
                    return $http.get('api/admin/roles/page/upsert/' + $stateParams.roleId).then(
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
                    $translatePartialLoader.addPart('user-role');
                    return $translate.refresh();
                }
            }
        });
    });
})();
