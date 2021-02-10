'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('users', {
            parent: 'admin',
            url: '/users',
            data: {
                pageTitle: 'Users',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminUsersController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/users/users.html'
                }
            },
            resolve: {
                resolvedPagedSearch: function(SearchService) {
                    return SearchService.getPagedSearchInstancePromise('api/admin/persons/search', {
                        initialSearchValue: '',
                        initialSort: {
                            field: 'firstName'
                        },
                        initialSortSecondary: {
                            field: 'lastName'
                        },
                        sortFields: ['firstName', 'lastName', 'login', 'email']
                    }).then(function(pagedSearch) {
                        return pagedSearch;
                    });
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('users');
                    return $translate.refresh();
                }
            }
        });
    });
})();
