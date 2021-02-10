'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('cms-blocks', {
            parent: 'admin',
            url: '/cms-blocks',
            data: {
                pageTitle: 'CMS Blocks',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminCmsBlocksController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/cms/cms-blocks/cms-blocks.html'
                }
            },
            resolve: {
                resolvedPagedSearch: function(SearchService) {
                    return SearchService.getPagedSearchInstancePromise('api/cms/blocks/search', {
                        initialSearchValue: '',
                        initialSort: {
                            field: 'name'
                        },
                        initialSortSecondary: {
                            field: 'key'
                        },
                        sortFields: ['description', 'key', 'name']
                    }).then(function(pagedSearch) {
                        return pagedSearch;
                    });
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cms-blocks');
                    return $translate.refresh();
                }
            }
        });
    });
})();
