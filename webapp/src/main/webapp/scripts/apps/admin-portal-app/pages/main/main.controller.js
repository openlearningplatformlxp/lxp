'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('MainController',
        function(resolvedAccountInfo) {
            return {
                account: resolvedAccountInfo.account,
                isAuthenticated: resolvedAccountInfo.isAuthenticated
            };
        }
    );
})();