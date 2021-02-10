'use strict';

(function() {
    var module = angular.module('app.pages');

    module.controller('RegisterController',
        function(resolvedData, $stateParams, $timeout, $translate, Auth, MessagesService) {
            var data = {
                    confirmPassword: '',
                    disableEmail: false,
                    disableFirstName: false,
                    disableLastName: false,
                    disableLogin: false,
                    registerAccount: {}
                },
                pageMessages = MessagesService.getMessagesInstance(),

                init = function() {
                    if (resolvedData.person) {
                        data.registerAccount.firstName = resolvedData.person.firstName;
                        data.registerAccount.lastName = resolvedData.person.lastName;
                        data.registerAccount.login = resolvedData.person.login;
                        data.registerAccount.email = resolvedData.person.email;

                        data.disableFirstName = !!resolvedData.person.firstName;
                        data.disableLastName = !!resolvedData.person.lastName;
                        data.disableLogin = !!resolvedData.person.login;
                        data.disableEmail = !!resolvedData.person.email;
                    }

                    var focusField = undefined;

                    if (!data.disableFirstName) {
                        focusField = 'firstName';
                    } else if (!data.disableLastName) {
                        focusField = 'lastName';
                    } else if (!data.disableEmail) {
                        focusField = 'email';
                    } else if (!data.disableLogin) {
                        focusField = 'login';
                    } else {
                        focusField = 'password';
                    }

                    $timeout(function() {
                        angular.element('[ng-model="ctrl.data.registerAccount.' + focusField + '"]').focus();
                    });
                },

                register = function() {
                    pageMessages.reset();

                    if (data.registerAccount.password !== data.confirmPassword) {
                        pageMessages.addError($translate.instant('global.messages.error.dontmatch'));
                    }

                    if (pageMessages.hasErrorMessages()) {
                        return;
                    }

                    data.registerAccount.langKey = $translate.use();

                    var queryParams = {
                        key: $stateParams.key
                    };

                    Auth.createAccount(queryParams, data.registerAccount).then(function() {
                        if ($stateParams.key) {
                            pageMessages.addSuccess($translate.instant('register.messages.success.activated'));
                        } else {
                            pageMessages.addSuccess($translate.instant('register.messages.success.registered'));
                        }
                    }).catch(function(response) {
                        if (response.status === 400 && response.data === 'login already in use') {
                            pageMessages.addError($translate.instant('register.messages.error.userexists'));
                        } else if (response.status === 400 && response.data === 'email address already in use') {
                            pageMessages.addError($translate.instant('register.messages.error.emailexists'));
                        } else {
                            pageMessages.addHttpError(response);
                        }
                    });
                },

                zend;

            init();

            return {
                data: data,
                pageMessages: pageMessages,
                register: register
            };
        }
    );
})();