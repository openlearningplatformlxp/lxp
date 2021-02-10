'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('overviewCard',
        function() {

            return {
                restrict: 'E',
                scope: {
                    item: "=",
                    account: "=",
                    returnPath: "<",
                    showResume: "<",
                    onResumeClick: '&onResumeClick',
                    showEnroll: "<",
                    onEnrollClick: '&onEnrollClick',
                    onDropClick: '&onDropClick',
                },
                templateUrl: 'scripts/components/overview-card/overview-card.html',
                link: function(scope, element, attrs) {},
                controller: function($scope) {

                    $scope.resumeClicked = function() {
                        if ($scope.onResumeClick) {
                            $scope.onResumeClick();
                        }
                    };

                    $scope.enrollClicked = function() {
                        if ($scope.onEnrollClick) {
                            $scope.onEnrollClick();
                        }
                    };

                    $scope.dropClicked = function() {
                        if ($scope.onDropClick) {
                            $scope.onDropClick().then(function(data) {
                            });
                        }
                    };

                }
            };
        }
    );
})();
