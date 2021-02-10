'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('featured-searches', {
            parent: 'admin',
            url: '/featured-searches',
            data: {
                pageTitle: 'Featured Searches',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminFeaturedSearchesController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/featured-searches/featured-searches.html'
                }
            },
            resolve: {
                resolvedPagedSearch: function(SearchService) {
                    return SearchService.getPagedSearchInstancePromise('api/admin/featured-searches/search', {
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
                    $translatePartialLoader.addPart('featured-searches');
                    return $translate.refresh();
                }
            }
        });
    });
})();
