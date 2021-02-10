'use strict';

(function() {
    angular.module('app.components').factory('LogsService', function($resource) {
        return $resource('api/logs', {}, {
            'findAll': {
                method: 'GET',
                isArray: true
            },
            'changeLevel': {
                method: 'PUT'
            }
        });
    });
})();