'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('PdfActivityController',
        function($scope, $sce, $location) {

            var data = {

                },

                init = function() {

                    $scope.triggerCompleteOnView();

                    data.resourceUrl = $sce.trustAsResourceUrl($location.protocol() + "://docs.google.com/gview?url=" + $scope.activityData.content.url + "&embedded=true");
                },

                zend;

            init();

            return {
                data: data
            };
        }
    );
})();
