'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('progressionDetails',
        function($state) {
            var

                zend;

            return {
                restrict: 'E',
                scope: {
                    progressionOverview: "="
                },
                templateUrl: 'scripts/components/progression-details/progression-details.html',
                link: function(scope, element, attrs) {},
                controller: function($scope) {

                    $scope.getProgressEventStatusClass = function(eventStatus) {
                        var iconClass = "fa-times-circle status-icon-notstarted";

                        if (eventStatus === "IN_PROGRESS") {
                            iconClass = "fa-play-circle status-icon-inprogress";
                        } else if (eventStatus === "COMPLETE") {
                            iconClass = "fa-check-circle status-icon-completed";
                        }

                        return iconClass;
                    };
                }
            };
        }
    );
})();
