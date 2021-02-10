/**
 * Admin Asset Add/Edit Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminAssetAddEditController',
        function(resolvedData, $http, $location, $state, $translate, AlertsService, FileUploader, TextEditorModal, MessagesService) {
            var data = {
                    asset: resolvedData.asset,
                    assetType: {
                        selected: undefined,
                        available: resolvedData.assetTypes
                    },
                    assetStoreType: {
                        selected: undefined,
                        available: resolvedData.assetStoreTypes
                    },
                    assetSubtype: {
                        selected: undefined
                    },
                    fileQueued: false
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
                                onClick: goToAdminAssets
                            }
                        });
                    } else {
                        goToAdminAssets();
                    }
                },

                editContent = function(form) {
                    var content = {
                        value: data.asset.content
                    };

                    TextEditorModal.showTextEditor({
                        content: content,
                        buttonOk: {
                            onClick: function() {
                                data.asset.content = content.value;

                                form.$setDirty();

                                onFormChange(form);
                            }
                        },
                        title: '&nbsp; <i class="fa fa-th" aria-hidden="true"></i> ' + $translate.instant('asset.asset-add-edit.title.edit', {
                            assetName: data.asset.name
                        })
                    });
                },

                getUrl = function(fullUrl) {
                    var url = 'asset/path' + (data.asset.path ? data.asset.path : '') + '/' + data.asset.filename;

                    if (fullUrl) {
                        url = $location.protocol() + '://' + $location.host() + ($location.port() != 80 ? ':' + $location.port() : '') + '/' + url;
                    }

                    return url;
                },

                goToAdminAssets = function() {
                    $state.go('assets');
                },

                init = function() {
                    var current, i;

                    if (resolvedData.httpError) {
                        pageMessages.addHttpError(resolvedData.httpError);
                        return;
                    }

                    if (resolvedData.asset) {
                        if (resolvedData.asset.assetStoreType) {
                            if (angular.isArray(data.assetStoreType.available) && data.assetStoreType.available.length > 0) {
                                for (i = 0; i < data.assetStoreType.available.length; i++) {
                                    current = data.assetStoreType.available[i];

                                    if (current.name === resolvedData.asset.assetStoreType.name) {
                                        data.assetStoreType.selected = current;
                                        break;
                                    }
                                }
                            }
                        }

                        if (resolvedData.asset.assetType) {
                            if (angular.isArray(data.assetType.available) && data.assetType.available.length > 0) {
                                for (i = 0; i < data.assetType.available.length; i++) {
                                    current = data.assetType.available[i];

                                    if (current.name === resolvedData.asset.assetType.name) {
                                        data.assetType.selected = current;
                                        break;
                                    }
                                }

                                switchAssetType();
                            }

                            if (resolvedData.asset.assetSubtype) {
                                if (angular.isArray(data.assetType.selected.subtypes) && data.assetType.selected.subtypes.length > 0) {
                                    for (i = 0; i < data.assetType.selected.subtypes.length; i++) {
                                        current = data.assetType.selected.subtypes[i];

                                        if (current.name === resolvedData.asset.assetSubtype.name) {
                                            data.assetSubtype.selected = current;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    data.uploader = makeUploader();
                },

                isAdd = function() {
                    return !isEdit();
                },

                isContentText = function() {
                    return (isSubtypeSelected() && !!data.assetSubtype.selected.text);
                },

                isEdit = function() {
                    return (resolvedData.asset && resolvedData.asset.id);
                },

                isFormDisabled = function(form) {
                    if (!isStoreTypeSupported()) {
                        return true;
                    }

                    return form.$invalid || (form.$pristine && !data.fileQueued);
                },

                isLoaded = function() {
                    return angular.isDefined(data.asset);
                },

                isStoreTypeDB = function() {
                    return isStoreTypeSelected() && data.assetStoreType.selected.name == 'DB';
                },

                isStoreTypeS3 = function() {
                    return isStoreTypeSelected() && data.assetStoreType.selected.name == 'S3';
                },

                isStoreTypeSelected = function() {
                    return (!!data.assetStoreType.selected);
                },

                isStoreTypeSupported = function() {
                    return isStoreTypeSelected() && (isStoreTypeDB() || isStoreTypeS3());
                },

                isSubtypeSelected = function() {
                    return !!data.assetSubtype.selected;
                },

                makeUploader = function() {
                    var uploader = new FileUploader({
                        onAfterAddingFile: function(item) {
                            if (uploader.queue.length > 1) {
                                uploader.removeFromQueue(0);
                            }

                            if (uploader.queue.length > 0) {

                                setAssetTypeAndSubtypeFromMimeType(data.uploader.queue[0].file.type);

                                if (!data.asset.contentType) {
                                    data.asset.contentType = data.uploader.queue[0].file.type;
                                }

                                if (!data.asset.filename) {
                                    data.asset.filename = data.uploader.queue[0].file.name;
                                }
                            }

                            data.fileQueued = (uploader.queue.length > 0);
                        },
                        onCompleteItem: function(item, response, status, headers) {
                            if (status == 200) {
                                content.value = response;

                                data.fileQueued = false;
                            }
                        },
                        onErrorItem: function(item, response, status, headers) {
                            AlertsService.alert({
                                text: $translate.instant('global.messages.error.errorOccurred'),
                                title: $translate.instant('global.messages.error.title')
                            });
                        },
                        queueLimit: 2,
                        removeAfterUpload: true
                    });

                    return uploader;
                },

                onContentTypeChange = function(form) {
                    setAssetTypeAndSubtypeFromMimeType(data.asset.contentType);

                    onFormChange(form);
                },

                onFormChange = function(form) {
                    if (form && form.$pristine) {
                        return;
                    }

                    pageMessages.resetSuccess();
                },

                save = function(form) {
                    pageMessages.reset();

                    var url,

                        onError = function(response) {
                            pageMessages.addHttpError(response);

                            disableButtons = false;
                        },

                        onSuccess = function(response) {
                            pageMessages.addSuccess(isAdd() ? $translate.instant('asset.asset-add-edit.messages.success.save-add-success') : $translate.instant('asset.asset-add-edit.messages.success.save-edit-success'));

                            form.$setPristine();
                            form.$setUntouched();

                            disableButtons = false;
                        },

                        putData = {
                            id: data.asset.id,
                            name: data.asset.name,
                            filename: data.asset.filename,

                            contentType: data.asset.contentType,

                            assetStoreType: data.assetStoreType.selected.name,

                            // for uploader call
                            assetTypeId: data.assetType.selected.id,
                            assetSubtypeId: data.assetSubtype.selected.id,

                            // for rest call
                            assetType: {
                                id: data.assetType.selected.id
                            },
                            assetSubtype: {
                                id: data.assetSubtype.selected.id
                            }
                        };

                    if (data.asset.description) {
                        putData.description = data.asset.description;
                    }

                    if (data.asset.path) {
                        putData.path = data.asset.path;
                    }

                    if (data.asset.content) {
                        putData.content = data.asset.content;
                    }

                    disableButtons = true;

                    if (data.uploader && data.uploader.queue.length > 0) {

                        data.uploader.queue[0].url = 'asset/upload' + (isEdit() ? '/' + data.asset.id : '');
                        data.uploader.queue[0].onSuccess = onSuccess;
                        data.uploader.queue[0].onError = onError;
                        data.uploader.queue[0].formData.push(putData);

                        data.uploader.uploadItem(0);
                    } else {
                        url = 'api/asset' + (isEdit() ? '/' + data.asset.id : '');

                        $http.put(url, putData).then(onSuccess, onError);
                    }
                },

                setAssetTypeAndSubtypeFromMimeType = function(mimeType) {
                    if (!mimeType) {
                        return false;
                    } else if (data.assetType.selected || data.assetSubtype.selected) {
                        return false;
                    } else if (!angular.isArray(resolvedData.assetTypes) || resolvedData.assetTypes.length < 1) {
                        return false;
                    }

                    var i,
                        j,
                        defaultMimeType,
                        foundSubtype = false,
                        foundType = false,
                        normalizedMimeType = mimeType.toLowerCase();

                    for (i = 0; i < resolvedData.assetTypes.length; i++) {
                        for (j = 0; j < resolvedData.assetTypes[i].subtypes.length; j++) {
                            defaultMimeType = resolvedData.assetTypes[i].subtypes[j].defaultContentType;

                            if (angular.isString(defaultMimeType) && defaultMimeType.toLowerCase() == normalizedMimeType) {
                                foundType = resolvedData.assetTypes[i];
                                foundSubtype = resolvedData.assetTypes[i].subtypes[j];
                                break;
                            }
                        }

                        if (foundType) {
                            break;
                        }
                    }

                    if (!foundType) {
                        return;
                    }

                    if (foundType) {
                        if (!data.assetType.selected && !data.assetSubtype.selected) {
                            data.assetType.selected = foundType;
                            switchAssetType();

                            data.assetSubtype.selected = foundSubtype;
                        }
                    }
                },

                showAssetUrl = function() {
                    return isEdit() && isStoreTypeDB() && showFormFullPath();
                },

                showFormAssetSubtype = function() {
                    return isStoreTypeSelected() && isStoreTypeSupported();
                },

                showFormAssetType = function() {
                    return isStoreTypeSelected() && isStoreTypeSupported();
                },

                showFormContent = function() {
                    return isStoreTypeSelected() && isStoreTypeSupported() && isStoreTypeDB() && isSubtypeSelected() && isContentText();
                },

                showFormContentType = function() {
                    return isStoreTypeSelected() && isStoreTypeSupported() && (isStoreTypeDB() || isStoreTypeS3);
                },

                showFormFile = function() {
                    if (isEdit() && isContentText()) {
                        return false;
                    }

                    return isStoreTypeSelected() && isStoreTypeSupported();
                },

                showFormFullPath = function() {
                    return isStoreTypeSelected() && isStoreTypeSupported();
                },

                showStoreTypeNotSupported = function() {
                    return isStoreTypeSelected() && (data.assetStoreType.selected.name != 'DB' && data.assetStoreType.selected.name != 'S3');
                },

                switchAssetSubtype = function() {
                    if (data.assetSubtype.selected && !data.asset.contentType) {
                        data.asset.contentType = data.assetSubtype.selected.defaultContentType;
                    }
                },

                switchAssetType = function() {
                    data.assetSubtype.selected = undefined;

                    if (data.assetType.selected) {
                        if (data.assetType.selected.subtypes && data.assetType.selected.subtypes.length == 1) {
                            data.assetSubtype.selected = data.assetType.selected.subtypes[0];
                        }
                    }
                },

                zend;

            init();

            return {
                data: data,

                areButtonsDisabled: areButtonsDisabled,
                cancel: cancel,
                editContent: editContent,
                getUrl: getUrl,
                isAdd: isAdd,
                isEdit: isEdit,
                isFormDisabled: isFormDisabled,
                isLoaded: isLoaded,
                onContentTypeChange: onContentTypeChange,
                onFormChange: onFormChange,
                pageMessages: pageMessages,
                save: save,
                showAssetUrl: showAssetUrl,
                showFormAssetSubtype: showFormAssetSubtype,
                showFormContent: showFormContent,
                showFormContentType: showFormContentType,
                showFormAssetType: showFormAssetType,
                showFormFile: showFormFile,
                showFormFullPath: showFormFullPath,
                showStoreTypeNotSupported: showStoreTypeNotSupported,
                switchAssetSubtype: switchAssetSubtype,
                switchAssetType: switchAssetType
            };
        }
    );
})();
