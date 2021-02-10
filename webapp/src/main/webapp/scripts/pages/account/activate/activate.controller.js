'use strict';

(function() {
    var module = angular.module('app.pages');

    module.controller('ActivationController',
        function($state, $stateParams, Auth) {
            var error = null,
                success = null,

                getError = function() {
                    return error;
                },

                getSuccess = function() {
                    return success;
                },

                init = function() {
                    Auth.activateAccount({
                        key: $stateParams.key
                    }).then(function(response) {
                        if (response.activated) {
                            error = null;
                            success = 'OK';
                        } else {
                            $state.go('register', {
                                key: $stateParams.key
                            });
                        }
                    }).catch(function() {
                        success = null;
                        error = 'ERROR';
                    });
                },

                zend;

            init();

            return {
                getError: getError,
                getSuccess: getSuccess
            };
        }
    );
})();