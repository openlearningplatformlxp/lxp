'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('assets', {
            parent: 'admin',
            url: '/assets',
            data: {
                pageTitle: 'Assets',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminAssetsController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/asset/assets/feedbacks.html'
                }
            },
            resolve: {
                resolvedPagedSearch: function(SearchService) {
                    return SearchService.getPagedSearchInstancePromise('api/asset/search', {
                        initialSearchValue: '',
                        initialSort: {
                            field: 'path/filename'
                        },
                        sortFields: ['assetStoreType', 'assetSubtype', 'path/filename', 'name']
                    }).then(function(pagedSearch) {
                        return pagedSearch;
                    });
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('assets');
                    return $translate.refresh();
                }
            }
        });
    });
})();
