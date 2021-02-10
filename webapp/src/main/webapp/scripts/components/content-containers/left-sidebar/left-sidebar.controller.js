/**
 * Portal Left-Sidebar Controller.
 */

'use strict';

(function() {
    var CLOSE_SIDEBAR_ON_MENU_CLICK_FOR_NARROW_DEVICE = true;

    angular.module('app.components').controller('LeftSidebarController',
        function($rootScope, $state, $timeout, AlertsService, Auth, BUILD, Principal) {
            var data = {
                    name: undefined,
                    toggled: false
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

                    $rootScope.$on('left-sidebar.visibility', function(event, show) {
                        var toggle = !angular.isDefined(show) || (angular.isDefined(show) && ((!!data.toggled) != (!!show)));

                        if (toggle) {
                            toggleSidebar();
                        }
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

                onMenuClick = function() {
                    if (CLOSE_SIDEBAR_ON_MENU_CLICK_FOR_NARROW_DEVICE) {
                        if (data.toggled) {
                            toggleSidebar();
                        }
                    }
                },

                /**
                 * Broadcasts two events: "left-sidebar.toggle.started" and "left-sidebar.toggle.finished".
                 * These are useful in certain circumstances to help with rendering issues. For examples,
                 * Google charts do not resize during page resizes, and these events can be used to hide
                 * the Chart during sidebar animation.
                 */
                toggleSidebar = function() {
                    $rootScope.$broadcast('left-sidebar.toggle.started');

                    $timeout(function() {
                        data.toggled = !data.toggled;

                        $timeout(function() {
                            $rootScope.$emit('resizeMsg'); // make angular re-render page

                            $timeout(function() {
                                $rootScope.$broadcast('left-sidebar.toggle.finished');
                            }, 100);
                        }, 550);
                    }, 1);
                },

                zend;

            init();

            return {
                BUILD: BUILD,
                data: data,
                getState: getState,
                isAuthenticated: isAuthenticated,
                logout: logout,
                onMenuClick: onMenuClick
            };
        }
    );
})();
