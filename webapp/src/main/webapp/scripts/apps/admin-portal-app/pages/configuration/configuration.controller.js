'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('ConfigurationController',
        function(resolvedData, MessagesService) {
            var pageMessages = MessagesService.getMessagesInstance(),

                getConfig = function() {
                    return resolvedData.configuration;
                },

                getValue = function(value) {
                    return value;
                },

                init = function() {
                    if (resolvedData.httpError) {
                        pageMessages.addHttpError(resolvedData.httpError);
                    }
                },

                zend;

            init();

            return {
                getConfig: getConfig,
                getValue: getValue,
                pageMessages: pageMessages
            };
        }
    );
})();
