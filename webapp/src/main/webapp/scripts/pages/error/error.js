'use strict';

(function() {
    var module = angular.module('app.pages');

    module.config(function($stateProvider) {
        $stateProvider.state('error', {
            parent: 'site',
            url: '/error',
            data: {
                pageTitle: 'error.title',
                authorities: []
            },
            views: {
                'content@site': {
                    templateUrl: 'scripts/pages/error/error.html'
                }
            },
            resolve: {
                mainTranslatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('error');
                    return $translate.refresh();
                }
            }
        });

        $stateProvider.state('accessdenied', {
            parent: 'site',
            url: '/accessdenied',
            data: {
                pageTitle: 'Access Denied',
                authorities: []
            },
            views: {
                'content@site': {
                    templateUrl: 'scripts/pages/error/accessdenied.html'
                }
            },
            resolve: {
                mainTranslatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('error');
                    return $translate.refresh();
                }
            }
        });
    });
})();