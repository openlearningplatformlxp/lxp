'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('cardFake', ['$filter', '$window',
        function($filter, $window) {

            return {
                restrict: 'E',
                scope: {
                    cardView: "=",
                    amount: "=",
                },
                templateUrl: 'scripts/components/card/card-fake.html',
                link: function(scope, element, attrs) {},
                controller: function($scope, $state, $location) {
                    // check if it was defined.  If not - set a default

                }
            };
        }
    ]);
})();
