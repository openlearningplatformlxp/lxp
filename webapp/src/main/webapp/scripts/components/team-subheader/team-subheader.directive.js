'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('teamSubheader',
        function($state) {
            var

                zend;

            return {
                restrict: 'E',
                scope: {
                    placeholderMessage: "<",
                    onSearch: '&onSearch',
                    onStartDate: '&onStartDate',
                    onEndDate: '&onEndDate'
                },
                templateUrl: 'scripts/components/team-subheader/team-subheader.html',
                link: function(scope, element, attrs) {},
                controller: function($scope) {
                    $scope.getStartDate = function(startDate) {
                        if ($scope.onStartDate) {
                            $scope.onStartDate({
                                startDate: startDate
                            });
                        }
                    };

                    $scope.getEndDate = function(endDate) {
                        if ($scope.onEndDate) {
                            $scope.onEndDate({
                                endDate: endDate
                            });
                        }
                    };

                    $scope.search = function(searchValue) {
                        if ($scope.onSearch) {
                            $scope.onSearch({
                                searchValue: searchValue
                            });
                        }
                    };
                }
            };
        }
    );
})();
