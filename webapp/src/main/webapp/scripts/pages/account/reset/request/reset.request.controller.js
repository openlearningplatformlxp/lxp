'use strict';

(function() {
    var module = angular.module('app.pages');

    module.controller('RequestResetController',
        function($rootScope, $state, $timeout, Auth) {
            var data = {
                    error: null, // TODO: SAC: no message presented to user!
                    errorEmailNotExists: null,
                    resetAccount: {},
                    success: null
                },

                init = function() {
                    $timeout(function() {
                        angular.element('[ng-model="ctrl.data.resetAccount.email"]').focus();
                    });
                },

                requestReset = function() {
                    data.error = null;
                    data.errorEmailNotExists = null;

                    Auth.resetPasswordInit(data.resetAccount.email).then(function() {
                        data.success = 'OK';
                    }).catch(function(response) {
                        data.success = null;
                        if (response.status === 400 && response.data === 'email address not registered') {
                            data.errorEmailNotExists = 'ERROR';
                        } else {
                            data.error = 'ERROR';
                        }
                    });
                };

            init();

            return {
                data: data,
                requestReset: requestReset
            };
        }
    );
})();