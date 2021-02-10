'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('announcements', {
            parent: 'admin',
            url: '/announcements',
            data: {
                pageTitle: 'Announcements',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminAnnouncementController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/announcements/announcements.html'
                }
            },
            resolve: {
                resolvedPagedSearch: function(SearchService) {
                    return SearchService.getPagedSearchInstancePromise('api/admin/announcements/search', {
                        initialSearchValue: '',
                        initialSort: {
                            field: 'createdDate',
                            dir: 'DESC'
                        },
                        initialSortSecondary: {
                            field: 'message'
                        },
                        searchOptions: {
                            enabled: '1'
                        },
                        sortFields: ['createdDate', 'message']
                    }).then(function(pagedSearch) {
                        return pagedSearch;
                    });
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('announcements');
                    return $translate.refresh();
                }
            }
        });
    });
})();
