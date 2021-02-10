'use strict';

(function() {
    angular.module('app.components').factory('Sessions', function($resource) {
        return $resource('api/account/sessions/:series', {}, {
            'getAll': {
                method: 'GET',
                isArray: true
            }
        });
    });
})();