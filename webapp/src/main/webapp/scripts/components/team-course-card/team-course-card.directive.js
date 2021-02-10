'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('teamCourseCard',
        function($state) {
            var

                zend;

            return {
                restrict: 'E',
                scope: {
                    progressionOverview: "=",
                    open: "=",
                    shared: "=",
                    type: "=",
                    onMemberClick: '&onMemberClick'
                },
                templateUrl: 'scripts/components/team-course-card/team-course-card.html',
                link: function(scope, element, attrs) {},
                controller: function($scope) {

                    $scope.showManagers = true;

                    $scope.goToMemberView = function(member, program) {
                        if (program.type === 'COURSE') {
                            return;
                        }

                        if ($scope.onMemberClick) {
                            $scope.onMemberClick({
                                member: member,
                                program: program
                            });
                        }
                    };
                }
            };
        }
    );
})();
