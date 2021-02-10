/**
 * Messages service.
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.service('MessagesService',
        function($http, $translate) {
            var messages = function() {
                var addMessageCallback = angular.noop,
                    numErrors = 0,
                    numSuccess = 0,
                    data = {
                        error: [],
                        success: []
                    },
                    addError = function(message) {
                        if (!message) {
                            return;
                        }

                        data.error.push(message);
                        numErrors++;
                        addMessageCallback();
                    },
                    addHttpError = function(response, skipExtendedInfo) {
                        var message = (response.data.message ? '<p>' + response.data.message + '</p>' : '');

                        if (!skipExtendedInfo && (response.statusText || response.status)) {
                            message += '<ul>';

                            if (response.statusText) {
                                message += '<li>Http Status Text: ' + response.statusText + '</li>';
                            }

                            if (response.status) {
                                message += '<li>Http Status: ' + response.status + '</li>';
                            }

                            message += '</ul>';
                        }

                        addError(message);
                    },
                    addSuccess = function(message) {
                        if (!message) {
                            return;
                        }

                        data.success.push(message);
                        numSuccess++;
                        addMessageCallback();
                    },
                    addXlatedError = function(key) {
                        return addError($translate.instant(key));
                    },
                    addXlatedSuccess = function(key) {
                        return addSuccess($translate.instant(key));
                    },
                    getNumErrors = function() {
                        return numErrors;
                    },
                    getNumMessages = function() {
                        return (getNumErrors() + getNumSuccess());
                    },
                    getNumSuccess = function() {
                        return numSuccess;
                    },
                    hasErrorMessages = function() {
                        return (getNumErrors() > 0);
                    },
                    hasMessages = function() {
                        return (getNumMessages() > 0);
                    },
                    hasSuccessMessages = function() {
                        return (getNumSuccess() > 0);
                    },
                    reset = function() {
                        resetSuccess();
                        resetErrors();
                    },
                    resetErrors = function() {
                        data.error = [];
                        numErrors = 0;
                    },
                    resetSuccess = function() {
                        data.success = [];
                        numSuccess = 0;
                    },
                    setAddMessageCallback = function(callback) {
                        addMessageCallback = (angular.isFunction(callback) ? callback : angular.noop);
                    };

                return {
                    data: data,

                    addError: addError,
                    addHttpError: addHttpError,
                    addSuccess: addSuccess,
                    addXlatedError: addXlatedError,
                    addXlatedSuccess: addXlatedSuccess,
                    getNumErrors: getNumErrors,
                    getNumMessages: getNumMessages,
                    getNumSuccess: getNumSuccess,
                    hasErrorMessages: hasErrorMessages,
                    hasMessages: hasMessages,
                    hasSuccessMessages: hasSuccessMessages,
                    reset: reset,
                    resetErrors: resetErrors,
                    resetSuccess: resetSuccess,
                    setAddMessageCallback: setAddMessageCallback
                }
            };

            return {
                getMessagesInstance: function() {
                    return new messages();
                }
            }
        }
    );
})();
