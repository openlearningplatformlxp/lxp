'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('searchBox',
        function($state) {
            var

                zend;

            return {
                restrict: 'E',
                scope: {
                    placeholderMessage: "<",
                    searchValue: "="
                },
                templateUrl: 'scripts/components/search-box/search-box.html',
                link: function(scope, element, attrs) {},
                controller: function($scope) {}
            };
        }
    );
})();
