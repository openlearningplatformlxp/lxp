'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('profile-external-learning', {
            parent: 'profile',
            url: '/profile/external-learning',
            data: {
                authorities: []
            },
            views: {
                'profile_content@profile': {
                    controller: 'ProfileEvidenceController',
                    controllerAs: 'ctrl',

                    templateUrl: 'scripts/apps/main-app/pages/profile/profile-evidence/profile-evidence.html'
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
