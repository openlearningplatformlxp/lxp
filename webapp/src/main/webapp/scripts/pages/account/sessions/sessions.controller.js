'use strict';

(function() {
    var module = angular.module('app.pages');

    module.controller('SessionsController',
        function(resolvedAccount, resolvedSessions, Sessions) {
            var error = null,
                sessions = resolvedSessions,
                success = null,

                getAccount = function() {
                    return resolvedAccount;
                },

                getError = function() {
                    return error;
                },

                getSessions = function() {
                    return sessions;
                },

                getSuccess = function() {
                    return success;
                },

                invalidate = function(series) {
                    Sessions.delete({
                            series: encodeURIComponent(series)
                        },
                        function() {
                            error = null;
                            success = 'OK';
                            sessions = Sessions.getAll();
                        },
                        function() {
                            success = null;
                            error = 'ERROR';
                        }
                    );
                };

            return {
                getAccount: getAccount,
                getError: getError,
                getSessions: getSessions,
                getSuccess: getSuccess,
                invalidate: invalidate
            };
        }
    );
})();