'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('HeaderExternalController',
        function($rootScope, $scope, $state, $location, $timeout, $stateParams, Auth, Principal) {
            var data = {
                    name: undefined,
                    navbar: {
                        controller: 'NavbarController as ctrl',
                        templateUrl: 'scripts/components/navbar/navbar.html'
                    },
                    notifications: []
                },
                getName = function(account) {
                    if (!account) {
                        return '';
                    }

                    var name = '';

                    if (account.firstName && account.firstName === account.lastName) {
                        name = account.firstName;
                    } else if (account.firstName && account.lastName) {
                        name = account.firstName + ' ' + account.lastName;
                    }

                    return name;
                },

                init = function() {
                    $scope.authenticated = Principal.isAuthenticated();
                    if ($scope.authenticated) {
                        showStuff();
                    }
                    $scope.$on('onLoggedIn', function(event, data) {
                        $scope.authenticated = Principal.isAuthenticated();
                        showStuff()
                    });
                    $scope.$on('onLoggedOut', function(event, data) {
                        $scope.authenticated = Principal.isAuthenticated();
                    });
                },

                isAuthenticated = function() {
                    return Principal.isAuthenticated();
                },

                notYetImplemented = function() {
                    alert("Not yet Implemented");
                },

                showStuff = function() {
                    Principal.identity().then(function(identity) {
                        data.name = getName(identity);
                    });

                    $rootScope.$on('principal.identity.loaded', function(event, identity) {
                        data.name = getName(identity);
                    });
                },

                logout = function() {
                    Auth.logout();

                    // TODO: SAC: the $timeout is a hack to get logout to work properly - fix this.

                    $timeout(function() {
                        $state.go('home', {}, {
                            reload: true
                        });
                    }, 100);
                };

            init();

            return {
                data: data,
                notYetImplemented: notYetImplemented,
                isAuthenticated: isAuthenticated,
                logout: logout
            };
        }
    );

})();
