'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('NavbarController',
        function($rootScope, $state, $timeout, Auth, BUILD, Principal) {
            var getState = function() {
                    return $state;
                },

                isAuthenticated = function() {
                    return Principal.isAuthenticated();
                },

                logout = function() {
                    Auth.logout();

                    // TODO: SAC: the $timeout is a hack to get logout to work properly - fix this.

                    $timeout(function() {
                        $state.go('home', {}, {
                            reload: true
                        });
                    }, 100);
                },

                toggle = function(name, show) {
                    $rootScope.$broadcast(name, show);
                },

                zend;

            return {
                BUILD: BUILD,
                getState: getState,
                isAuthenticated: isAuthenticated,
                logout: logout,
                toggle: toggle
            };
        }
    );
})();