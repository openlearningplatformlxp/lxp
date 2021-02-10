'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('teamManagerList',
        function($state) {
            var

                zend;

            return {
                restrict: 'E',
                scope: {
                    managers: "=",
                    onManagerClick: '&onManagerClick'
                },
                templateUrl: 'scripts/components/team-manager-list/team-manager-list.html',
                link: function(scope, element, attrs) {},
                controller: function($scope) {

                    $scope.showManagers = true;

                    $scope.goToManagerView = function(manager) {
                        if ($scope.onManagerClick) {
                            $scope.onManagerClick({
                                manager: manager
                            });
                        }
                    };
                }
            };
        }
    );
})();
