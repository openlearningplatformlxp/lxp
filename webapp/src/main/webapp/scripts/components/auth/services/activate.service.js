'use strict';

(function() {
    angular.module('app.components').factory('Activate', function($resource) {
        return $resource('api/account/activate', {}, {
            'get': {
                method: 'GET',
                params: {},
                isArray: false
            }
        });
    });
})();