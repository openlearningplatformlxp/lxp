'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('NotificationsController',
        function($state, $sce, $location, $http, $stateParams) {
            var data = {
                link: $sce.trustAsResourceUrl($stateParams.link)
            };

            return {
                data: data
            };
        }
    );

})();
