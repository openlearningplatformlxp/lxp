'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('cardViewSwitcher',
        function($filter, $window) {

            return {
                restrict: 'E',
                templateUrl: 'scripts/components/card-view-switcher/card-view-switcher.html',
                link: function(scope, element, attrs) {},
                controller: function($rootScope, $scope, CardService) {
                    // check if it was defined.  If not - set a default
                    $scope.showGridView = function() {
                        $scope.cardView = CardService.showGridView();
                        $rootScope.$broadcast('onUpdatedCardView', $scope.cardView);
                    };

                    $scope.showListView = function() {
                        $scope.cardView = CardService.showListView();
                        $rootScope.$broadcast('onUpdatedCardView', $scope.cardView);
                    };
                }
            };
        }
    );
})();
