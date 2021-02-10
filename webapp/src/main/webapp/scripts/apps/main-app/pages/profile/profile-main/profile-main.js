'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('profile-main', {
            parent: 'profile',
            url: '/profile/main',
            data: {
                authorities: []
            },
            views: {
                'profile_content@profile': {
                    controller: 'ProfileMainController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/profile/profile-main/profile-main.html'
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
