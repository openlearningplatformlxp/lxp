/**
 * Full Page Layout.
 */

'use strict';

(function() {
    angular.module('app.components').controller('FullPageController',
        function($rootScope, $state, $timeout, AlertsService, Auth, BUILD, Principal) {
            var data = {},

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

                getState = function() {
                    return $state;
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
                    AlertsService.confirm({
                        buttonOk: {
                            onClick: function() {
                                Auth.logout();

                                // TODO: SAC: the $timeout is a hack to get logout to work properly - fix this.

                                $timeout(function() {
                                    $state.go('home', {}, {
                                        reload: true
                                    });
                                }, 100);
                            }
                        },
                        title: 'Confirm Logout',
                        text: 'Do you want to logout?'
                    });
                },

                zend;

            init();

            return {
                BUILD: BUILD,
                data: data,
                getState: getState,
                isAuthenticated: isAuthenticated,
                logout: logout
            };
        }
    );
})();
