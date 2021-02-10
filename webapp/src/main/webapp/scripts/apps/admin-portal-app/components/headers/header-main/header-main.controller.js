'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('AdminPortalHeaderMainController',
        function($rootScope, $state, $timeout, Auth, Principal) {
            var data = {
                    name: undefined,
                    navbar: {
                        controller: 'NavbarController as ctrl',
                        templateUrl: 'scripts/apps/admin-portal-app/components/navbar/navbar.html'
                    }
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
                    Principal.identity().then(function(identity) {
                        data.name = getName(identity);
                    });

                    $rootScope.$on('principal.identity.loaded', function(event, identity) {
                        data.name = getName(identity);
                    });
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

                zend;

            init();

            return {
                data: data,
                isAuthenticated: isAuthenticated,
                logout: logout
            };
        }
    );
})();
