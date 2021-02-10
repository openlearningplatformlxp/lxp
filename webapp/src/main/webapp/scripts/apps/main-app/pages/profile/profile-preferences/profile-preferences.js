'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('profile-preferences', {
            parent: 'profile',
            url: '/profile/preferences',
            data: {
                authorities: []
            },
            views: {
                'profile_content@profile': {
                    controller: 'ProfilePreferencesController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/profile/profile-preferences/profile-preferences.html'
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
