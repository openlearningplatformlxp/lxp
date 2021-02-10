/**
 * Admin Discovery Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminDiscoveryController',
        function(discoveryProgramData, $state, AdminDiscoveryService) {
            var data = {
                    discoveryPrograms: discoveryProgramData
                },

                goToAddDiscoveryProgram = function() {
                    $state.go('discovery-program-upsert');
                },

                goToEditDiscoveryProgram = function(discoveryProgram) {
                    $state.go('discovery-program-upsert', {
                        discoveryProgramItem: discoveryProgram
                    });
                },

                deleteDiscoveryProgram = function(discoveryProgram, $index) {
                    AdminDiscoveryService.deleteDiscoveryProgram(discoveryProgram.id).then(function(response) {
                        data.discoveryPrograms.splice($index, 1);
                    }, function(error) {
                        console.log(error);
                    });
                },

                zend;

            return {
                data: data,
                goToAddDiscoveryProgram: goToAddDiscoveryProgram,
                goToEditDiscoveryProgram: goToEditDiscoveryProgram,
                deleteDiscoveryProgram: deleteDiscoveryProgram
            };
        }
    );
})();
