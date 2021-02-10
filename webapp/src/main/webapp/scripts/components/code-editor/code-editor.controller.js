/**
 * Code Editor Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('TextEditorController',
        function(resolvedContent, $translate, AlertsService, AlertsServiceDelegate, FileUploader, IframeResizerService) {
            var content = resolvedContent,

                iframeResizerHtml = IframeResizerService.getIframeResizerInstance({
                    content: content
                }),

                init = function() {
                    var uploader = new FileUploader({
                        onAfterAddingFile: function(item) {
                            uploader.uploadAll();
                        },
                        onBeforeUploadItem: function(item) {
                            // nothing right now
                        },
                        onCompleteItem: function(item, response, status, headers) {
                            if (status == 200) {
                                content.value = response;
                            }
                        },
                        onErrorItem: function(item, response, status, headers) {
                            AlertsService.alert({
                                text: $translate.instant('global.messages.error.errorOccurred'),
                                title: $translate.instant('global.messages.error.title')
                            });
                        },
                        removeAfterUpload: true,
                        url: 'asset/fetch'
                    });

                    AlertsServiceDelegate.buttons.add({
                        class: 'btn-default',
                        file: {
                            uploader: uploader
                        },
                        position: 'left',
                        text: $translate.instant('global.buttons.ImportFromFile')
                    });
                },

                zend;

            init();

            return {
                content: content,
                iframeResizerHtml: iframeResizerHtml
            };
        }
    );
})();
