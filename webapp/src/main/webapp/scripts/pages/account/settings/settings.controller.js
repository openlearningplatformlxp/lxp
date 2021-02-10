'use strict';

(function() {
    var module = angular.module('app.pages');

    module.controller('SettingsController',
        function(resolvedAccount, $translate, Auth, Language, Principal) {
            var data = {
                    settingsAccount: resolvedAccount
                },
                error = null,
                errorEmailExists = null,
                success = null,

                getError = function() {
                    return error;
                },

                getErrorEmailExists = function() {
                    return errorEmailExists;
                },

                getSuccess = function() {
                    return success;
                },

                save = function(form) {
                    Auth.updateAccount(data.settingsAccount).then(function() {
                        form.$setPristine();
                        error = null;
                        success = 'OK';

                        Principal.identity(true).then(function(account) {
                            data.settingsAccount = account;
                        });

                        Language.getCurrent().then(function(current) {
                            if (data.settingsAccount.langKey !== current) {
                                $translate.use(data.settingsAccount.langKey);
                            }
                        });
                    }).catch(function() {
                        success = null;
                        error = 'ERROR';
                    });
                };

            return {
                data: data,
                getError: getError,
                getErrorEmailExists: getErrorEmailExists,
                getSuccess: getSuccess,
                save: save
            };
        }
    );
})();