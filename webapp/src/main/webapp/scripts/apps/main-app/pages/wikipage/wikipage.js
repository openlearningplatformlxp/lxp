'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    var views = {
        'content@site': {
            controller: 'WikipageController',
            controllerAs: 'ctrl',
            templateUrl: 'scripts/apps/main-app/pages/wikipage/wikipage.html'
        }
    };

    var resolve = {
        mainTranslatePartialLoader: ["$translate", "$translatePartialLoader", function($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('main');
            return $translate.refresh();
        }],
        resolvedWikipage: ["$state", "$stateParams", "$http", function($state, $stateParams, $http) {
            var pageUrl = window.location.href.split('/pages')[1];
            return $http({
                url: 'api/wikipage/?url=' + pageUrl,
                method: 'GET'
            }).then(function success(response) {
                return response.data;
            }, function error(response) {
                $state.go('home');
            });
        }]
    };

    module.config(["$stateProvider", function($stateProvider) {

        $stateProvider.state('pages', {
            parent: 'site',
            url: '/pages/:path1',
            data: {
                authorities: []
            },
            views: views,
            resolve: resolve
        });

        $stateProvider.state('pages2', {
            parent: 'site',
            url: '/pages/:path1/:path2',
            data: {
                authorities: []
            },
            views: views,
            resolve: resolve
        });

        $stateProvider.state('pages3', {
            parent: 'site',
            url: '/pages/:path1/:path2/:path3',
            data: {
                authorities: []
            },
            views: views,
            resolve: resolve
        });

        $stateProvider.state('pages4', {
            parent: 'site',
            url: '/pages/:path1/:path2/:path3/:path4',
            data: {
                authorities: []
            },
            views: views,
            resolve: resolve
        });

        $stateProvider.state('pages5', {
            parent: 'site',
            url: '/pages/:path1/:path2/:path3/:path4/:path5',
            data: {
                authorities: []
            },
            views: views,
            resolve: resolve
        });
    }]);
})();
