'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('UrlActivityController', function($scope, $state, $window, $location) {

        var data = {},

            resourceUrl = null,

            init = function() {
                data.activity = $scope.activityData;
                data.resourceDescription = data.activity.content.description;
                data.nextActivity = $scope.nextActivityData;

                resourceUrl = formatUrl(data.activity.content.url);
            },
            /*
            init = function() {
                $timeout(function(){
                    var win = window.open(data.url, '_blank');
                    win.focus();
                }, 4000);
            },
            */

            formatUrl = function(url) {
                if (url && !url.startsWith("http://") && !url.startsWith("https://")) {
                    url = $location.protocol() + "://" + url;
                }
                return url;
            },

            launchExternalLink = function() {
                var navUrl = resourceUrl;

                $scope.triggerCompleteOnView();

                if (data.activity.originalType === 'SCORM') {
                    navUrl = $state.href('course-player-external', {
                        courseId: data.activity.courseId,
                        activityId: data.activity.id
                    });
                }

                $window.open(navUrl, '_blank');
            },

            zend;

        init();

        return {
            data: data,
            goToNextActivity: $scope.goToNextActivity,
            launchExternalLink: launchExternalLink
        }
    });
})();
