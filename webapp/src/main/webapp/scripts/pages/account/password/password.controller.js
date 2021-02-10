'use strict';

(function() {
    var module = angular.module('app.pages');

    module.controller('PasswordController',
        function(resolvedAccount, Auth) {
            var data = {
                    confirmPassword: '',
                    currentPassword: '',
                    newPassword: ''
                },
                doNotMatch = null,
                error = null,
                login = resolvedAccount.login,
                success = null,

                changePassword = function(form) {
                    if (data.newPassword !== data.confirmPassword) {
                        doNotMatch = 'ERROR';
                    } else {
                        doNotMatch = null;
                        Auth.changePassword(data.currentPassword, data.newPassword).then(function() {
                            error = null;
                            success = 'OK';

                            data.confirmPassword = '';
                            data.currentPassword = '';
                            data.newPassword = '';
                        }).catch(function(errorCallback) {
                            success = null;
                            error = 'server.error.' + errorCallback.data.groupName + '.' + errorCallback.data.errorCode; // TODO: SAC: error infrastructure needs added!
                        });
                    }

                    form.$setPristine();
                },

                getDoNotMatch = function() {
                    return doNotMatch;
                },

                getError = function() {
                    return error;
                },

                getErrorMessage = function() {
                    return (error != 'ERROR' && error) || 'password.messages.error';
                },

                getLogin = function() {
                    return login;
                },

                getSuccess = function() {
                    return success;
                };

            return {
                data: data,
                changePassword: changePassword,
                getDoNotMatch: getDoNotMatch,
                getError: getError,
                getErrorMessage: getErrorMessage,
                getLogin: getLogin,
                getSuccess: getSuccess
            };
        }
    );
})();