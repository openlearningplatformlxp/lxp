'use strict';

(function() {
    angular.module('app.components').factory('Password', function($resource) {
        return $resource('api/account/change_password', {}, {});
    });

    angular.module('app.components').factory('PasswordResetInit', function($resource) {
        return $resource('api/account/reset_password/init', {}, {})
    });

    angular.module('app.components').factory('PasswordResetFinish', function($resource) {
        return $resource('api/account/reset_password/finish', {}, {})
    });
})();