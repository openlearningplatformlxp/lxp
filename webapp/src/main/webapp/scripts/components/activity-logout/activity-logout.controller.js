/**
 * Controller for Activity (Auto) Logout service.
 *
 * Service to present a "Session Expiration Soon" dialog and auto-logout the session if user does not
 * choose to extend the session.
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('ActivityLogoutController',
        function(resolvedOptions, $state, $timeout, $translate, ActivityMonitor, AlertsServiceDelegate, Auth, Principal) {
            var data = {
                    seconds: resolvedOptions.secsBeforeLogout,
                    secondsRemaining: resolvedOptions.secsBeforeLogout
                },

                countdownTimeoutHandle = undefined,

                cancelCountdown = function() {
                    if (countdownTimeoutHandle) {
                        $timeout.cancel(countdownTimeoutHandle);
                        countdownTimeoutHandle = undefined;
                    }
                },

                countdown = function() {
                    cancelCountdown();

                    if (data.secondsRemaining > 0) {
                        countdownTimeoutHandle = $timeout(function() {
                            if (ActivityMonitor.getSecondsIdle() > 5) {
                                data.secondsRemaining--;
                            }
                            countdown();
                        }, 1000);
                    } else {
                        logout();

                        AlertsServiceDelegate.close();
                    }
                },

                getBarStyles = function() {
                    return {
                        width: Math.trunc((data.secondsRemaining / data.seconds) * 100) + '%'
                    }
                },

                init = function() {
                    AlertsServiceDelegate.buttons.add({
                        class: 'btn-cancel',
                        onClick: function() {
                            logout();
                        },
                        text: $translate.instant('global.buttons.logout')
                    });

                    AlertsServiceDelegate.buttons.add({
                        class: 'btn-warning',
                        onClick: function() {
                            cancelCountdown();

                            Principal.identity(true); // reset backend idle time
                        },
                        text: $translate.instant('global.buttons.extendSession')
                    });

                    countdown();
                },

                logout = function() {
                    cancelCountdown();

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
                getBarStyles: getBarStyles
            }
        }
    );
})();
