'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('teamIndividualCard',
        function($state) {
            var

                zend;

            return {
                restrict: 'E',
                scope: {
                    teamMember: "=",
                    open: "=",
                    onManagerClick: '&onManagerClick',
                    onMemberClick: '&onMemberClick'
                },
                templateUrl: 'scripts/components/team-individual-card/team-individual-card.html',
                link: function(scope, element, attrs) {},
                controller: function($scope) {
                    $scope.hideContent = function() {
                        $scope.open = false;
                    };

                    $scope.showContent = function() {
                        $scope.open = true;
                    };

                    $scope.goToManagerView = function($event) {
                        $event.stopPropagation();

                        if ($scope.onManagerClick) {
                            $scope.onManagerClick({
                                manager: $scope.teamMember
                            });
                        }
                    };

                    $scope.goToMemberView = function(program) {
                        if ($scope.onMemberClick) {
                            $scope.onMemberClick({
                                member: $scope.teamMember,
                                program: program
                            });
                        }
                    };
                }
            };
        }
    );
})();
