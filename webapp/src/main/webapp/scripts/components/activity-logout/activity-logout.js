/**
 * Activity Logout service.
 *
 * Service to present a "Session Expiration Soon" dialog and auto-logout the session if user does not
 * choose to extend the session.
 *
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.service('ActivityLogout',
        function($rootScope, $translate, ActivityMonitor, AlertsService, Principal) {
            var isShowing = false,

                start = function(options) {
                    var opts = angular.copy(options) || {};

                    opts.idleMSBeforeWarning = (opts.idleSecsBeforeWarning || 1800) * 1000;
                    opts.secsBeforeLogout = opts.secsBeforeLogout || 30;

                    ActivityMonitor.broadcastIdle('activity-logout.idle-warning', opts.idleMSBeforeWarning);

                    $rootScope.$on('activity-logout.idle-warning', function(event, data) {
                        Principal.identity(true).then(
                            function() {
                                if (Principal.isAuthenticated()) {
                                    if (isShowing) {
                                        return;
                                    }

                                    isShowing = true;

                                    AlertsService.modal({
                                        include: {
                                            controller: 'ActivityLogoutController as ctrl',
                                            templateUrl: 'scripts/components/activity-logout/activity-logout.html',
                                            resolve: {
                                                resolvedOptions: opts
                                            }
                                        },
                                        onClose: function() {
                                            isShowing = false;
                                        },
                                        title: $translate.instant('activity-logout.sessionExpiresTitle')
                                    });
                                }
                            }
                        );

                        data.restart();
                    });
                },

                zend;

            return {
                start: start
            }
        }
    );
})();
