'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('LabelActivityController',
        function($scope, $sce) {

            var data = {
                    labelTex: $sce.trustAsResourceUrl($scope.activityData.content.html)
                },

                zend;

            return {
                data: data
            }
        }
    );
})();
