'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('upcomingClassCard',
        function($state) {
            var

                zend;

            return {
                restrict: 'E',
                scope: {
                    item: "=",
                },
                templateUrl: 'scripts/components/upcoming-class-card/upcoming-class-card.html',
                link: function(scope, element, attrs) {},
                controller: function($scope, CourseEnrollmentService, AlertsService) {

                    var onApprovalRequired = function(response) {
                        $scope.item.status = 'REQUESTED_APPROVAL';
                        CourseEnrollmentService.requestApproval({
                            sessionId: $scope.item.sessionId
                        }, function(response) {
                        })

                    };

                    $scope.requireApproval = function() {

                        AlertsService.confirmCancel({
                            title: 'Request Approval from Manager?',
                            text: ' ',
                            buttonOk: {
                                onClick: onApprovalRequired,
                                class: 'primary'
                            }
                        });
                    };

                    $scope.signUp = function() {
                        $scope.item.status = 'ENROLLED';
                        CourseEnrollmentService.enrollInCourseSession({
                            courseId: $scope.item.courseId,
                            sessionId: $scope.item.sessionId
                        }, function(response) {
                        });
                    };

                }
            };
        }
    );

})();
