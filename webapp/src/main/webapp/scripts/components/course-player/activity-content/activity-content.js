'use strict';

(function() {
    var module = angular.module('app.components'),
        supportedTypeHashmap = {
            "RESOURCE": true,
            "PAGE": true,
            "SCORM": true,
            "URL": true,
            "VIDEO": true,
            "QUIZ": true,
            "FEEDBACK": true,
            "LABEL": true,
            "CERTIFICATE": true,
            "CHOICE": true
        };


    module.directive('activityContent',
        function($compile, CoursePlayerService) {
            return {
                restrict: 'E',
                scope: {
                    config: '=',
                    onNextActivityClick: '&onNextActivityClick'
                },
                replace: false,
                link: function($scope, $element, $attrs) {

                    $scope.$watch('config', function(newValue, oldValue, scope) {
                        init();
                    });

                    var init = function() {
                        var activity = $scope.config.activityData,
                            isLocked = activity.activityStatus ? activity.activityStatus.isLocked : false,
                            type,
                            typeSupported,
                            div;

                        // TODO: (WJK) Clean this controller a bit

                        $scope.activityData = $scope.config.activityData;
                        $scope.nextActivityData = $scope.config.nextActivityData;

                        $scope.goToNextActivity = function() {
                            if ($scope.onNextActivityClick) {
                                $scope.onNextActivityClick();
                            }
                        };

                        $scope.triggerCompleteOnView = function() {
                            if (activity.shouldCompleteOnView && (!activity.activityStatus || activity.activityStatus.status != 1)) {
                                CoursePlayerService.markActivityComplete({
                                    courseId: activity.courseId,
                                    activityId: activity.id
                                }).then(function success(response) {
                                    // No-op
                                }, function(error) {
                                    console.log(error);
                                });
                            }
                        };

                        if (!activity || !activity.content) {
                            div = '<ng-include src="\'scripts/components/course-player/activity-content/notsupported/notsupported.html\'"></ng-include>';
                            $element.append($compile(div)($scope));
                            return;
                        }

                        $element.empty();

                        if (activity.type == 'activityreport') {
                            activity.type = 'activity-report';
                        }
                        if (activity.content.shouldDisplayInNewWindow) {
                            activity.originalType = activity.type;
                            activity.type = 'URL';
                        }

                        type = activity.type;

                        //check if activity supported
                        typeSupported = supportedTypeHashmap[type];

                        if (isLocked) {
                            div = '<ng-include src="\'scripts/components/course-player/activity-content/locked/locked.html\'"></ng-include>';
                        } else if (!typeSupported) {
                            div = '<ng-include src="\'scripts/components/course-player/activity-content/notsupported/notsupported.html\'"></ng-include>';
                            setTimeout(function() {
                                window.open(activity.externalURL, "_blank");
                            }, 3000);
                        } else {
                            type = type.toLowerCase();
                            div = '<ng-include src="\'scripts/components/course-player/activity-content/' + type + '/' + type + '.html\'"></ng-include>';
                        }
                        $element.append($compile(div)($scope));
                    };
                }
            }
        }
    );
})();
