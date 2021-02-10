'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('audit-searches', {
            parent: 'admin',
            url: '/audit-searches',
            data: {
                pageTitle: 'Audit Searches',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminAuditSearchesController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/audit-searches/audit-searches.html'
                }
            },
            resolve: {
                resolvedLastIndex: function($http, $q) {
                    return $http({
                        url: 'api/admin/audit-searches/lastIndex',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },
                resolvedPagedSearch: function(SearchService) {
                    return SearchService.getPagedSearchInstancePromise('api/admin/audit-searches/search', {
                        initialSearchValue: '',
                        initialSort: {
                            field: 'createdDate',
                            dir: 'DESC'
                        },
                        sortFields: ['createdDate']
                    }).then(function(pagedSearch) {
                        return pagedSearch;
                    });
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('audit-searches');
                    return $translate.refresh();
                }
            }
        });
    });
})();
