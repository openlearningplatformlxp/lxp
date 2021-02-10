'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive("datepicker", function() {
        return {
            restrict: "A",
            require: "ngModel",
            link: function(scope, elem, attrs, ngModelCtrl) {
                var updateModel = function(dateText) {
                    scope.$apply(function() {
                        ngModelCtrl.$setViewValue(dateText);
                    });
                };
                var options = {
                    dateFormat: "mm/dd/yy",
                    onSelect: function(dateText) {
                        updateModel(dateText);
                    }
                };
                elem.datepicker(options);
            }
        }
    });
})();
