'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('activityManualCompletion',
        function() {
            return {
                bindToController: {
                    activity: '='
                },
                controller: 'ActivityManualCompletionController',
                controllerAs: 'ctrl',
                restrict: 'E',
                scope: {},
                templateUrl: 'scripts/components/course-player/activity-content/activity-manual-completion/activity-manual-completion.html'
            }
        }
    );

    module.controller('ActivityManualCompletionController',
        function(CoursePlayerService) {
            var data = {},

                activity = this.activity,

                init = function() {
                    data.activityCompleted = activity.activityStatus ? activity.activityStatus.status === 1 : false;
                    data.showControls = activity.allowsManualCompletion;
                },

                toggleCompletion = function() {
                    // NOTE: (WJK) Monitoring the local state which the switch will toggle before getting here
                    if (data.activityCompleted) {
                        CoursePlayerService.markActivityComplete({
                            courseId: activity.courseId,
                            activityId: activity.id
                        }).then(function success(response) {
                            data.activityCompleted = true;
                        }, function(error) {
                            console.log(error);
                        });
                    } else {
                        CoursePlayerService.markActivityIncomplete({
                            courseId: activity.courseId,
                            activityId: activity.id
                        }).then(function success(response) {
                            data.activityCompleted = false;
                        }, function(error) {
                            console.log(error);
                        });
                    }
                },

                zend;

            init();

            return {
                data: data,
                toggleCompletion: toggleCompletion
            }
        }
    );
})();
