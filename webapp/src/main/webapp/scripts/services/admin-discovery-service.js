'use strict';

(function() {
    var module = angular.module('app.services');

    module.service('AdminDiscoveryService',
        function($http, $q) {
            var getDiscoveryProgramData = function() {
                    return $http({
                        url: 'api/admin/discovery/',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                upsertDiscoveryProgram = function(params) {
                    return $http({
                        url: 'api/admin/discovery/',
                        method: 'PUT',
                        data: {
                            id: params.id,
                            type: params.type,
                            programId: params.programId,
                            discoveryProgramText: params.discoveryProgramText,
                            active: params.active
                        }
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                deleteDiscoveryProgram = function(discoveryProgramId) {
                    return $http({
                        url: 'api/admin/discovery/' + discoveryProgramId,
                        method: 'DELETE'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                };

            return {
                getDiscoveryProgramData: getDiscoveryProgramData,
                upsertDiscoveryProgram: upsertDiscoveryProgram,
                deleteDiscoveryProgram: deleteDiscoveryProgram
            };
        }
    );
})();
