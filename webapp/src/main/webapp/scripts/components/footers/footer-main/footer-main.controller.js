'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('FooterMainController',
        function($rootScope, $scope, $state, $timeout, Auth, Principal) {
            var data = {
                    year: moment().format('YYYY'),
                    name: undefined,
                    helpUrl: appGlobal.config.get('app.config.supporturl')
                },

                init = function() {
                    $scope.authenticated = Principal.isAuthenticated();
                    $scope.$on('onLoggedIn', function(event, data) {
                        $scope.authenticated = Principal.isAuthenticated();
                    });
                    $scope.$on('onLoggedOut', function(event, data) {
                        $scope.authenticated = Principal.isAuthenticated();
                    });
                },

                notYetImplemented = function() {
                    alert("Not yet Implemented");
                };

            init();

            return {
                data: data,
                notYetImplemented: notYetImplemented
            };
        }
    );
})();
