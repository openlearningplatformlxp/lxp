(function() {
    'use strict';

    var module = angular.module('app.components');

    var uniqueCounter = 0;

    module.directive('videoPlayer', [
        function() {
            return {
                restrict: 'A',
                scope: {
                    config: '='
                },
                templateUrl: 'scripts/components/course-player/activity-content/video/video-player/video-player.html'
            };
        }
    ]);
})();