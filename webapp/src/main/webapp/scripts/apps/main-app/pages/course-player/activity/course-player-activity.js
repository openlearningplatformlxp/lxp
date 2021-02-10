'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('course-player-activity', {
            parent: 'course-player',
            url: '/activity/{sectionId:int}/{activityId:int}',
            data: {
                authorities: []
            },
            views: {
                'activity@course-player': {
                    controller: 'CoursePlayerActivityController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/course-player/activity/course-player-activity.html'
                }
            },
            resolve: {
                mainTranslatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                },

                resolvedSectionId: function($stateParams) {
                    return $stateParams.sectionId || 0;
                },

                resolvedActivityId: function($stateParams) {
                    return $stateParams.activityId || 0;
                }
            }
        });
    });
})();
