/**
 * Admin Notifications Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminWikipageCreateController',
        function($http, $state, $stateParams, AlertsService, MessagesService, resolvedData) {
            var data = {
                    wikipage: {
                        title: null,
                        slug: null,
                        cssContent: null,
                        htmlContent: null,
                        tags: [],
                        indexOnSearch: true,
                        status: "DRAFT"
                    },
                    selectedTag: null,
                    editor: 'quill'
                },
                pageMessages = MessagesService.getMessagesInstance(),
                cancel = function(form) {
                    if (form.$dirty) {
                        AlertsService.confirmCancel({
                            buttonOk: {
                                onClick: goToWikipages
                            }
                        });
                    } else {
                        goToWikipages();
                    }
                },
                isFormDisabled = function(form) {
                    return form != null && (form.$invalid);
                },
                goToWikipages = function() {
                    $state.go('wikipages');
                },
                saveDraft = function(form) {
                    data.wikipage.status = 'DRAFT';
                    send(form);
                },
                savePublish = function(form) {
                    data.wikipage.status = 'PUBLISHED';
                    send(form);
                },
                saveArchive = function(form) {
                    data.wikipage.status = 'ARCHIVED';
                    send(form);
                },
                switchEditor = function(editor) {
                    data.editor = editor;
                },
                send = function(form) {

                    if (!isFormDisabled(form) || data.wikipage.status === 'DRAFT' || data.wikipage.status === 'ARCHIVED') {
                        var postData = {
                                id: data.wikipage.id,
                                title: data.wikipage.title,
                                status: data.wikipage.status,
                                tags: data.wikipage.tags,
                                url: data.wikipage.slug,
                                cssContent: data.wikipage.cssContent,
                                htmlContent: data.wikipage.htmlContent,
                                indexOnSearch: data.wikipage.indexOnSearch
                            },
                            url = 'api/admin/wikipages/';

                        var request = null;
                        if (data.wikipage.id != null) {
                            request = $http.put(url, postData, {});
                        } else {
                            request = $http.post(url, postData, {});
                        }

                        request.then(
                            function success(response) {
                                if (response.data.status == 'ARCHIVED') {
                                    pageMessages.addSuccess('Wikipage has been archived!');
                                } else if (data.wikipage.id != null) {
                                    pageMessages.addSuccess('Wikipage has been updated!');
                                } else {
                                    pageMessages.addSuccess('Wikipage has been saved!');
                                }
                                data.wikipage.id = response.data.id;

                                form.$setPristine();
                                form.$setUntouched();
                            },
                            function error(response) {
                                pageMessages.addHttpError(response);
                            }
                        );
                    }
                },
                addTag = function(selectedTag, index) {
                    if (data.wikipage.tags == null) {
                        data.wikipage.tags = [];
                    }
                    data.wikipage.tags.push(data.selectedTag.description)
                    data.selectedTag = null;
                },
                removeTag = function(interest, index) {
                    data.wikipage.tags.splice(index, 1);
                },
                tagChanged = function(input) {
                    data.selectedTag = null;
                },
                selectTag = function(tag) {
                    data.selectedTag = tag;
                },
                init = function() {
                    if ($stateParams.id != null) {
                        data.wikipage = resolvedData.wikipage;
                        data.wikipage.slug = resolvedData.wikipage.url;
                    }
                };

            init();

            return {
                data: data,
                pageMessages: pageMessages,
                tagChanged: tagChanged,
                selectTag: selectTag,
                removeTag: removeTag,
                addTag: addTag,
                isFormDisabled: isFormDisabled,
                cancel: cancel,
                savePublish: savePublish,
                saveDraft: saveDraft,
                saveArchive: saveArchive,
                switchEditor: switchEditor
            };
        }
    );
})();
