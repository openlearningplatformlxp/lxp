/**
 * ngInclude substitute which also optionally takes a controller and resolve object.
 *
 * NOTE: The resolves do not wait for promise resolution before rendering.
 * ***** Example:
 *
 *   *** In a controller:
 *
 *     ...
 *
 *     getIncludeController = function() {
 *         return 'MyTest as ctrl';
 *     },
 *
 *     getIncludeResolve = function() {
 *         return {
 *             resolvedUserName: function() {
 *                 return 'Scott';
 *             }
 *         }
 *     },
 *
 *     getIncludeTemplateUrl = function() {
 *
 *     }
 *
 *     ...
 *
 *   *** In a template:
 *
 *     <div include-controller="ctrl.getIncludeController()" include-resolve="ctrl.getIncludeResolve()" include-template-url="ctrl.getIncludeTemplateUrl()"></div>
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('includeTemplateUrl',
        function() {
            return {
                controller: function($controller, $scope) {
                    if ($scope.controller) {
                        var resolve = $scope.resolve || {};

                        angular.forEach(resolve, function(value, key) {
                            if (angular.isFunction(value)) {
                                resolve[key] = value();
                            }
                        });

                        $controller($scope.controller, angular.extend({}, {
                            $scope: $scope
                        }, resolve));
                    }
                },
                restrict: 'A',
                scope: {
                    controller: '=includeController',
                    resolve: '=includeResolve',
                    templateUrl: '=includeTemplateUrl'
                },
                template: '<div ng-include="templateUrl"></div>'
            };
        }
    );

    /*
     * Initial implementation using different approach... but does not have resolve implemented.
     *
    module.directive('includeController',
        function($compile) {
            return {
                link: function(scope, element) {
                    var includeHtml = '<div ' + (scope.controller ? 'ng-controller="' + scope.controller : '' ) + '" ng-include="\'' + scope.template + '\'"></div>',
                        includeElement = angular.element(includeHtml);

                    $compile(includeElement)(scope, function(clonedElement) {
                        element.append(clonedElement);
                    });
                },
                restrict: 'A',
                scope: {
                    controller: '=includeController',
                    template: '=includeTemplateUrl'
                }
            };
        }
    );
     */
})();
