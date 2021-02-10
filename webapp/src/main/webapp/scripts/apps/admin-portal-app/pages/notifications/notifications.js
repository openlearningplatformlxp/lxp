'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('notifications', {
            parent: 'admin',
            url: '/notifications',
            data: {
                pageTitle: 'Notifications',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminNotificationsController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/notifications/notifications.html'
                }
            },
            resolve: {
                resolvedPagedSearch: function(SearchService) {
                    return SearchService.getPagedSearchInstancePromise('api/admin/notifications/search', {
                        initialSearchValue: '',
                        initialSort: {
                            field: 'createdDate',
                            dir: 'DESC'
                        },
                        initialSortSecondary: {
                            field: 'title'
                        },
                        searchOptions: {
                            enabled: '1'
                        },
                        sortFields: ['createdDate', 'title', 'message']
                    }).then(function(pagedSearch) {
                        return pagedSearch;
                    });
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notifications');
                    return $translate.refresh();
                }
            }
        });
    });
})();
