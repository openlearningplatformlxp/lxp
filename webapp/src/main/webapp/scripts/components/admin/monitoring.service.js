'use strict';

(function() {
    angular.module('app.components').factory('MonitoringService', function($rootScope, $http) {
        return {
            getMetrics: function() {
                return $http.get('metrics/metrics').then(function(response) {
                    return response.data;
                });
            },

            checkHealth: function() {
                return $http.get('admin/manage/health').then(function(response) {
                    return response.data;
                });
            },

            threadDump: function() {
                return $http.get('admin/manage/dump').then(function(response) {
                    return response.data;
                });
            }
        };
    });
})();