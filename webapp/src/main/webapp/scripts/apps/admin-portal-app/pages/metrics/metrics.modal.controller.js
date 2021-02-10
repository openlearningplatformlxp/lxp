'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('MetricsModalController',
        function(threadDump) {
            var data = {
                    threadDump: threadDump,
                    threadDumpRunnable: 0,
                    threadDumpWaiting: 0,
                    threadDumpTimedWaiting: 0,
                    threadDumpBlocked: 0,
                    threadDumpAll: 0
                },

                getLabelClass = function(threadState) {
                    if (threadState === 'RUNNABLE') {
                        return 'label-success';
                    } else if (threadState === 'WAITING') {
                        return 'label-info';
                    } else if (threadState === 'TIMED_WAITING') {
                        return 'label-warning';
                    } else if (threadState === 'BLOCKED') {
                        return 'label-danger';
                    }
                },

                init = function() {
                    angular.forEach(threadDump, function(value) {
                        if (value.threadState === 'RUNNABLE') {
                            data.threadDumpRunnable += 1;
                        } else if (value.threadState === 'WAITING') {
                            data.threadDumpWaiting += 1;
                        } else if (value.threadState === 'TIMED_WAITING') {
                            data.threadDumpTimedWaiting += 1;
                        } else if (value.threadState === 'BLOCKED') {
                            data.threadDumpBlocked += 1;
                        }
                    });

                    data.threadDumpAll = data.threadDumpRunnable + data.threadDumpWaiting + data.threadDumpTimedWaiting + data.threadDumpBlocked;
                };

            init();

            return {
                data: data,
                getLabelClass: getLabelClass
            };
        }
    );
})();
