'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('progressCard',
        function($state) {
            var

                zend;

            return {
                restrict: 'E',
                scope: {
                    open: "=",
                },
                templateUrl: 'scripts/components/progress-card/progress-card.html',
                link: function(scope, element, attrs) {},
                controller: function($scope) {
                    $scope.hideContent = function() {
                        $scope.open = false;
                    };

                    $scope.showContent = function() {
                        $scope.open = true;
                    };
                }
            };
        }
    );
})();
