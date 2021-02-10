'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('messageCard',
        function($state) {
            var

                zend;

            return {
                restrict: 'E',
                scope: {
                    message: "<"
                },
                templateUrl: 'scripts/components/message-card/message-card.html',
                link: function(scope, element, attrs) {},
                controller: function($scope) {}
            };
        }
    );
})();
