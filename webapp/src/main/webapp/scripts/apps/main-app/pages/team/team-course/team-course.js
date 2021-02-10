'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('team-course', {
            parent: 'team',
            url: '/team/course',
            data: {
                authorities: []
            },
            views: {
                'team_content@team': {
                    controller: 'TeamCourseController',
                    controllerAs: 'ctrl',

                    templateUrl: 'scripts/apps/main-app/pages/team/team-course/team-course.html'
                }
            },
            resolve: {
                mainTranslatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                },

                resolvedProgressionType: function() {
                    return "COURSE";
                }
            }
        });
    });
})();
