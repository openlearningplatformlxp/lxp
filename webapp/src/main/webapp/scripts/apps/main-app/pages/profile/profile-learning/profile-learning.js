'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('profile-learning', {
            parent: 'profile',
            url: '/profile/learning',
            data: {
                authorities: []
            },
            views: {
                'profile_content@profile': {
                    controller: 'ProfileLearningController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/profile/profile-learning/profile-learning.html'
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
