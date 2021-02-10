'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('user-roles', {
            parent: 'admin',
            url: '/user-roles',
            data: {
                authorities: ['USERS_ROLE_VIEW', 'USERS_ROLE_CREATE', 'USERS_ROLE_UPDATE', 'USERS_ROLE_DELETE'],
                pageTitle: 'User Roles'
            },
            views: {
                'content@site': {
                    controller: 'AdminUserRolesController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/user-role/user-roles/user-roles.html'
                }
            },
            resolve: {
                resolvedPagedSearch: function(SearchService) {
                    return SearchService.getPagedSearchInstancePromise('api/admin/roles/search', {
                        initialSearchValue: '',
                        initialSort: {
                            field: 'name'
                        },
                        sortFields: ['name']
                    }).then(function(pagedSearch) {
                        return pagedSearch;
                    });
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('user-role');
                    return $translate.refresh();
                }
            }
        });
    });
})();
