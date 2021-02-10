/**
 * Angular File Upload Button - lays an invisible Angular File Upload button over any content.
 *
 * based on: http://jsfiddle.net/stereosteve/v5Rdc/7/
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('angularFileUploadButton',
        function($compile) {
            return {
                restrict: 'E',
                link: function(scope, element, attributes) {
                    var el = angular.element(element),
                        button = el.children()[0],
                        fileInputHtml = '<input' + (scope.accept ? ' accept="' + scope.accept + '"' : '') + (scope.filters ? ' filters="' + scope.filters + '"' : '') + (scope.required ? ' ng-required="' + scope.required + '"' : '') + ' type="file" uploader="uploader" nv-file-select>',
                        fileInput = angular.element(fileInputHtml);

                    el.css({
                        position: 'relative',
                        overflow: 'hidden',
                        width: button.offsetWidth,
                        height: button.offsetHeight
                    });

                    fileInput.css({
                        position: 'absolute',
                        top: 0,
                        left: 0,
                        'z-index': '2',
                        width: '100%',
                        height: '100%',
                        opacity: '0',
                        cursor: 'pointer'
                    });

                    $compile(fileInput)(scope, function(clonedElement) {
                        el.append(clonedElement);
                    });
                },
                scope: {
                    accept: '@',
                    filters: '@',
                    required: '@',
                    uploader: '='
                }
            }
        }
    );
})();
