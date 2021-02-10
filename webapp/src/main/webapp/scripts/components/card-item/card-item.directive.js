'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('cardItem',
        function($filter, $window) {

            return {
                restrict: 'E',
                scope: {
                    item: "=",
                    type: "=?" // white/dark - Default white.
                },
                templateUrl: 'scripts/components/card-item/card-item.html',
                link: function(scope, element, attrs) {},
                controller: function($scope, $state, $location) {
                    // check if it was defined.  If not - set a default
                    //Unused as of 12/16/19 $scope.type = angular.isDefined($scope.type) ? $scope.type : 'white';
                    if (typeof($scope.item) == "undefined") {
                        return;
                    }
                    $scope.item.iconCls = 'svg';
                    $scope.item.iconUrl = '/assets/images/box.svg';
                    $scope.item.imgType = 'box';

                    var item = $scope.item;

                    if (item.type == 'CLASSROOM' && item.city.toLowerCase() != 'virtual') {
                        $scope.item.imgType = 'ilt';
                    } else if (item.type == 'CLASSROOM' && item.city.toLowerCase() == 'virtual') {
                        $scope.item.imgType = 'vilt';
                    } else if (item.type == 'LEARNING_PATH' || item.type == 'PROGRAM_LINK') {
                        $scope.item.imgType = 'learningpath';
                    } else if ((item.type == 'COURSE' || item.type == 'SINGLE_ACTIVITY_COURSE') && (item.cms != 'LYNDA' && item.cms != 'KALTURA' && item.cms != 'ALLEGO')) {
                        $scope.item.imgType = 'course';
                    } else if (item.type == 'WIKIPAGE') {
                        $scope.item.imgType = 'wikipage';
                    } else if (item.type == 'WEB_TASK') {
                        $scope.item.imgType = 'webtask';;
                    } else if (item.type == 'OFFLINE_TASK') {
                        $scope.item.imgType = 'offlinetask';
                    } else if (item.cms == 'LYNDA') {
                        $scope.item.imgType = 'lynda';
                    } else if (item.cms == 'KALTURA') {
                        $scope.item.imgType = 'kaltura';
                    } else if (item.cms == 'ALLEGO') {
                        $scope.item.imgType = 'allego';
                    }
                }
            };
        }
    );
})();
