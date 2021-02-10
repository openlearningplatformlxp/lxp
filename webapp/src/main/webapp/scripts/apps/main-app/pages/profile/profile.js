'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('profile', {
            parent: 'site',
            abstract: true,
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'ProfileController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/profile/profile.html'
                }
            },
            resolve: {
                mainTranslatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                }
            }
        });
    });
})();
