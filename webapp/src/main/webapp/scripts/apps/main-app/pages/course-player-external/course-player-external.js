'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('course-player-external', {
            parent: 'site',
            url: '/course-player-external/{courseId:int}/{activityId:int}',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'CoursePlayerExternalController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/course-player-external/course-player-external.html'
                },
                'header@site': {
                    templateUrl: 'scripts/components/headers/header-external/header-external.html',
                    controller: 'HeaderExternalController',
                    controllerAs: 'ctrl'
                },
                'footer@site': {
                    template: '<div></div>'
                }
            },
            resolve: {
                mainTranslatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                },

                resolvedCourseId: function($stateParams) {
                    return $stateParams.courseId || 0;
                },

                resolvedActivityId: function($stateParams) {
                    return $stateParams.activityId || 0;
                }
            }
        });
    });
})();
