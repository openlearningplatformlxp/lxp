'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('wikipage-tree', {
            parent: 'admin',
            url: '/wikipage/tree',
            data: {
                pageTitle: 'Wikipage Folder Tree',
                authorities: ['ROLE_WIKI_EDITOR']
            },
            views: {
                'content@site': {
                    controller: 'AdminWikipageTreeController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/wikipage/tree/wikipage-tree.html'
                }
            },
            resolve: {
                resolvedData: function($http, $stateParams) {
                    return {};
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wikipage');
                    return $translate.refresh();
                }
            }
        });
    });
})();
