'use strict';

/*
 * TODO: SAC: Is this used?
 */
angular.module('app.components')
    .factory('Person', function($resource) {
        return $resource('api/persons/:login', {}, {
            'query': {
                method: 'GET',
                isArray: true
            },
            'get': {
                method: 'GET',
                transformResponse: function(data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {
                method: 'PUT'
            }
        });
    });