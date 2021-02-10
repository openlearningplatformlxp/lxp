'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('team-individual', {
            parent: 'team',
            url: '/team/individual',
            data: {
                authorities: []
            },
            views: {
                'team_content@team': {
                    controller: 'TeamIndividualController',
                    controllerAs: 'ctrl',

                    templateUrl: 'scripts/apps/main-app/pages/team/team-individual/team-individual.html'
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
