'use strict';

(function() {
    var module = angular.module('app.components'),
        supportedTypeHashmap = {
            "SCORM": true
        };


    module.directive('activityContentExternal',
        function($compile) {
            return {
                restrict: 'E',
                scope: {
                    config: '='
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

                        if (!activity || !activity.content) {
                            div = '<ng-include src="\'scripts/components/course-player-external/activity-content-external/notsupported/notsupported.html\'"></ng-include>';
                            $element.append($compile(div)($scope));
                            return;
                        }

                        $element.empty();

                        type = activity.type;

                        //check if activity supported
                        typeSupported = supportedTypeHashmap[type];

                        if (isLocked) {
                            div = '<ng-include src="\'scripts/components/course-player-external/activity-content-external/locked/locked.html\'"></ng-include>';
                        } else if (!typeSupported) {
                            div = '<ng-include src="\'scripts/components/course-player-external/activity-content-external/notsupported/notsupported.html\'"></ng-include>';
                        } else {
                            type = type.toLowerCase();
                            div = '<ng-include src="\'scripts/components/course-player-external/activity-content-external/' + type + '/' + type + '.html\'"></ng-include>';
                        }
                        $element.append($compile(div)($scope));
                    };

                    //init gets called in $watch above - if this is called init will get called twice

                }
            }
        }
    );
})();
