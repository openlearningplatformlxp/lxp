'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('LogsController',
        function(resolvedLoggers, LogsService) {
            var loggers = resolvedLoggers,

                changeLevel = function(name, level) {
                    LogsService.changeLevel({
                        name: name,
                        level: level
                    }, function() {
                        loggers = LogsService.findAll();
                    });
                },

                getLoggers = function() {
                    return loggers;
                },

                getLoggersCount = function() {
                    return (angular.isArray(loggers) ? loggers.length : 0);
                };

            return {
                changeLevel: changeLevel,
                getLoggers: getLoggers,
                getLoggersCount: getLoggersCount
            };
        }
    );
})();
