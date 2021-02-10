'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('teamCard',
        function($state) {
            var

                zend;

            return {
                restrict: 'E',
                scope: {
                    team: "=",
                    onProgramClick: '&onProgramClick'
                },
                templateUrl: 'scripts/components/team-card/team-card.html',
                link: function(scope, element, attrs) {},
                controller: function($scope) {

                    $scope.goToProgramView = function() {
                        if ($scope.team.program.type && $scope.team.program.type === 'COURSE') {
                            return;
                        }

                        if ($scope.onProgramClick) {
                            $scope.onProgramClick({
                                program: $scope.team.program
                            });
                        }
                    };
                }
            };
        }
    );
})();
