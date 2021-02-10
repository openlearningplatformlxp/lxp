'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('LockedActivityController',
        function($scope) {

            var data = {

                },

                init = function() {
                    data.content = $scope.activityData;
                    data.requiredAction = (data.content.activityStatus) ? data.content.activityStatus.requiredAction : null;
                };

            init();

            return {
                data: data
            };
        }
    );
})();
