'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('breadcrumbs',
        function($state) {
            var makeBreadcrumbs = function() {
                    var breadcrumbs = [];

                    if ($state.current && $state.current.data && $state.current.data.breadcrumbs) {
                        if (angular.isArray($state.current.data.breadcrumbs)) {
                            breadcrumbs = $state.current.data.breadcrumbs;
                        }
                    }

                    return breadcrumbs;
                },

                zend;

            return {
                restrict: 'E',
                templateUrl: 'scripts/components/breadcrumbs/breadcrumbs.html',
                link: function(scope, element, attrs) {

                    scope.breadcrumbs = makeBreadcrumbs();

                    scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
                        scope.breadcrumbs = makeBreadcrumbs();
                    });
                }
            };
        }
    );
})();
