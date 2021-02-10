'use strict';

(function() {
    angular.module('app.components').factory('Register', function($resource) {
        return $resource('api/account/register', {}, {});
    });
})();