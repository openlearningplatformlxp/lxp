/**
 * Admin Notifications Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminDiscoveryProgramUpsertController',
        function(resolvedProgramsData, resolvedDiscoveryTypes, $http, $state, $stateParams, AlertsService, MessagesService, AdminDiscoveryService) {
            var data = {
                    programs: resolvedProgramsData.programs,
                    discoveryProgramItem: {
                        id: null,
                        type: null,
                        programId: null,
                        active: false,
                        textField: null
                    },
                    types: resolvedDiscoveryTypes,
                    isAbleToSave: false
                },

                pageMessages = MessagesService.getMessagesInstance(),

                init = function() {

                    if ($stateParams.discoveryProgramItem) {
                        data.discoveryProgramItem = {
                            id: $stateParams.discoveryProgramItem.id,
                            programId: "" + $stateParams.discoveryProgramItem.programId,
                            active: $stateParams.discoveryProgramItem.active,
                            type: $stateParams.discoveryProgramItem.type,
                            textField: $stateParams.discoveryProgramItem.discoveryProgramText
                        }
                    }
                    if (data.discoveryProgramItem.id != null) {
                        data.isAbleToSave = true;
                    }
                },

                cancel = function(form) {
                    if (form.$dirty) {
                        AlertsService.confirmCancel({
                            buttonOk: {
                                onClick: goToAdminDiscovery
                            }
                        });
                    } else {
                        goToAdminDiscovery();
                    }
                },

                isAbleToSave = function() {
                    data.isAbleToSave = true;

                    if (!data.discoveryProgramItem.type) {
                        data.isAbleToSave = false;
                    } else {
                        if (data.discoveryProgramItem.type == 'PROGRAM') {
                            data.discoveryProgramItem.textField = null;
                            if (!data.discoveryProgramItem.programId) {
                                data.isAbleToSave = false;
                            }
                        }
                        if (data.discoveryProgramItem.type == 'TEXT') {
                            data.discoveryProgramItem.programId = null;
                            if (!data.discoveryProgramItem.textField || data.discoveryProgramItem.textField == "" || data.discoveryProgramItem.textField == " ") {
                                data.isAbleToSave = false;
                            }
                        }
                    }
                },

                goToAdminDiscovery = function() {
                    $state.go('discovery');
                },

                save = function() {
                    if (data.isAbleToSave) {
                        AdminDiscoveryService.upsertDiscoveryProgram({
                            id: data.discoveryProgramItem.id,
                            type: data.discoveryProgramItem.type,
                            programId: data.discoveryProgramItem.programId,
                            active: data.discoveryProgramItem.active,
                            discoveryProgramText: data.discoveryProgramItem.textField
                        }).then(function(response) {
                            goToAdminDiscovery();
                        }, function(error) {
                            pageMessages.addHttpError(response);
                        });
                    }
                },

                zend;

            init();

            return {
                data: data,
                pageMessages: pageMessages,
                isAbleToSave: isAbleToSave,
                cancel: cancel,
                save: save
            };
        }
    );
})();
