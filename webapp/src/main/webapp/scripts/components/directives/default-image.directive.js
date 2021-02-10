/**
 * From: https://stackoverflow.com/questions/27549134/angularjs-ng-src-condition-if-not-found-via-url
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('onErrorSrc', function() {
        return {
            link: function(scope, element, attrs) {
                element.bind('error', function() {
                    $(element).hide();
                    $("#" + attrs.onErrorSrc).show();
                });
            }
        }
    });
})();
