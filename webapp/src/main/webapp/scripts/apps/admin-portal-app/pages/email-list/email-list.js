'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('email-list', {
            parent: 'admin',
            url: '/email-list',
            data: {
                pageTitle: 'Email List',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminEmailListController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/email-list/email-list.html'
                }
            },
            resolve: {
                resolvedPagedSearch: function(SearchService) {
                    return SearchService.getPagedSearchInstancePromise('api/admin/email/search', {
                        initialSearchValue: '',
                        initialSort: {
                            field: 'lastModifiedDate',
                            dir: 'DESC'
                        },
                        sortFields: ['lastModifiedDate', 'to', 'subject', 'sentDate', 'attemptCount', 'lastAttemptDate']
                    }).then(function(pagedSearch) {
                        return pagedSearch;
                    });
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('email-list');
                    return $translate.refresh();
                }
            }
        });
    });
})();
