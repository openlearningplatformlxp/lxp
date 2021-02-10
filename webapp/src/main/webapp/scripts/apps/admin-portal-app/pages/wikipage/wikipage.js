'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('wikipages', {
            parent: 'admin',
            url: '/wikipages',
            data: {
                pageTitle: 'Wikipages',
                authorities: ['ROLE_WIKI_EDITOR']
            },
            views: {
                'content@site': {
                    controller: 'AdminWikipageController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/wikipage/wikipage.html'
                }
            },
            resolve: {
                resolvedPagedSearch: function(SearchService) {
                    return SearchService.getPagedSearchInstancePromise('api/admin/wikipages/search', {
                        initialSearchValue: '',
                        initialSort: {
                            field: 'createdDate',
                            dir: 'DESC'
                        },
                        sortFields: ['type', 'createdDate']
                    }).then(function(pagedSearch) {
                        return pagedSearch;
                    });
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wikipage');
                    return $translate.refresh();
                }
            }
        });
    });
})();
