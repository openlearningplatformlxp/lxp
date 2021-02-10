'use strict';

(function() {
    var module = angular.module('app.pages');

    module.controller('ResetFinishController',
        function($stateParams, $timeout, Auth) {
            var data = {
                    confirmPassword: undefined,
                    doNotMatch: null,
                    error: undefined,
                    keyMissing: $stateParams.key === undefined,
                    resetAccount: {},
                    success: undefined
                },

                finishReset = function() {
                    if (data.resetAccount.password !== data.confirmPassword) {
                        data.doNotMatch = 'ERROR';
                    } else {
                        Auth.resetPasswordFinish({
                            key: $stateParams.key,
                            newPassword: data.resetAccount.password
                        }).then(function() {
                            data.success = 'OK';
                        }).catch(function(response) {
                            data.success = null;
                            data.error = 'ERROR';
                        });
                    }
                },

                init = function() {
                    $timeout(function() {
                        angular.element('[ng-model="ctrl.data.resetAccount.password"]').focus();
                    });
                };

            init();

            return {
                data: data,
                finishReset: finishReset
            };
        }
    );
})();