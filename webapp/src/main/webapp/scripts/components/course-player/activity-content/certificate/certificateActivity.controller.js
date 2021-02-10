'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('CertificateActivityController',
        function($scope, $sce) {
            var data = {},

                init = function() {
                    data.content = $scope.activityData.content;
                    data.resourceUrl = getUrl();
                },

                getUrl = function() {
                    return $sce.trustAsResourceUrl("asset-storage/asset-certificate/" + $scope.activityData.id + "/certificate.pdf");
                },

                zend;

            init();

            return {
                data: data,
                getUrl: getUrl
            }
        }
    );
})();
