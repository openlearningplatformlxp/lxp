'use strict';

(function() {
    var module = angular.module('app.components'),

        SORT_TYPES = [{
                name: "Name"
            },
            {
                name: "Course",
                filterType: "COURSE"
            },
            {
                name: "Learning Path",
                filterType: "LEARNING_PATH"
            },
            {
                name: "Progress",
                sortType: "PROGRESS"
            },
            {
                name: "Date",
                sortType: "DATE"
            }
        ];

    module.directive('achievements', [
        function() {

            return {
                restrict: 'E',
                scope: {
                    activityRecords: "=",
                    achievements: "=",
                    clazz: "@",
                    loading: "="
                },
                templateUrl: 'scripts/components/directives/achievements/achievements.html',
                link: function(scope, element, attrs) {},
                controller: function($scope, $state, $location) {

                    $scope.sortTypes = SORT_TYPES;
                    $scope.sortType = SORT_TYPES[3];

                    $scope.selectSortType = function(sortType) {
                        $scope.sortType = sortType;
                        document.getElementById("sortTypeDropdown").focus();
                    };

                    $scope.achievementFilter = function(achievement) {
                        var type = achievement.type ? achievement.type || "LEARNING_PATH" : "LEARNING_PATH";
                        if (!$scope.sortType.filterType || type === $scope.sortType.filterType) {
                            return true;
                        }
                    };

                    $scope.achievementSortBy = function(achievement1, achievement2) {
                        var progress1 = achievement1.value.progress || 0,
                            progress2 = achievement2.value.progress || 0;

                        if ($scope.sortType.sortType === "PROGRESS") {
                            if (progress1 !== progress2) {
                                return progress1 - progress2;
                            } else if (progress1 === 100) {
                                var date1 = moment(achievement1.value.completedDate),
                                    date2 = moment(achievement2.value.completedDate);

                                return date1.isAfter(date2);
                            } else {
                                var date1 = moment(achievement1.value.timeEnrolled),
                                    date2 = moment(achievement2.value.timeEnrolled);

                                return date1.isAfter(date2);
                            }
                        } else if ($scope.sortType.sortType === "DATE") {
                            var date1 = moment(achievement1.value.sortDate),
                                date2 = moment(achievement2.value.sortDate);

                            return date1.isAfter(date2);
                        }

                        return 0;
                    };

                    $scope.goToActivity = function(activity) {
                        if (activity.type === 'LEARNING_PATH') {
                            $state.go("learning-path", {
                                id: activity.id
                            });
                        } else {
                            $state.go("course-totara", {
                                courseId: activity.id,
                                source: "profile"
                            });
                        }
                    };


                    $scope.notYetImplemented = function() {
                        window.alert('Not yet implemented');
                    };
                }
            };
        }
    ]);
})();
