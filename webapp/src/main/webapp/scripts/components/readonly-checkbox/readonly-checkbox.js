'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('readonlyCheckbox',
        function() {
            return {
                bindToController: {
                    readonlyFn: '&',
                },
                controller: 'ReadonlyCheckboxController',
                controllerAs: 'ctrl',
                restrict: 'E',
                scope: {},
                templateUrl: 'scripts/components/readonly-checkbox/readonly-checkbox.html',
                transclude: true
            }
        }
    );

    module.controller('ReadonlyCheckboxController',
        function() {
            var
                readonlyFn = this.readonlyFn,
                init = function() {};

            return {
                readonlyFn: readonlyFn
            };
        }
    );
})();
