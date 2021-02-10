'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('team-personal-plans', {
            parent: 'team',
            url: '/team/personal-plans',
            data: {
                authorities: []
            },
            views: {
                'team_content@team': {
                    controller: 'TeamPersonalPlansController',
                    controllerAs: 'ctrl',

                    templateUrl: 'scripts/apps/main-app/pages/team/team-personal-plans/team-personal-plans.html'
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
