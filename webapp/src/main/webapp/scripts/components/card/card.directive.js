'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('card',
        function($filter, $window) {

            return {
                restrict: 'E',
                scope: {
                    item: "=",
                    type: "=?", // white/dark - Default white.
                    source: "<",
                    returnPath: "<",
                    showResume: "<",
                    onResumeClick: '&onResumeClick',
                    showEnroll: "<",
                    onEnrollClick: '&onEnrollClick',
                    showOpenInLms: "<",
                    selections: "=", // enabled on admin, override link to allow selections
                    onSelected: '&onSelected',
                },
                templateUrl: 'scripts/components/card/card.html',
                link: function(scope, element, attrs) {},
                controller: function($sce, $scope, $state, $location) {
                    // check if it was defined.  If not - set a default
                    $scope.type = angular.isDefined($scope.type) ? $scope.type : 'white';
                    // $scope.subType = angular.isDefined($scope.subType) ? $scope.subType : '';

                    if ($scope.item != null && $scope.item.type === 'CLASSROOM') {
                        $scope.type = 'classroom';
                    }

                    $scope.showOpenInLmsBtn = $scope.showOpenInLms && $scope.type !== 'cover' && $scope.type !== 'overview';
                    $scope.showOpenInLmsBtn = $scope.showOpenInLms && $scope.item.type !== 'CLASSROOM';

                    if ($scope.item != null) {
                        if ($scope.type != 'cover' && $scope.type != 'hero' && $scope.type != 'overview') {
                            var titleMax = 150;
                            var descriptionMax = 280;

                            $scope.item.title = $filter('limitTo')($scope.item.title, titleMax) + ($scope.item.title.length > titleMax ? '&hellip;' : '');
                            if ($scope.item.description === null) {
                                $scope.item.description = '';
                            }
                            //
                            var shorten = $filter('limitTo')($scope.item.description, descriptionMax) + ($scope.item.description.length > descriptionMax ? ' &hellip;' : '');
                            try {
                                $sce.getTrustedHtml(shorten);
                                $scope.item.description = shorten;
                            } catch (e) {
                                descriptionMax += 20;
                                shorten = $filter('limitTo')($scope.item.description, descriptionMax) + ($scope.item.description.length > descriptionMax ? ' &hellip;' : '');
                                $scope.item.description = shorten;
                            }

                        }
                    }

                    $scope.goBack = function() {
                        if ($scope.returnPath) {
                            $location.url($scope.returnPath);
                            return;
                        }
                        window.history.go(-1);
                    };

                    $scope.goToCard = function() {

                        if ($scope.selections) {
                            $scope.item.selected = !$scope.item.selected;
                            if ($scope.onSelected) {
                                $scope.onSelected($scope.item);
                            }
                        } else if ($scope.item.type == 'WIKIPAGE') {
                            $window.open('/#/pages' + $scope.item.externalUrl, '_self');
                        } else if ($scope.item.cms && ($scope.item.cms === 'ALLEGO' || $scope.item.cms === 'LYNDA')) {
                            $window.open($scope.item.externalUrl, '_blank');
                        } else if ($scope.type != 'cover' && $scope.type != 'overview') {
                            if ($scope.item.type == 'LEARNING_PATH') {

                                $state.go("learning-path", {
                                    id: $scope.item.id,
                                    personal: $scope.item.personal,
                                    returnPath: $scope.returnPath
                                });
                            } else if ($scope.item.type == 'CLASSROOM') {
                                $state.go("course-totara", {
                                    courseId: $scope.item.id,
                                    source: $scope.source,
                                    externalUrl: $scope.item.externalUrl
                                });
                            } else if (($scope.item.cms === 'KALTURA')) {
                                $state.go("course-totara", {
                                    courseId: 0,
                                    source: $scope.source,
                                    externalUrl: $scope.item.externalUrl
                                });
                            } else {
                                if ($scope.item.discoveryProgramType && $scope.item.discoveryProgramType == 'TEXT') {
                                    return;
                                }
                                $state.go("course-totara", {
                                    courseId: $scope.item.id,
                                    source: $scope.source,
                                });
                            }
                        }

                    };

                    $scope.isDueDate = function(item) {
                            return item.dueDate != null && moment(item.dueDate, 'MM/DD/YYYY').isBefore(moment());
                        },

                        $scope.goToCardInLms = function($event) {
                            if ($event) {
                                $event.stopPropagation();
                            }
                            if ($scope.type != 'cover' && $scope.type != 'overview' && $scope.item.type !== 'LEARNING_PATH') {
                                $state.go("course-overview", {
                                    courseId: $scope.item.id,
                                    returnPath: $scope.returnPath
                                });
                            }
                        };

                    $scope.notYetImplemented = function($event) {
                        if ($event) {
                            $event.stopPropagation();
                        }
                        alert("Not yet Implemented yet");
                    };

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
                }
            };
        }
    );
})();
