'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {

        $stateProvider.state('learning-path-create', {
            parent: 'site',
            url: '/catalog/learning-path/create',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'LearningPathController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/catalog/learning-path/learning-path.html'
                }
            },
            resolve: {
                mainTranslatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                },

                resolvedProgramOverview: function($stateParams) {
                    return {
                        create: true,
                        totalNumOfCourses: 0,
                        courseSets: [{
                            name: "Section 1",
                            courses: []
                        }],
                        program: {
                            title: 'My Personal Learning Path',
                            type: {
                                name: "LEARNING_PATH"
                            }
                        }
                    };
                }
            }
        });

        $stateProvider.state('learning-path', {
            parent: 'site',
            url: '/catalog/learning-path/:id?{returnPath}&{personal}',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'LearningPathController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/main-app/pages/catalog/learning-path/learning-path.html'
                }
            },
            resolve: {
                mainTranslatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                },
                resolvedAccountInfo: function(Principal) {
                    return Principal.identity().then(function(account) {
                        return {
                            account: account,
                            isAuthenticated: Principal.isAuthenticated()
                        };
                    });
                },


                resolvedProgramOverview: function($stateParams, CourseService) {
                    var programId = $stateParams.id || 0;
                    var personal = $stateParams.personal || false;
                    return CourseService.getProgramOverviewData({
                        programId: programId,
                        personal: personal
                    });
                }
            }
        });
    });
})();
