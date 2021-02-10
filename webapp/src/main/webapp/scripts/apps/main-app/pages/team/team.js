'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('team', {
            parent: 'site',
            abstract: true,
            url: '/team',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'TeamController',
                    controllerAs: 'ctrl',

                    templateUrl: 'scripts/apps/main-app/pages/team/team.html'
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
