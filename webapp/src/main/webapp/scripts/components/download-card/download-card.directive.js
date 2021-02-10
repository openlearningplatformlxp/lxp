'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('downloadCard',
        function($state) {
            var

                zend;

            return {
                restrict: 'E',
                scope: {
                    open: "=",
                    card: "=",
                },
                templateUrl: 'scripts/components/download-card/download-card.html',
                link: function(scope, element, attrs) {},
                controller: function($scope) {
                    $scope.notYetImplemented = function() {
                        alert("Not yet Implemented");
                    };
                }
            };
        }
    );
})();
