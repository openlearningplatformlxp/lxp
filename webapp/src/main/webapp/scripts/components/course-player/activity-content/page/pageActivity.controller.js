'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('PageActivityController',
        function($scope) {

            var data = {
                    activity: {},
                    type: null
                },

                init = function() {

                    $scope.triggerCompleteOnView();

                    data.activity = $scope.activityData;
                    data.resourceDescription = data.activity.content.description;
                    data.resourceContent = data.activity.content.html;
                    data.nextActivity = $scope.nextActivityData;
                },

                zend;

            init();

            return {
                data: data,
                goToNextActivity: $scope.goToNextActivity
            };
        }
    );
})();