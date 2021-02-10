'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('HealthModalController',
        function(resolvedCurrentHealth) {
            return {
                currentHealth: resolvedCurrentHealth
            };
        }
    );
})();
