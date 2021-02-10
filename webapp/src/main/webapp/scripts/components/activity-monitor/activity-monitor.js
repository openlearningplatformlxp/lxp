'use strict';

(function() {
    var module = angular.module('app.components');

    module.service('ActivityMonitor',
        function($document, $rootScope, $timeout) {
            var eventNames = {
                    highActivity: 'mousemove mousewheel scroll touchmove',
                    lowActivity: 'focus keydown mousedown touchstart' // click keyup
                },
                broadcasts = {},
                restart = [],
                lastEventTime = undefined,
                highActivityWatchSleepTimeMS = 1000,
                updateCount = 0,
                watchedElement = angular.element($document),

                data = {
                    lastEventTime: lastEventTime,
                    updateCount: updateCount
                },

                broadcastIdle = function(eventToBroadcast, secondsIdle) {
                    if (!isStarted()) {
                        start();
                    }

                    var broadcast = broadcasts['idle.' + eventToBroadcast];

                    if (!broadcast) {
                        broadcast = {
                            timeoutHandle: undefined,
                            public: {
                                restart: function() {
                                    restart.push({
                                        id: 'idle.' + eventToBroadcast,
                                        func: broadcastIdle,
                                        args: [eventToBroadcast, secondsIdle]
                                    });
                                }
                            }
                        };

                        broadcasts['idle.' + eventToBroadcast] = broadcast;
                    }

                    var msToCheck = secondsIdle - (new Date() - lastEventTime);

                    if (msToCheck > 0) {
                        broadcast.timeoutHandle = $timeout(function() {
                            broadcastIdle(eventToBroadcast, secondsIdle);
                        }, msToCheck);
                    } else {
                        $rootScope.$broadcast(eventToBroadcast, broadcast.public);
                    }

                    return broadcast.public;
                },

                cancelBroadcastIdle = function(eventToCancel) {
                    var broadcast = broadcasts['idle.' + eventToCancel];

                    if (broadcast && broadcast.timeoutHandle) {
                        $timeout.cancel(broadcast.timeoutHandle);
                    }

                    broadcasts['idle.' + eventToCancel] = undefined;
                },

                checkForNextActivity = function(addAllEvents) {
                    var eventNamesToWatch = eventNames.highActivity;

                    if (addAllEvents) {
                        eventNamesToWatch += ' ' + eventNames.lowActivity
                    }

                    watchedElement.on(eventNamesToWatch, function() {
                        updateLastEventTime();

                        updateCount++;
                        data.updateCount = updateCount;

                        watchedElement.off(eventNames.highActivity);

                        if (restart.length > 0) {
                            angular.forEach(restart, function(r) {
                                if (broadcasts[r.id]) {
                                    r.func.apply(this, r.args);
                                }
                            });

                            restart = [];
                        }

                        $timeout(function() {
                            checkForNextActivity();
                        }, highActivityWatchSleepTimeMS);
                    });
                },

                getData = function() {
                    if (!isStarted()) {
                        start();
                    }

                    return data;
                },

                getLastEventTime = function() {
                    if (!isStarted()) {
                        start();
                    }

                    return lastEventTime;
                },

                getSecondsIdle = function() {
                    if (!isStarted()) {
                        start();
                    }

                    return Math.trunc((new Date() - lastEventTime) / 1000);
                },

                isStarted = function() {
                    return !!lastEventTime;
                },

                start = function() {
                    updateLastEventTime();

                    checkForNextActivity(true);
                },

                updateLastEventTime = function() {
                    lastEventTime = new Date();
                    data.lastEventTime = lastEventTime;
                },

                zend;

            return {
                broadcastIdle: broadcastIdle,
                cancelBroadcastIdle: cancelBroadcastIdle,
                getData: getData,
                getLastEventTime: getLastEventTime,
                getSecondsIdle: getSecondsIdle,
                isStarted: isStarted,
                start: start
            }
        }
    );
})();
