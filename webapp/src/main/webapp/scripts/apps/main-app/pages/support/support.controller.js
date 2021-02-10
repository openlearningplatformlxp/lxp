'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('SupportController',
        function() {


            var getNumArray = function(num) {
                return new Array(num);
            };

            var notYetImplemented = function() {
                alert("Not yet Implemented");
            };


            return {
                getNumArray: getNumArray,
                notYetImplemented: notYetImplemented
            };
        }
    );
})();
