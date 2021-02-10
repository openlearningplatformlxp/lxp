'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('spring-session-list', {
            parent: 'admin',
            url: '/spring-session-list',
            data: {
                pageTitle: 'Spring Session List',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminSpringSessionListController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/spring-session-list/spring-session-list.html'
                }
            },
            resolve: {
                resolvedPagedSearch: function(SearchService) {
                    return SearchService.getPagedSearchInstancePromise('api/admin/spring-sessions/search', {
                        initialSearchValue: '',
                        initialSort: {
                            field: 'lastAccessTime',
                            dir: 'DESC'
                        },
                        sortFields: ['creationTime', 'lastAccessTime', 'maxInactiveInterval', 'principalName', 'sessionId']
                    }).then(function(pagedSearch) {
                        return pagedSearch;
                    });
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('spring-session-list');
                    return $translate.refresh();
                }
            }
        });
    });
})();
