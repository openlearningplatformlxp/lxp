/**
 * Admin CMS Block Add/Edit Controller
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminCmsBlockAddEditController',
        function(resolvedData, $http, $state, $translate, AlertsService, TextEditorModal, MessagesService) {
            var data = {
                    cmsBlock: resolvedData
                },

                disableButtons = false,
                pageMessages = MessagesService.getMessagesInstance(),

                areButtonsDisabled = function() {
                    return disableButtons;
                },

                cancel = function(form) {
                    if (form.$dirty) {
                        AlertsService.confirmCancel({
                            buttonOk: {
                                onClick: goToAdminCmsBlocks
                            }
                        });
                    } else {
                        goToAdminCmsBlocks();
                    }
                },

                editContent = function(form) {
                    var content = {
                        value: data.cmsBlock.content
                    };

                    TextEditorModal.showTextEditor({
                        content: content,
                        buttonOk: {
                            onClick: function() {
                                data.cmsBlock.content = content.value;

                                form.$setDirty();

                                onFormChange(form);
                            }
                        },
                        title: '&nbsp; <i class="fa fa-th" aria-hidden="true"></i> ' + $translate.instant('cms-block-add-edit.title.edit')
                    });
                },

                goToAdminCmsBlocks = function() {
                    $state.go('cms-blocks');
                },

                init = function() {
                    if (resolvedData.httpError) {
                        pageMessages.addHttpError(resolvedData.httpError);
                    }
                },

                isAdd = function() {
                    return !isEdit();
                },

                isEdit = function() {
                    return resolvedData.id;
                },

                isFormDisabled = function(form) {
                    return form.$invalid || form.$pristine;
                },

                isLoaded = function() {
                    return angular.isDefined(data.cmsBlock);
                },

                onFormChange = function(form) {
                    if (form && form.$pristine) {
                        return;
                    }

                    pageMessages.resetSuccess();
                },

                save = function(form) {
                    pageMessages.reset();

                    var putData = {
                            id: data.cmsBlock.id,
                            key: data.cmsBlock.key,
                            name: data.cmsBlock.name,
                            description: data.cmsBlock.description,
                            content: data.cmsBlock.content
                        },
                        url = 'api/cms/block/' + (isEdit() ? '/' + resolvedData.id : '');

                    disableButtons = true;

                    $http.put(url, putData).then(function(response) {
                        pageMessages.addSuccess(isAdd() ? $translate.instant('cms-block-add-edit.messages.success.save-add-success') : $translate.instant('cms-block-add-edit.messages.success.save-edit-success'));

                        form.$setPristine();
                        form.$setUntouched();

                        disableButtons = false;
                    }, function(response) {
                        pageMessages.addHttpError(response);

                        disableButtons = false;
                    });
                },

                zend;

            init();

            return {
                data: data,

                areButtonsDisabled: areButtonsDisabled,
                cancel: cancel,
                editContent: editContent,
                isAdd: isAdd,
                isEdit: isEdit,
                isFormDisabled: isFormDisabled,
                isLoaded: isLoaded,
                onFormChange: onFormChange,
                save: save,
                pageMessages: pageMessages
            };
        }
    );
})();
