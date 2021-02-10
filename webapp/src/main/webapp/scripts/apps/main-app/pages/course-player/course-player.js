'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('course-player', {
            parent: 'site',
            url: '/course-player/{courseId:int}?{step}&{returnPath}',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'CoursePlayerController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/course-player/course-player.html'
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

                resolvedCoursePlayerData: function($stateParams, CoursePlayerService) {
                    var courseId = $stateParams.courseId || 0;
                    return CoursePlayerService.getCoursePlayerData({
                        courseId: courseId
                    });
                }
            }
        });
    });
})();
